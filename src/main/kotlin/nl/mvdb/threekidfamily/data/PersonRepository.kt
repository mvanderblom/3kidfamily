package nl.mvdb.threekidfamily.data

import nl.mvdb.threekidfamily.data.model.PersonEntity
import org.springframework.stereotype.Component

@Component
class PersonRepository {
    private val people = mutableMapOf<Long, PersonEntity>()

    fun store(person: PersonEntity) {
        people[person.id] = person
    }

    fun findAll(): List<PersonEntity> = people.values //
        .map { it.copy() }
        .toList()

    fun clear() = people.clear()

}
