package com.example.marcodemodesign.api

import com.example.marcodemodesign.service.ReviewService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.time.Instant


@RestController
class BaseTestController(val reviewService: ReviewService) {

    val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("v1/review")
    fun findReviews(): Any {
        return timeMethod {
            reviewService.findAllReviews()
        }
    }

    @GetMapping("v1/ratings")
    fun findByRetailerId(@RequestParam(required = false, defaultValue = "") retailerId: String): Any {
        return timeMethod { reviewService.findAllRatingsFor(retailerId) }
    }

    @GetMapping("v2/ratings")
    fun findByRetailerIdVersion(@RequestParam(required = false, defaultValue = "") retailerId: String): Any {
        return timeMethod { reviewService.findAllRatingsFor(retailerId, 2) }
    }

    fun timeMethod(function: () -> Any): Any {
        val now = Instant.now()
        val invoke = function.invoke()
        val totalDuration = Duration.between(now, Instant.now()).toMillis()
        log.info("method took $totalDuration ms")
        return invoke
    }

}