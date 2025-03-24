package com.example.webflux

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.core.publisher.Flux
import reactor.util.retry.Retry
import java.time.Duration


@SpringBootApplication
open class Main


fun main(args: Array<String>) {
    runApplication<Main>(*args);
}


@Component
class RepeatedConnection : CommandLineRunner {

    val webClient = WebClient.create("http://localhost:8080")

    override fun run(vararg args: String?) {
        webClient.get()
            .retrieve()
            .bodyToFlux<String>()
            .log()
            .repeat()
            .subscribe()
    }
}


@RestController
class RestController(val numberService: NumberService) {


    @GetMapping(produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun generateNumbers() = numberService.getNumbers(0, 100)

    @GetMapping("/strings", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun helloWorl2d() = numberService.getNumbers(0, 100).map { "$it < String" }


}


@Service
class NumberService {

    @GetMapping
    fun getNumbers(from: Int, to: Int): Flux<Int> = Flux.range(from, to)
        .delayElements(Duration.ofMillis(100))
        .onErrorComplete()
}