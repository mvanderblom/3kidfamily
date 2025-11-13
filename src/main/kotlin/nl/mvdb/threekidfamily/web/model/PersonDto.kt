package nl.mvdb.threekidfamily.web.model

import nl.mvdb.threekidfamily.data.model.PersonEntity
import nl.mvdb.threekidfamily.service.model.Person
import java.time.LocalDate

data class PersonDto(
    val id: Long,
    val name: String? = null,
    val birthDate: LocalDate? = null,
    val parent1: PersonRefDto? = null,
    val parent2: PersonRefDto? = null,
    val partner: PersonRefDto? = null,
    val children: List<PersonRefDto> = emptyList(),
) {
    fun toEntity() = PersonEntity(
        id = id,
        name = name,
        dateOfBirth = birthDate,
        parent1 = parent1?.toEntity(),
        parent2 = parent2?.toEntity(),
        partner = partner?.toEntity(),
        children = children.map { it.toEntity() }
    )
}

fun List<Person>.toDto(): List<PersonDto> = this.map(Person::toDto)

private fun Person.toDto(): PersonDto {
    val parents = parents.toList()
    val parent1 = parents.firstOrNull()
    val parent2 = parents.secondOrNull()
    return PersonDto(
        id = id,
        name = name,
        birthDate = dateOfBirth,
        parent1 = parent1?.let { PersonRefDto(it.id) },
        parent2 = parent2?.let { PersonRefDto(it.id) },
        partner = partners.firstOrNull()?.let { PersonRefDto(it.id) },
        children = children.map { PersonRefDto(it.id) }
    )
}

private fun <T> List<T>.secondOrNull(): T? {
    if (this.size > 1) return this[1]
    return null
}
