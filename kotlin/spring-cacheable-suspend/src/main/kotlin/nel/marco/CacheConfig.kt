package nel.marco

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

/*
Very basic example of how to code your cache.
 */

@EnableCaching
@Configuration
class CacheConfig {

    @Bean
    fun caffeineConfig() = Caffeine.newBuilder()
        .expireAfterWrite(5, TimeUnit.SECONDS)
        .recordStats() // note: how to save prometheus

    @Bean
    fun cacheManager(caffeine: Caffeine<Any, Any>): CacheManager {
        val caffeineCacheManager = CaffeineCacheManager()
        caffeineCacheManager.setCacheNames(listOf("randomResponse"))
        caffeineCacheManager.setCaffeine(caffeine)
        return caffeineCacheManager
    }
}