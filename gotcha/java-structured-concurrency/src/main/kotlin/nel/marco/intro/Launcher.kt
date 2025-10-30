package nel.marco.intro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Launcher {
}

fun main(args: Array<String>) {
    runApplication<Launcher>()
}