package nel.marco

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@SpringBootApplication
@EnableRetry
@EnableScheduling
open class Launcher


fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}


@Component
class Run(
    private val serieCall: SerieCall,
    private val parallelCall: ParallelCall,
    private val largeAmountParallelCall: LargeAmountParallelCall
) {

    private val log = LoggerFactory.getLogger(this::class.java)


    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    fun run() {
        runBlocking {
            log.info("XXXXXXXXXXXXXXXX")
            log.info(largeAmountParallelCall.exampleWrongSetup(2))
            log.info(largeAmountParallelCall.exampleWrongSetup2(2))

            var coreSize = Runtime.getRuntime().availableProcessors()
            println("coreSize = $coreSize")
            log.info(largeAmountParallelCall.example(Dispatchers.IO, coreSize))
            log.info(largeAmountParallelCall.example(Dispatchers.Default, coreSize))


            coreSize = Runtime.getRuntime().availableProcessors() + 1
            println("coreSize = $coreSize")
            log.info(largeAmountParallelCall.example(Dispatchers.IO, coreSize))
            log.info(largeAmountParallelCall.example(Dispatchers.Default, coreSize))
        }
    }

}


@RestController
class Users {


    @GetMapping("/users")
    fun getUsers(): String {
        return "Users"
    }

}
