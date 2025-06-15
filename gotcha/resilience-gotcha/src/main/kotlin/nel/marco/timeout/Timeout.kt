package nel.marco.timeout

import io.github.resilience4j.timelimiter.annotation.TimeLimiter
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture


@RestController
class ExampleRestController(
    var service: ExampleService
) {

    @GetMapping("/timeout")
    suspend fun timeout(): String? {
//        return service.timeoutFunction() // 1. What will happen?
//        return service.timeoutFunctionV2() // 2. What will happen?
        return service.timeoutFunctionV3()?.get() // 3. What will happen?
    }
}


@Service
class ExampleService {

    @TimeLimiter(name = "backendA")
    fun timeoutFunction(): String {
        return fetch(2000)
    }

    @TimeLimiter(name = "backendA")
    fun timeoutFunctionV2(): CompletableFuture<String>? {
        return CompletableFuture.supplyAsync { fetch(2000) }
    }

    @TimeLimiter(name = "backendA")
    suspend fun timeoutFunctionV3(): CompletableFuture<String>? {
        return CompletableFuture.supplyAsync { fetch(2000) }
    }


}

fun fetch(delay: Long): String {
    Thread.sleep(delay)
    return "Done!"
}
