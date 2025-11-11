package nl.mvdb.threekidfamily.service.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Person(val id: Long, val name: String, val dateOfBirth: LocalDate) {
    val partners = mutableSetOf<Person>()
    val parents = mutableSetOf<Person>()
    val children = mutableSetOf<Person>()

    fun addPartner(person: Person) {
        if (partners.addPerson(person))
            person.addPartner(this)
    }

    fun addParent(person: Person) {
        if (parents.addPerson(person))
            person.addChild(this)
    }

    fun addChild(person: Person) {
        if (children.addPerson(person))
            person.addParent(this)
    }

    val hasPartner get() = partners.isNotEmpty()

    val age = ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now())

    val isValid: Boolean
        get() {
            val childrenHaveCommonAncestor = children.haveCommonAncestor(partners.firstOrNull())
            val atLeastOneUnder18 = children.any { it.age < 18 }
            return hasPartner && childrenHaveCommonAncestor && atLeastOneUnder18
        }
}

fun MutableSet<Person>.addPerson(person: Person): Boolean {
    if (this.contains(person)) return false
    return this.add(person)
}

fun Collection<Person>.haveCommonAncestor(person: Person?): Boolean {
    if (person == null) return false
    return this.all { it.parents.contains(person) }
}