package nl.mvdb.threekidfamily.web.model

import nl.mvdb.threekidfamily.data.model.PersonRefEntity

data class PersonRefDto(val id: Long) {
    fun toEntity() = PersonRefEntity(id)
}