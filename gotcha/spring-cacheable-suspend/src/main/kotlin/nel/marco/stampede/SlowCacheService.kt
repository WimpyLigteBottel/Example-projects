package nel.marco.stampede

import kotlinx.coroutines.delay
import nel.marco.intro.BASIC_CACHE_NAME
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class SlowCacheService {


    @Cacheable(value = [BASIC_CACHE_NAME])
    suspend fun slowCache(input: String): String {
        println("Slow cache: $input")
        delay(5000)
        return "$input -> ${Random.nextInt(0, 100)}"
    }

}