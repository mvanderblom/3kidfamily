package nl.mvdb.threekidfamily

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PersonTest {
    @Test
    fun `partner relationship is reflexive`() {
        val go = Person("Go", 40)
        val tijn = Person("Tijn", 39)

        go.addPartner(tijn)

        assertThat(go.partners).containsExactly(tijn)
        assertThat(tijn.partners).containsExactly(go)
    }

    @Test
    fun `partner relationship is idempotent`() {
        val go = Person("Go", 40)
        val tijn = Person("Tijn", 39)

        go.addPartner(tijn)
        go.addPartner(tijn)

        assertThat(go.partners).containsExactly(tijn)
        assertThat(tijn.partners).containsExactly(go)
        assertThat(go.partners).hasSize(1)
    }

    @Test
    fun `parent child relationships are reflexive`() {
        val go = Person("Go", 40)
        val tijn = Person("Tijn", 39)
        val wende = Person("Wende", 6)

        go.addChild(wende)
        wende.addParent(tijn)

        assertThat(go.children).containsExactly(wende)
        assertThat(wende.parents).containsExactly(go, tijn)
    }

    @Test
    fun `parent child relationships are idempotent`() {
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
    }

    @Test
    fun `All kids have a common ancestor`() {
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

        assertThat(listOf(wende, siebe, bo).haveCommonAncestor(go.partners.first())).isTrue
    }

    @Test
    fun `All kids have a common ancestor - illegitimate child`() {
        val go = Person("Go", 40)
        val tijn = Person("Tijn", 39)
        val wende = Person("Wende", 6)
        val siebe = Person("Siebe", 4)
        val illegitimateChild = Person("Illegitimate child", 4)

        go.addPartner(tijn)
        go.addChild(wende)
        go.addChild(siebe)
        go.addChild(illegitimateChild)
        tijn.addChild(wende)
        tijn.addChild(siebe)

        assertThat(listOf(wende, siebe, illegitimateChild).haveCommonAncestor(go.partners.first())).isFalse
    }
}