package nl.mvdb.threekidfamily.service

import nl.mvdb.threekidfamily.data.PersonRepository
import nl.mvdb.threekidfamily.data.model.PersonEntity
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
        val allPeopleById = getAllPeopleById(allPeopleEntities)

        allPeopleEntities.forEach {
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

        return allPeopleById.values.toList()

    }

    private fun getAllPeopleById(allPeopleEntities: List<PersonEntity>): MutableMap<Long, Person> {
        val allPeopleById = allPeopleEntities
            .associate { it.id to Person(it.id, it.name, it.dateOfBirth) }
            .toMutableMap()

        val topLevelPersonIds = allPeopleById.keys
        allPeopleEntities
            .flatMap { it.children }
            .distinct()
            .filter { topLevelPersonIds.contains(it.id).not() }
            .forEach { allPeopleById[it.id] = Person(it.id) }

        return allPeopleById
    }

    fun getAllValid() = getAll()
        .filter(Person::isValid)

    fun storeAndValidate(personDto: PersonDto): List<Person> {
        personRepository.store(personDto.toEntity())

        return getAllValid()
    }
}

