package nl.mvdb.threekidfamily.web

import nl.mvdb.threekidfamily.web.model.PersonDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/person/v1")
class PersonController {

    @PostMapping
    fun getAndValidate(person: PersonDto): List<PersonDto> {
        return emptyList()
    }
}