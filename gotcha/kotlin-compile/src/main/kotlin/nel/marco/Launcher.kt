package nel.marco

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
class Launcher

fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}


@RestController
class Endpoint {

    @GetMapping("/helloworld")
    fun main(): String = "Hello world"
}