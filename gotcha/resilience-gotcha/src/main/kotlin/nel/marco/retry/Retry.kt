package nel.marco.retry

import io.github.resilience4j.retry.annotation.Retry
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ExampleRetryRestController(
    val retryService: RetryService
) {

    @GetMapping("/retry")
    fun retry() {
        retryFunction() // 1
//        retryService.retryFunctionVersion2("danger?") // 2
//        retryService.retryFunctionVersion2() // 3
    }

    @Retry(name = "backendA")
    fun retryFunction(): String {
        println("Doing retryFunction!") // 1. How many times will this print?
        throw RuntimeException("Retry function failed!")
    }


}

@Service
class RetryService(
    private val globalString: String = "retryFunctionVersion2"
) {

    @Retry(name = "backendA")
    fun retryFunctionVersion2(defaultValue: String = this.globalString): String {
        println("Doing $defaultValue!") // 1. How many times will this print?
        throw RuntimeException("Retry function failed!")
    }
}


