package nl.mvdb.threekidfamily.web.model

import nl.mvdb.threekidfamily.data.model.PersonEntity
import nl.mvdb.threekidfamily.service.model.Person
import java.time.LocalDate

data class PersonDto(
    val id: Long,
    val name: String,
    val birthDate: LocalDate,
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

fun List<Person>.toDto(): List<PersonDto> {
    TODO("Not yet implemented")
}

