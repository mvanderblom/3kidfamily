package nl.mvdb.threekidfamily.web

import nl.mvdb.threekidfamily.service.PersonService
import nl.mvdb.threekidfamily.web.model.PersonDto
import nl.mvdb.threekidfamily.web.model.toDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/person/v1")
class PersonController(
    private val personService: PersonService,
) {
    @PostMapping
    fun storeAndValidate(person: PersonDto): ResponseEntity<List<PersonDto>> {
        val storeAndValidate = personService.storeAndValidate(person)

        if (storeAndValidate.isEmpty())
            return ResponseEntity(null, null, 444)

        return ResponseEntity(storeAndValidate.toDto(), HttpStatus.OK)
    }
}
