package com.example.marcodemodesign.api

import com.example.marcodemodesign.db.entity.Rating
import com.example.marcodemodesign.db.entity.Review
import com.example.marcodemodesign.service.ReviewService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.time.Instant


@RestController
class BaseTestController(var reviewService: ReviewService) {

    val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("v1/review")
    fun findReviews(): Any {
        val function: () -> MutableList<Review> = {
            reviewService.findAllReviews()
        }

        return timeMethod(function)
    }

    @GetMapping("v1/ratings")
    fun findByRetailerId(@RequestParam(required = false, defaultValue = "") retailerId: String): Any {
        val function: () -> List<Rating> = {
            reviewService.findAllRatingsFor(retailerId)
        }

        return timeMethod(function)
    }

    fun timeMethod(function: () -> Any): Any {
        val now = Instant.now()
        val invoke = function.invoke()
        val totalDuration = Duration.between(now, Instant.now()).toMillis()
        log.info("method took $totalDuration ms")
        return invoke
    }

}