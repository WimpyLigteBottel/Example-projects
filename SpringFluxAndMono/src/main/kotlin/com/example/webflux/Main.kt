package com.example.webflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import kotlin.random.Random


@SpringBootApplication
open class Main


fun main(args: Array<String>) {
    runApplication<Main>(*args);
}


@RestController
class RestController(val numberService: NumberService) {


    @GetMapping(produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun generateNumbers() = numberService.getNumbers(0, 10)

    @GetMapping("/strings",produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun helloWorl2d() = numberService.getNumbers(0, 10).map { "$it < String" }


}


@Service
class NumberService {

    @GetMapping
    fun getNumbers(from: Int, to: Int): Flux<Int> = Flux.range(from, to)
        .delayElements(Duration.ofMillis(Random.nextLong(500,1000)))
        .onErrorComplete()
}