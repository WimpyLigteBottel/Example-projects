package nel.marco

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*
import java.util.*


@SpringBootApplication
class Main


fun main() {
    runApplication<Main>()
}


@RestController
@RequestMapping("/v1")
@CrossOrigin
class HelloWorld {

    @GetMapping(value = ["/hello"])
    fun getStaticPage(@RequestParam(required = false) name: String): String {
        return "hello $name!"
    }

}

data class Person(val id: String, val name: String, val age: Int)

@RestController
@RequestMapping("/v1/person")
@CrossOrigin
class PersonController {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val personMap = mutableMapOf<String, Person>()

    @GetMapping(value = ["/{id}"])
    fun getPerson(@PathVariable id: String): Person? {
        log.info("Retrieving person [id=$id]")
        return personMap[id]
    }

    @DeleteMapping(value = ["/{id}"])
    fun removePerson(@PathVariable id: String): Person? {
        log.info("Removing person [id=$id;person=${personMap[id]}]")
        return personMap.remove(id)
    }


    @GetMapping(value = ["/"])
    fun getPersons(): List<Person> {
        log.info("Getting persons")

        return personMap.values.toList()
    }

    @PostMapping(value = ["/"])
    fun postPerson(@RequestBody person: Person): Person? {
        val key = UUID.randomUUID().toString()
        log.info("Adding person [key=$key;person=$person]")
        return personMap.put(key, person.copy(id = key))
    }

}