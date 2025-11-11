package nl.mvdb.threekidfamily

import nl.mvdb.threekidfamily.service.PersonValidationService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PersonValidationServiceTest {
    val validationService = PersonValidationService()

    @Test
    fun `Validation works as described`() {
        val valid = validationService.isValid("Go")
        assertThat(valid).isTrue()
    }
}