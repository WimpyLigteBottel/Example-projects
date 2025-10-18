package nel.marco

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class Launch {
}

fun main() {
    runApplication<Launch>()
}

@RestController
@CrossOrigin
class CustomRestController() {

    @GetMapping("/helloWorld")
    fun helloWorld() = "HelloWorld!"


    @GetMapping("/delay")
    fun helloWorld(@RequestParam amount: Long = 1000): String {
        Thread.sleep(amount)

        return "I have waited for $amount"
    }

}


@RestController
@CrossOrigin
class PersonRestController() {
    var person: Person = Person("name", "surname", 0)

    @PostMapping("/person")
    fun updatePerson(@RequestParam name: String, @RequestParam surname: String, @RequestParam age: Int): Person {
        person = Person(name, surname, age)
        return person
    }

    @GetMapping("/person")
    fun getPersonMemory(): Person {
        return person
    }
}
