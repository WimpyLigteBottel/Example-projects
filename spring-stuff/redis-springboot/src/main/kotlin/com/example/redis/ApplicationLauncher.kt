package com.example.rabbitmqspringboot

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration


@SpringBootApplication
class ApplicationLauncher

fun main(args: Array<String>) {
    runApplication<ApplicationLauncher>(*args)
}


@Component
class StartupExecutor : CommandLineRunner {

    val log = LoggerFactory.getLogger(UserRepository::class.java)

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userRepositoryRedisCacheable: UserRepositoryRedisCacheable


    override fun run(vararg args: String) {
        println("starting up...")
        userRepository.removeUsers()

        userRepositoryRedisCacheable.addUserRedis("marco1")
        userRepository.findUsernameViaRedis("marco1")?.let { log.info(it) }
        userRepositoryRedisCacheable.findUsernameViaRedis("marco1")?.let { log.info(it) }

        println("done")
        System.exit(0)
    }

}


@Repository
class UserRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {

    //@Cacheable("users", key = "#username")
    fun findUsernameViaRedis(username: String): String? {
        return redisTemplate.opsForValue().get(username)
    }

    fun addUserRedis(username: String) {
        redisTemplate.opsForValue().set("$username", username, 10.seconds.toJavaDuration())
    }

    // @CacheEvict("users", allEntries = true)
    fun removeUsers() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }
}

@Repository
class UserRepositoryRedisCacheable(
    private val userRepository: UserRepository
) {

    @Cacheable("users", key = "#username")
    fun findUsernameViaRedis(username: String): String? {
        return userRepository.findUsernameViaRedis(username)
    }

    @CachePut("users", key = "#username")
    fun addUserRedis(username: String) {
        userRepository.addUserRedis(username)
    }

    @CacheEvict("users", allEntries = true)
    fun removeUsers() {
        userRepository.removeUsers()
    }
}


@Configuration
@EnableCaching
class ConfigurationRedis {

    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): RedisCacheManager {
        return RedisCacheManager.create(connectionFactory)
    }

}