package nel.marco

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
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
        WebClient
            .create("http://localhost:8080")
            .get()
            .uri("/hello-world")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
            .let {
                println(it)
            }

        System.exit(0)
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MarcoAnnotation(
    val value: String,
)

@Aspect
@Component
class AspectConfig {
    @Around("@annotation(MarcoAnnotation)")
    fun around(joinPoint: ProceedingJoinPoint): Any? {
        textAndFrom("1. Around before", joinPoint)
        val response = joinPoint.proceed()
        textAndFrom("4. Around after", joinPoint)
        return response
    }

    @Before("@within(org.springframework.web.bind.annotation.RestController) && @annotation(MarcoAnnotation)")
    fun before(joinPoint: JoinPoint) {
        textAndFrom("2. BEFORE", joinPoint)
    }

    @After("@annotation(MarcoAnnotation)")
    fun after(joinPoint: JoinPoint) {
        textAndFrom("3. AFTER", joinPoint)
    }

    fun textAndFrom(text: String, joinPoint: JoinPoint) {
        println("$text ${joinPoint.signature.declaringTypeName}.${joinPoint.signature.name}")
    }
}

@RestController
class HelloWorldRestController {

    @Autowired
    lateinit var failingService: FailingService

    @GetMapping("/hello-world")
    @MarcoAnnotation(value = "this-should-be-custom")
    suspend fun example(): String = failingService.example()
}

@Service
class FailingService {
    @MarcoAnnotation(value = "this-should-be-custom")
    suspend fun example(): String = "i-should-fail"
}
