package nl.mvdb.threekidfamily

data class Person(val name: String, val age: Int) {
    val partners = mutableSetOf<Person>()
    val parents = mutableSetOf<Person>()
    val children = mutableSetOf<Person>()

    fun addPartner(person: Person) {
        if (partners.contains(person) || person == this) return
        partners.add(person)
        person.addPartner(this)
    }

    fun addParent(person: Person) {
        if (parents.contains(person) || person == this) return
        parents.add(person)
        person.addChild(this)
    }

    fun addChild(person: Person) {
        if (children.contains(person) || person == this) return
        children.add(person)
        person.addParent(this)
    }

    val hasPartner get() = partners.isNotEmpty()

    val isUnder18 get() = age < 18
}
