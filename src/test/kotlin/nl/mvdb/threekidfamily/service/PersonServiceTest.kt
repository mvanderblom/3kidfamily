package nl.mvdb.threekidfamily.service

import nl.mvdb.threekidfamily.data.PersonRepository
import nl.mvdb.threekidfamily.service.model.Person
import nl.mvdb.threekidfamily.web.model.PersonDto
import nl.mvdb.threekidfamily.web.model.PersonRefDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PersonServiceTest {
    private val personRepository = PersonRepository()
    private val personService = PersonService(personRepository)

    private lateinit var donaldDto: PersonDto
    private lateinit var daisyDto: PersonDto
    private lateinit var heuyDto: PersonDto
    private lateinit var deweyDto: PersonDto
    private lateinit var louieDto: PersonDto

    @BeforeEach
    fun setUp() {
        donaldDto = PersonDto(
            1,
            "Donald",
            LocalDate.now().minusYears(39),
            partner = PersonRefDto(2),
            children = listOf(PersonRefDto(3), PersonRefDto(4), PersonRefDto(5))
        )
        daisyDto = PersonDto(
            2,
            "Daisy",
            LocalDate.now().minusYears(40),
            children = listOf(PersonRefDto(3), PersonRefDto(4), PersonRefDto(5))
        )
        heuyDto = PersonDto(3, "Heuy", LocalDate.now().minusYears(6))
        deweyDto = PersonDto(4, "Dewey", LocalDate.now().minusYears(4))
        louieDto = PersonDto(5, "Louie", LocalDate.now().minusYears(2))
    }

    @AfterEach
    fun tearDown() {
        personRepository.clear()
    }

    @Test
    fun `a person can be stored`() {
        personService.store(donaldDto)

        val people = personService.getAll()

        assertThat(people).hasSize(1)
        assertThat(people.first().name).isEqualTo(donaldDto.name)
    }

    @Test
    fun `a person can be updated`() {
        personService.store(donaldDto)
        personService.store(donaldDto.copy(name = "Donaldson"))

        val people = personService.getAll()

        assertThat(people).hasSize(1)
        assertThat(people.first().name).isEqualTo("Donaldson")
    }

    @Test
    fun `family relationships are inferred`() {
        personService.store(donaldDto, daisyDto, heuyDto, deweyDto, louieDto)
        val donald = personService.getAll().single { it.id == donaldDto.id }
        assertThat(donald.partners.single().name).isEqualTo(daisyDto.name)
        assertThat(donald.children.map(Person::name)).containsExactlyInAnyOrder(
            heuyDto.name,
            deweyDto.name,
            louieDto.name
        )
    }

    @Test
    fun `only valid people are returned`() {
        personService.store(donaldDto, daisyDto, heuyDto, deweyDto, louieDto)
        val allValidPeople = personService.getAllValid()
        assertThat(allValidPeople).hasSize(2)
        assertThat(allValidPeople.map(Person::name)).containsExactlyInAnyOrder(donaldDto.name, daisyDto.name)
    }

    @Test
    fun `minimal set of valid people`() {
        personService.store(
            PersonDto(
                id = 1,
                name = "Donald",
                birthDate = LocalDate.now().minusYears(39),
                partner = PersonRefDto(2),
                children = listOf(PersonRefDto(3), PersonRefDto(4), PersonRefDto(5))
            ),
            PersonDto(
                id = 2,
                name = "Daisy",
                birthDate = LocalDate.now().minusYears(39),
                children = listOf(PersonRefDto(3), PersonRefDto(4), PersonRefDto(5))
            ),
            PersonDto(3, "Heuy", LocalDate.now().minusYears(6))
        )

        assertThat(personService.getAllValid()).hasSize(2)
    }

    @Test
    fun `auto repair`() {
        personService.store(
            PersonDto(
                id = 1,
                name = "Donald",
                birthDate = LocalDate.now().minusYears(39),
                children = listOf(PersonRefDto(3))
            ),
            PersonDto(3, "Heuy", LocalDate.now().minusYears(6))
        )

        val people = personService.getAll()
        assertThat(people).hasSize(2)
        assertThat(people[0].children).hasSize(1)
        assertThat(people[1].parents).hasSize(1)
    }
}