package nel.marco.intro

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

/*
This configuration sets up multiple caches with different expiration policies.
 */

const val BASIC_CACHE_NAME = "BASIC_CACHE_NAME"
const val LONG_CACHE_NAME = "LONG_CACHE_NAME"

/**
 * Configures the application's cache managers.
 *
 * This setup defines two caches, 'BASIC_CACHE_NAME' and 'LONG_CACHE_NAME',
 * each with a different expiration time (5 seconds and 1 hour, respectively).
 * It uses a SimpleCacheManager to manage these distinct CaffeineCache instances.
 */
@EnableCaching
@Configuration
class CacheConfig {

    @Bean
    fun shortLivedCache(): Cache {
        return CaffeineCache(
            BASIC_CACHE_NAME,
            Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .maximumSize(10000)
                .recordStats() // note: how to save prometheus
                .evictionListener<Any, Any> { key, value, cause ->
                    println("EvictedListener [key=$key, value=$value, reason=$cause]")
                }.build()
        )
    }

    @Bean
    fun longLivedCache(): Cache {
        return CaffeineCache(
            LONG_CACHE_NAME,
            Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.HOURS)
                .maximumSize(10000)
                .recordStats() // note: how to save prometheus
                .evictionListener<Any, Any> { key, value, cause ->
                    println("EvictedListener [key=$key, value=$value, reason=$cause]")
                }.build()
        )
    }

    @Bean
    fun cacheManager(shortLivedCache: Cache, longLivedCache: Cache): CacheManager {
        return SimpleCacheManager().apply {
            setCaches(listOf(shortLivedCache, longLivedCache))
        }
    }
}