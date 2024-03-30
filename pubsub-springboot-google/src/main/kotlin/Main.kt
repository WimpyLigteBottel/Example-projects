package nel.marco


import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random


@SpringBootApplication
open class Launcher

fun main() {
    runApplication<Launcher>()
}


@RestController
open class HelloWorld(private val publisher: MessagePublisher) {

    private val log = LoggerFactory.getLogger(this::class.java)


    @GetMapping
    fun helloWorld(): String {

        repeat(100) {
            publisher.publish("Hello " + Random.nextInt(10))
        }

        return "Hello"
    }

}


