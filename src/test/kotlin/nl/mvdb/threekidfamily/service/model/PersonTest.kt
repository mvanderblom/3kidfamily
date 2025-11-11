package nl.mvdb.threekidfamily.service.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PersonTest {
    lateinit var daisy: Person
    lateinit var donald: Person
    lateinit var heuy: Person
    lateinit var dewey: Person
    lateinit var louie: Person

    @BeforeEach
    fun setUp() {
        donald = Person(1, "Donald", LocalDate.now().minusYears(39))
        daisy = Person(2, "Daisy", LocalDate.now().minusYears(40))
        heuy = Person(3, "Heuy", LocalDate.now().minusYears(6))
        dewey = Person(4, "Dewey", LocalDate.now().minusYears(4))
        louie = Person(5, "Louie", LocalDate.now().minusYears(2))
    }

    @Test
    fun `partner relationship is reflexive`() {
        daisy.addPartner(donald)

        assertThat(daisy.partners).containsExactly(donald)
        assertThat(donald.partners).containsExactly(daisy)
    }

    @Test
    fun `partner relationship is idempotent`() {
        daisy.addPartner(donald)
        daisy.addPartner(donald)

        assertThat(daisy.partners).containsExactly(donald)
        assertThat(donald.partners).containsExactly(daisy)
        assertThat(daisy.partners).hasSize(1)
    }

    @Test
    fun `parent child relationships are reflexive`() {
        daisy.addChild(heuy)
        heuy.addParent(donald)

        assertThat(daisy.children).containsExactly(heuy)
        assertThat(heuy.parents).containsExactly(daisy, donald)
    }

    @Test
    fun `All kids have a common ancestor`() {
        daisy.addPartner(donald)
        daisy.addChild(heuy)
        daisy.addChild(dewey)
        daisy.addChild(louie)
        donald.addChild(heuy)
        donald.addChild(dewey)
        donald.addChild(louie)

        assertThat(listOf(heuy, dewey, louie).haveCommonAncestor(daisy.partners.first())).isTrue
    }

    @Test
    fun `All kids have a common ancestor - illegitimate child`() {
        val illegitimateChild = Person(5, "Illegitimate child", LocalDate.now().minusYears(2))

        daisy.addPartner(donald)
        daisy.addChild(heuy)
        daisy.addChild(dewey)
        daisy.addChild(illegitimateChild)
        donald.addChild(heuy)
        donald.addChild(dewey)

        assertThat(listOf(heuy, dewey, illegitimateChild).haveCommonAncestor(daisy.partners.first())).isFalse
    }

    @Test
    fun `Age is inferred`() {
        assertThat(donald.age).isEqualTo(39)
    }
}