package nel.marco

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@EnableRetry
open class Launcher


fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}


@Component
class Run(
    private val serieCall: SerieCall,
    private val parallelCall: ParallelCall,
    private val largeAmountParallelCall: LargeAmountParallelCall
) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(this::class.java)


    override fun run(vararg args: String?) {

        runBlocking {
            log.info(largeAmountParallelCall.exampleWrongSetup())
            log.info(largeAmountParallelCall.exampleWrongSetup2())
            log.info(largeAmountParallelCall.example())
        }

    }

}


@RestController
class Users {


    @GetMapping("/users")
    fun getUsers(): String {
        Thread.sleep(1000)
        return "Users"
    }

}
