package nel.marco.intro

import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class ManualCacheService(
    private val cacheManager: CacheManager
) {
    val cache: Cache = cacheManager.getCache(BASIC_CACHE_NAME)!!


    fun manualCache(input: String): String {
        // Get cached result and return
        cache.get(input, String::class.java)?.let { cachedResult ->
            return cachedResult
        }

        // normal processing
        println("randomResponse -> ManualCacheService")
        val result = "$input -> ${Random.Default.nextInt(0, 100)}"

        // update cache
        cache.putIfAbsent(input, result)

        // return result
        return result
    }


}