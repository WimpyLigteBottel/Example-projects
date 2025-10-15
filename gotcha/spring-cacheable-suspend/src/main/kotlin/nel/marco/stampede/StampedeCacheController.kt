package nel.marco.stampede

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.system.measureTimeMillis

@RestController
class StampedeCacheController(
    val slowCacheService: SlowCacheService
) {

    @GetMapping("stampede")
    suspend fun stampede(@RequestParam(defaultValue = "0") number: String): String {
        return slowCacheService.slowCache(number)
    }

    @GetMapping("stampede-large")
    suspend fun stampedeLarge(@RequestParam(defaultValue = "0") number: String): String = coroutineScope {
        val time = measureTimeMillis {
            val jobs = mutableListOf<Deferred<String>>()

            // create 'requests'
            repeat(10000) {
                val element = async {
                    slowCacheService.slowCache(it.toString())
                }
                jobs.add(element)
            }

            //wait for all requests to finnish
            println("waiting for jobs now")
            jobs.awaitAll()
            println("jobs done!")
        }

        "${time}ms"
    }


}