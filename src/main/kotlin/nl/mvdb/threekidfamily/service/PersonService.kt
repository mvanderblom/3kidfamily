package nl.mvdb.threekidfamily.service

import nl.mvdb.threekidfamily.data.PersonRepository
import nl.mvdb.threekidfamily.service.model.Person
import nl.mvdb.threekidfamily.web.model.PersonDto
import org.springframework.stereotype.Component

@Component
class PersonService(
    private val personRepository: PersonRepository,
) {
    fun store(vararg personDtos: PersonDto) = personDtos
        .map(PersonDto::toEntity)
        .forEach(personRepository::store)

    fun getAll(): List<Person> {
        val allPeopleEntities = personRepository.findAll()

        val allPeopleById: Map<Long, Person> = allPeopleEntities
            .associate { it.id to Person(it.id, it.name, it.dateOfBirth) }

        return allPeopleEntities.map {
            val person = requireNotNull(allPeopleById[it.id]) { "WTF!?" }
            it.children
                .mapNotNull { (childId) -> allPeopleById[childId] }
                .forEach(person::addChild)

            listOfNotNull(it.parent1, it.parent2)
                .mapNotNull { (parentId) -> allPeopleById[parentId] }
                .forEach(person::addParent)

            it.partner
                ?.let { (partnerId) -> allPeopleById[partnerId] }
                ?.also(person::addPartner)

            person
        }

    }

    fun getAllValid() = getAll()
        .filter(Person::isValid)

    fun storeAndValidate(personDto: PersonDto): List<Person> {
        personRepository.store(personDto.toEntity())

        return getAllValid()
    }
}

