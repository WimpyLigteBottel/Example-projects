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
        val result = StructuredTaskScope.open(Joiner.anySuccessfulResultOrThrow<Any>()).use { scope ->
            val slowButReliable = scope.fork<String>(Callable { fetch("slow response", 1000) })
            val fastButFlaky = scope.fork<String>(Callable { fetch("fast response", 200, isFlaky = true) })

            // need to do join
            scope.join()

            when (fastButFlaky.state()) {
                StructuredTaskScope.Subtask.State.SUCCESS -> return fastButFlaky.get()
                StructuredTaskScope.Subtask.State.UNAVAILABLE -> null
                StructuredTaskScope.Subtask.State.FAILED -> null
            }


            // handle partial success
            when (slowButReliable.state()) {
                StructuredTaskScope.Subtask.State.SUCCESS -> return slowButReliable.get()
                StructuredTaskScope.Subtask.State.UNAVAILABLE -> null
                StructuredTaskScope.Subtask.State.FAILED -> null
            }

            "Both Failed"
        }

        return result
    }


    fun fetch(info: String, delayWait: Long = 500, isFlaky: Boolean = false): String {
        Thread.sleep(delayWait)
        val nextValue = Random.nextInt(0, 100)
        if (isFlaky && nextValue > 50) {
            throw RuntimeException("Flaky")
        }
        return info
    }
}