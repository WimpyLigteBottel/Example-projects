package nel.marco

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@SpringBootApplication
@EnableScheduling
@EnableCaching
class Launcher {
}


fun main(args: Array<String>) {
    runApplication<Launcher>()
}


@Service
class RunStuff(
    val cacheManager: CacheManager
) {

    val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedRate = 500, timeUnit = TimeUnit.MILLISECONDS)
    fun repeatCall() {
        WebClient.create().get().uri("http://localhost:8080").retrieve().bodyToMono(String::class.java).toFuture()
            .join().let {
                logger.info(it)
            }

    }
}


@RestController
class Controller(
    val slowLogic: SlowLogic
) {

    @GetMapping
    suspend fun helloWorld() = slowLogic.slowLogic(Random.nextInt(0, 3).toString())
}


@Service
class SlowLogic {

    //    @Cacheable(value = ["slowLogic"], key = "#a0")
    @Cacheable(value = ["slowLogic"])
    suspend fun slowLogic(input: String): String {
        val random = Random.nextInt(0, 1000)
        return "$input $random"
    }
}
