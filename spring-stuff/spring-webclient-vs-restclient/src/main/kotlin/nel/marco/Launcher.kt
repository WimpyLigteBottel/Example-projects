package nel.marco

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

@SpringBootApplication
open class Launcher

fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}

@Component
class OnStartUp : CommandLineRunner {

    val restClient = RestClient.create("http://localhost:8080")
    val executor: Executor = Executors.newVirtualThreadPerTaskExecutor()

    override fun run(vararg args: String) = runBlocking(Dispatchers.IO) {
        (8 downTo 1).map { count ->
            (1..count).map {
                CompletableFuture.supplyAsync({ runBlocking { call() } }, executor)  // just this
            }
        }.forEach { futures ->
            val ms = measureTimeMillis {
                val ans = CompletableFuture.allOf(*futures.toTypedArray()).get()

                futures.forEach { f ->
                    print(f.state().name + " ")
                }
            }
            println("${futures.size} took ${ms}ms")
        }
    }


    suspend fun call(): String = withContext(Dispatchers.IO) {
        val result = restClient
            .post()
            .uri("/person")
            .headers { headers ->
                headers.put("CUSTOM-HEADER", listOf("YOU_SHOULD_NOT_LOSE_THIS"))
            }
            .exchangeForRequiredValue { _, response ->
                when (response.statusCode.value()) {
                    200 -> {
                        val bodyTo = response.bodyTo(Person::class.java)
                        Response.OK(person = bodyTo!!)
                    }

                    500 -> Response.Error(message = response.bodyTo(String::class.java) ?: "Unknown error")
                    else -> throw RuntimeException("Unexpected status: ${response.statusCode.value()}")
                }
            }

        when (result) {
            is Response.OK -> "Success: ${result.person}"
            is Response.Error -> "Error: ${result.message}"
        }
    }
}

sealed class Response {
    data class OK(val person: Person) : Response()
    data class Error(val message: String) : Response()
}

data class Person(val name: String = "", val email: String = "")

@RestController
class Users {

    @RequestMapping("/hello-world")
    suspend fun getUsers(@RequestBody requestBody: String?): String {
        return "Hello from the controller [msg=$requestBody]"
    }

    @RequestMapping("/person")
    suspend fun getPerson(@RequestHeader request: Map<String, Any?>): ResponseEntity<String> {
        println(request)


        delay(1000)
        return ResponseEntity.status(500).body("Failed")
    }
}