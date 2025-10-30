package nel.marco.java

import nel.marco.Person
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.concurrent.Callable
import java.util.concurrent.StructuredTaskScope

@RestController
class PersonController {

    @GetMapping("/java/person")
    fun getPerson(): Person {
        return StructuredTaskScope.open<Any>().use { scope ->
            val name = scope.fork<String>(Callable { fetch("person") })
            val age = scope.fork<Int>(Callable { fetch("1").toInt() })
            val birthDate = scope.fork<LocalDate>(Callable { LocalDate.parse(fetch("1900-01-01")) })

            scope.join()

            Person(age.get(), name.get(), birthDate.get())
        }
    }

    fun fetch(info: String, delayWait: Long = 500): String {
        Thread.sleep(delayWait)
        return info
    }
}