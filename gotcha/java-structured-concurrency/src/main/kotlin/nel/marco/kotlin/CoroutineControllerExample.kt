package nel.marco.kotlin

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import nel.marco.Person
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class CoroutineControllerExample {

    @GetMapping("/kotlin/person")
    suspend fun getPerson(): Person = coroutineScope {

        val name = async { fetch("John") }
        val age = async { fetch("1") }


        Person(age.await().toInt(), name.await(), LocalDate.now())
    }


    suspend fun fetch(info: String, delayWait: Long = 500): String {
        delay(delayWait)
        return info
    }
}