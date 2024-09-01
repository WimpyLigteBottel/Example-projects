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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
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
    var exec = Executors.newVirtualThreadPerTaskExecutor()
    val create = WebClient.create()


    @GetMapping("/hello")
    fun handleFile(
        @RequestParam("name", required = false) name: String? = null,
    ): String {
//        log.info("/hello?name=$name   |||| ${Thread.currentThread().name}")
        val request = RecordRequest("http://localhost:8080/hello", "GET", listOf(name), OffsetDateTime.now())
        requests.add(request)
        return "Hello $name"
    }

    @GetMapping("/clear")
    fun handleFile() {
        requests.clear()
        exec = Executors.newVirtualThreadPerTaskExecutor()
    }

    @GetMapping("/replay")
    fun replay() {
        var lastOffsetDateTime: OffsetDateTime = requests.first().time
        val requestWithDelay = requests.map {
            val sleepTime = Duration.between(lastOffsetDateTime, it.time).toMillis()
            lastOffsetDateTime = it.time
            it.copy(sleepTime = sleepTime)
        }
        log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXGoing to replay ${requestWithDelay.size} events")

        val latch = CountDownLatch(1)
        val now = OffsetDateTime.now()
        val sleepDuration = AtomicLong(0)
        requestWithDelay.forEach {
            exec.submit {
                val sleepDurationX = sleepDuration.getAndAdd(it.sleepTime!!)
                latch.await()
                Thread.sleep(sleepDurationX)
                create
                    .get()
                    .uri("http://localhost:8080/hello?name=${it.parameters.first()}")
                    .retrieve()
                    .toBodilessEntity()
                    .block()
            }
        }
        latch.countDown()
        exec.shutdown()
        exec.awaitTermination(5, TimeUnit.SECONDS)
        val after = OffsetDateTime.now()
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
open class StartupCommand() : CommandLineRunner {

    val create = WebClient.create()

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String?) {
        kotlin.runCatching {
            repeat(10) {
                val times = 10000
                repeat(times) {
                    request()
                }
                replay()
                clear()
            }
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

    private fun clear() {
        create
            .get()
            .uri("http://localhost:8080/clear")
            .retrieve()
            .toEntity(String::class.java)
            .block()
    }
}