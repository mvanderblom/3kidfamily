package nl.mvdb.threekidfamily.data.model

import java.time.LocalDate

data class PersonEntity(
    val id: Long,
    val name: String,
    val dateOfBirth: LocalDate,
    val parent1: PersonRefEntity? = null,
    val parent2: PersonRefEntity? = null,
    val partner: PersonRefEntity? = null,
    val children: List<PersonRefEntity> = emptyList(),
)
