package nel.marco

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
open class Launcher

fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}



