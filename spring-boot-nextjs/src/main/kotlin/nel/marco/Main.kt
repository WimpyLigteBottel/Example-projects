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
    fun getStaticPage(
        @RequestParam(required = false) name: String? = null,
        @RequestParam name1: String = "name1",
        @RequestParam name2: String? = "name2",
        @RequestParam(defaultValue = "3name") name3: String = "name3",
        @RequestParam name4: String = "name4",
    ): String {

        println("name $name")
        println("name1 $name1")
        println("name2 $name2")
        println("name3 $name3")
        println("name4 $name4")

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