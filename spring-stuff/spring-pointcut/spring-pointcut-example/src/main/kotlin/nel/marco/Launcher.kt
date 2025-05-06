package nel.marco

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
open class Launcher


fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}


@Component
class OnStartUp : CommandLineRunner {
    override fun run(vararg args: String?) {
        val result = WebClient.create("http://localhost:8080").get().uri("/hello-world").retrieve()
            .bodyToMono(String::class.java).block()

        println(result)
        System.exit(0)
    }

}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MarcoAnnotation(val value: String)

@Aspect
@Component
class AspectConfig {

    @Around("@annotation(MarcoAnnotation)")
    fun around(joinPoint: ProceedingJoinPoint): Any? {
        println("1. Around before ${joinPoint.signature.name}")
        val response = joinPoint.proceed()
        println("4. Around after ${joinPoint.signature.name}")
        return response
    }

    @Before("@annotation(MarcoAnnotation)")
    fun before(joinPoint: JoinPoint) {
        println("2. BEFORE JOIN POINT ${joinPoint.signature.name}")
    }

    @After("@annotation(MarcoAnnotation)")
    fun after(joinPoint: JoinPoint) {
        println("3. AFTER JOIN POINT ${joinPoint.signature.name}")
    }
}

@RestController
class Users {

    @GetMapping("/hello-world")
    @MarcoAnnotation(value = "this-should-be-custom")
    suspend fun getUsers(): String {
        return "Hello from the controller"
    }
}
