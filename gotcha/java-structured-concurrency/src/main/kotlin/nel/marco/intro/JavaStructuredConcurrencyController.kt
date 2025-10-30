package nel.marco.intro

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.Callable
import java.util.concurrent.StructuredTaskScope
import java.util.concurrent.StructuredTaskScope.Joiner
import java.time.LocalDate
import kotlin.random.Random

@RestController
class JavaStructuredConcurrencyController {


    @GetMapping("/java/person")
    fun getPerson(): Person {
        var result = StructuredTaskScope.open<Any>().use { scope ->
            val name = scope.fork<String>(Callable { fetch("person") })
            val age = scope.fork<Int>(Callable { fetch("1").toInt() })
            val birthDate = scope.fork<LocalDate>(Callable { LocalDate.parse(fetch("1900-01-01")) })

            // 1.
            scope.join()

            Person(age.get(), name.get(), birthDate.get())
        }

        return result
    }

    @GetMapping("/java/partial")
    fun getFirstSuccessResponse(): String {
        val result: String = StructuredTaskScope.open(Joiner.anySuccessfulResultOrThrow<String>()).use { scope ->
            scope.fork<String>(Callable { fetch("slow response", 1000, isFlaky = true) })
            scope.fork<String>(Callable { fetch("fast response", 200, isFlaky = true) })

            try {
                scope.join()
            } catch (e: Exception) {
                return "Both Failed ${e.message}"
            }
        }

        return result
    }


    fun fetch(info: String, delayWait: Long = 500, isFlaky: Boolean = false): String {
        val nextValue = Random.nextInt(0, 100)
        if (isFlaky && nextValue > 50) {
            throw RuntimeException("Flaky")
        }
        Thread.sleep(delayWait)
        return info
    }
}