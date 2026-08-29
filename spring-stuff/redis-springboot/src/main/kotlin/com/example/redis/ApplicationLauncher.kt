package com.example.rabbitmqspringboot

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository


@SpringBootApplication
class ApplicationLauncher

fun main(args: Array<String>) {
    runApplication<ApplicationLauncher>(*args)
}


@Component
class StartupExecutor : CommandLineRunner {

    @Autowired
    lateinit var userRepository: UserRepository


    override fun run(vararg args: String) {
        println("starting up...")
        userRepository.addUser("marco")
        userRepository.addUser("polo")
        userRepository.findByUsername("marco")
        userRepository.findByUsername("marco")
        userRepository.findByUsername("polo")
        userRepository.findByUsername("polo")
        println("done")
    }

}


@Repository
class UserRepository {

    val log = LoggerFactory.getLogger(UserRepository::class.java)
    val users = arrayListOf<String>()


    @Cacheable("users", key = "#username")
    fun findByUsername(username: String): String? {
        Thread.sleep(1000)
        log.info("findByUsername $username")
        return users.find { it == username }
    }

    fun addUser(username: String): String {
        Thread.sleep(1000)
        log.info("addUser $username")
        val userExist = users.find { it == username }

        userExist ?: users.add(username)

        return users.find { it == username }!!
    }

    @CacheEvict("users", allEntries = true)
    fun removeUsers() {
        users.clear()
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