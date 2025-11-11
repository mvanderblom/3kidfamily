package nl.mvdb.threekidfamily.service.model

data class Person(val name: String, val age: Int) {
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

    val isUnder18 get() = age < 18
}

fun MutableSet<Person>.addPerson(person: Person): Boolean {
    if (this.contains(person)) return false
    return this.add(person)
}

fun Collection<Person>.haveCommonAncestor(person: Person) = this.all { it.parents.contains(person) }