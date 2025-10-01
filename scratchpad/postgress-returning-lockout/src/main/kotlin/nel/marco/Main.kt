package nel.marco

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Launcher

fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}
