package nl.mvdb.threekidfamily.data.model

import java.time.LocalDate

data class PersonEntity(
    val id: Long,
    val name: String? = null,
    val dateOfBirth: LocalDate? = null,
    val parent1: PersonRefEntity? = null,
    val parent2: PersonRefEntity? = null,
    val partner: PersonRefEntity? = null,
    val children: List<PersonRefEntity> = emptyList(),
)
