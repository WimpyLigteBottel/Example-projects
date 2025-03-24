package com.example.caching

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.system.measureTimeMillis


@SpringBootApplication
class CachingApplication

fun main(args: Array<String>) {
    runApplication<CachingApplication>(*args)
}

@Configuration
@EnableCaching
class CacheConfig {

    val log = LoggerFactory.getLogger(CacheConfig::class.java)
    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = CaffeineCacheManager("delay", "delayV2")
        cacheManager.setCaffeine(caffeineBuilder())

        return cacheManager
    }

    fun caffeineBuilder(): Caffeine<Any, Any> =
        Caffeine.newBuilder()
            .maximumSize(1000)
            .initialCapacity(100)
            .expireAfterWrite(Duration.ofSeconds(5))
            .recordStats()
            .evictionListener<Any?, Any?> { key, value, cause: RemovalCause ->
                log.info("evictionListener: Removing value from cache [key=$key;value=$value;cause=$cause")
            }
            .removalListener { key, value, cause ->
                log.info("removalListener: Removing value from cache [key=$key;value=$value;cause=$cause")
            }

}

@RestController
class RestController(
    val delayService: DelayService,
    var cacheManager: CacheManager
) : CommandLineRunner {

    @GetMapping
    fun initalLoad(): String {
        val measure = measureTimeMillis {
            delayService.delayInMiliseconds(Random.nextLong(5))
        }

        return "Done! $measure ms"
    }

    @GetMapping("/stats")
    fun stats(): String {
        val manager = cacheManager.getCache("delay") as CaffeineCache

        return "${manager.nativeCache.stats()}"
    }

    override fun run(vararg args: String?) {
        val measure1 = measureTimeMillis {
            repeat(10) {
                delayService.delayInMiliseconds(1000)
            }
        }

        println("Done! $measure1 ms")


        val measure2 = measureTimeMillis {
            runBlocking(Dispatchers.IO) {
                withContext(Dispatchers.IO) {
                    repeat(5) {
                        println(delayService.delayInMilisecondsCoroutine(1000).toString() + " result returned v2")
                    }
                    delay(5000)
                    println("I have waited for cache to clear")
                    println(delayService.delayInMilisecondsCoroutine(1000).toString() + " result returned v2")

                }
            }
        }
        println("V2 => Done! $measure2 ms")
    }
}

@Service
class DelayService {
    val log = LoggerFactory.getLogger("DelayService")

    @Cacheable("delay")
    fun delayInMiliseconds(amount: Long) {
        log.info("CACHE MISS $amount delay")
        TimeUnit.MILLISECONDS.sleep(amount)
    }

    @Cacheable("delayV2")
    suspend fun delayInMilisecondsCoroutine(amount: Long): Long {
        log.info("V2 => CACHE MISS $amount delay")
        delay(1000)
        return amount
    }
}


