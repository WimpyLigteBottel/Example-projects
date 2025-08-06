package nel.marco

import kotlinx.coroutines.reactor.ReactorContext
import nel.marco.second.CUSTOM_ID
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import kotlin.coroutines.coroutineContext

@SpringBootApplication
open class Launcher


fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}


@Component
class OnStartUp : CommandLineRunner {
    override fun run(vararg args: String?) {
        val result = WebClient.create("http://localhost:8080").get()
            .uri("/hello-world")
            .headers { headers ->
                headers.put("CUSTOM-HEADER", listOf("YOU_SHOULD_NOT_LOSE_THIS"))
            }
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        println(result)
        System.exit(0)
    }

}


@RestController
class Users {

    @GetMapping("/hello-world")
    suspend fun getUsers(): String {
        someCoroutineHandler()
        return "Hello from the controller"
    }


    suspend fun someCoroutineHandler() {
        val custom_ID = coroutineContext[ReactorContext]?.context?.getOrEmpty<String>(CUSTOM_ID)
        println("1st. Accessed CUSTOM_ID in RestController.Coroutine: ${custom_ID}}")
    }

}
