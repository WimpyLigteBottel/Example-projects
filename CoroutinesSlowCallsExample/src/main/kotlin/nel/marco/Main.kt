package nel.marco

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
open class Launcher


fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}