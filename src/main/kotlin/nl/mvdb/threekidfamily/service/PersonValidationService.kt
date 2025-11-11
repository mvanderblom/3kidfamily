package nl.mvdb.threekidfamily.service

import nl.mvdb.threekidfamily.service.model.Person
import nl.mvdb.threekidfamily.service.model.haveCommonAncestor
import org.springframework.stereotype.Component

@Component
class PersonValidationService {

    private val people: List<Person>

    init {
        val go = Person("Go", 40)
        val tijn = Person("Tijn", 39)
        val wende = Person("Wende", 6)
        val siebe = Person("Siebe", 4)
        val bo = Person("Bo", 4)

        go.addPartner(tijn)
        go.addChild(wende)
        go.addChild(siebe)
        go.addChild(bo)
        tijn.addChild(wende)
        tijn.addChild(siebe)
        tijn.addChild(bo)

        this.people = listOf(go, tijn, wende, siebe, bo)
    }

    fun isValid(name: String): Boolean {
        val person = requireNotNull(people.find { it.name == name }) { "Person not found" }

        val hasPartner = person.hasPartner
        val childrenHaveCommonAncestor = person.children.haveCommonAncestor(person.partners.first())
        val atLeastOneUnder18 = person.children.any { it.age < 18 }
        return hasPartner && childrenHaveCommonAncestor && atLeastOneUnder18
    }
}