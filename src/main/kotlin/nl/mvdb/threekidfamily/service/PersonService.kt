package nl.mvdb.threekidfamily.service

import nl.mvdb.threekidfamily.data.PersonRepository
import nl.mvdb.threekidfamily.service.model.Person
import nl.mvdb.threekidfamily.web.model.PersonDto
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class PersonService(
    private val personRepository: PersonRepository,
) {

    init {
        val go = Person(1, "Go", LocalDate.of(1985, 2, 14))
        val tijn = Person(2, "Tijn", LocalDate.of(1986, 1, 7))
        val wende = Person(3, "Wende", LocalDate.of(2019, 12, 29))
        val siebe = Person(4, "Siebe", LocalDate.of(2021, 7, 12))
        val bo = Person(5, "Bo", LocalDate.of(2013, 8, 13))

        go.addPartner(tijn)
        go.addChild(wende)
        go.addChild(siebe)
        go.addChild(bo)
        tijn.addChild(wende)
        tijn.addChild(siebe)
        tijn.addChild(bo)

    }


    fun storeAndValidate(personDto: PersonDto): List<Person> {
        personRepository.store(personDto.toEntity())

        val allPeopleEntities = personRepository.findAll()

        val allPeopleById: Map<Long, Person> = allPeopleEntities
            .associate { it.id to Person(it.id, it.name, it.dateOfBirth) }

        val people = allPeopleEntities.map {
            val person = requireNotNull(allPeopleById[it.id]) { "WTF!?" }
            it.children
                .map { (childId) -> allPeopleById[childId]!! }
                .forEach(person::addChild)

            listOfNotNull(it.parent1, it.parent2)
                .map { (parentId) -> allPeopleById[parentId]!! }
                .forEach(person::addParent)

            it.partner
                ?.let { (partnerId) -> allPeopleById[partnerId]!! }
                ?.also(person::addPartner)

            person
        }

        return people
            .filter(Person::isValid)
    }
}

