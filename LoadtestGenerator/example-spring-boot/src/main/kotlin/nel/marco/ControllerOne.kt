package nel.marco

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*


data class Person (
    var name: String? = null,
    var id: String? = null
)

@RestController
@RequestMapping("/v1")
class ControllerOne {

    val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/hello")
    fun getHello(@RequestParam name: String) {
        log.info("GET hello $name")
    }


    @PostMapping("/hello")
    fun postHello(@RequestBody person: Person) {
        log.info("POST hello ${person.name}")
    }


    @PutMapping("/hello")
    fun putHello(@RequestBody person: Person) {
        log.info("PUT hello ${person.name}")
    }
}