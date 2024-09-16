package nel.marco

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime


@SpringBootApplication
class ServerLauncher

fun main(args: Array<String>) {
    runApplication<ServerLauncher>(*args)
}

@RestController
class InputController(
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/report")
    fun helloWorld(@RequestParam allParams: Map<String, Any>): String {
        log.info("received: ${allParams}")
        return "$allParams"
    }

    @GetMapping("/report/{path}")
    fun helloWorld(
        @PathVariable("path") path: String,
        @RequestParam allParams: Map<String, Any>): String {
        log.info("path: $path")
        log.info("received: ${allParams}")
        return "$allParams"
    }

}