package nel.marco

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class Launch {
}

fun main() {
    runApplication<Launch>()
}

@RestController
@CrossOrigin
class RestController() {

    @GetMapping("/helloWorld")
    fun helloWorld() = "HelloWorld!"


    @GetMapping("/delay")
    fun helloWorld(@RequestParam amount: Long = 1000): String {
        Thread.sleep(amount)

        return "I have waited for $amount"
    }

}