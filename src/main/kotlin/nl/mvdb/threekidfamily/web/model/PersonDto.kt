package nl.mvdb.threekidfamily.web.model

import java.time.LocalDate

data class PersonDto(
    val id: Long,
    val name: String,
    val birthDate: LocalDate,
    val parent1: PersonRefDto? = null,
    val parent2: PersonRefDto? = null,
    val partner: PersonRefDto? = null,
    val children: List<PersonRefDto> = emptyList(),
)