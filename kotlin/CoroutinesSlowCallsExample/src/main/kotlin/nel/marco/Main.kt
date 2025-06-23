package nel.marco

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import nel.marco.coroutine.LargeAmountParallelCall
import nel.marco.javaversion.JavaLargeParallel
import nel.marco.javaversion.JavaParallel
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.CrossOrigin
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
    private val largeAmountParallelCall: LargeAmountParallelCall,
    private val javaLargeParallel: JavaLargeParallel,
    private val javaParallel: JavaParallel,
) {

    private val log = LoggerFactory.getLogger(this::class.java)


    fun javaLargeRun() {
        log.info("XXXXXXXXXXXXXXXX")
        var requestAmount = 100
        log.info(javaLargeParallel.exampleWrongSetup(requestAmount))
        log.info(javaLargeParallel.example(requestAmount))

    }

//    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    fun javaRun() {
        log.info("XXXXXXXXXXXXXXXX")
        log.info(javaParallel.exampleWrongSetup())
        log.info(javaParallel.example())

    }

}


@RestController
@CrossOrigin
class Users {

    @GetMapping("/users")
    fun getUsers(): String {
        return Thread.currentThread().name
    }

}
