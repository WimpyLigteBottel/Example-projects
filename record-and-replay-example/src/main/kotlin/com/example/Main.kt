package com.example

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration
import java.time.OffsetDateTime
import java.util.*
import java.util.concurrent.atomic.AtomicLong

@SpringBootApplication
open class Launcher


fun main() {
    runApplication<Launcher>()
}

@RestController
open class Endpoint {

    private val log = LoggerFactory.getLogger(this::class.java)

    val requests = mutableListOf<RecordRequest>()

    @GetMapping("/hello")
    fun handleFile(
        @RequestParam("name", required = false) name: String? = null,
    ): String {
        log.info("/hello?name=$name")
        val request = RecordRequest("http://localhost:8080/hello", "GET", listOf(name), OffsetDateTime.now())
        requests.add(request)
        return "Hello $name"
    }

    @GetMapping("/replay")
    fun replay() {
        val create = WebClient.create()
        var lastOffsetDateTime: OffsetDateTime = requests.first().time
        val requestWithDelay = requests.map {
            it.copy(sleepTime = 0)
        }
        log.info("Going to replay ${requestWithDelay.size} events")

        val now = OffsetDateTime.now()
        val sleepDuration = AtomicLong(0)
        requestWithDelay.forEach {
            Thread.startVirtualThread {
                Thread.sleep(sleepDuration.getAndAdd(it.sleepTime!!))
                create
                    .get()
                    .uri("http://localhost:8080/hello?name=${it.parameters.first()}")
                    .retrieve()
                    .toBodilessEntity()
                    .block()
            }.join()
        }
        val after = OffsetDateTime.now()
        println("Done!")
        log.info("This took [time=${Duration.between(now, after).toMillis()}ms]")
        log.info(
            "original took [time={}ms]",
            Duration.between(requestWithDelay.first().time, requestWithDelay.last().time).toMillis()
        )
    }
}


data class RecordRequest(
    val url: String,
    val method: String,
    val parameters: List<String?>,
    val time: OffsetDateTime,
    val sleepTime: Long? = null
)

@Component
open class StartupCommand(
    val endpoint: Endpoint
) : CommandLineRunner {

    val create = WebClient.create()

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String?) {
        kotlin.runCatching {
            val times = 1000
            repeat(times) {
                request()
            }
            Thread.sleep(1000)
            log.info("Done $times requests")
            replay()
            Thread.sleep(1000)
            replay()
        }

        System.exit(1)
    }


    private fun request() {
        create
            .get()
            .uri("http://localhost:8080/hello?name=${Random().nextInt(0, 100000)}")
            .retrieve()
            .toEntity(String::class.java)
            .block()
    }

    private fun replay() {
        create
            .get()
            .uri("http://localhost:8080/replay")
            .retrieve()
            .toEntity(String::class.java)
            .block()
    }
}