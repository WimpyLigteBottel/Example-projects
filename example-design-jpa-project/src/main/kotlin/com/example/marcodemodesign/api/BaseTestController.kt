package com.example.marcodemodesign.api

import com.example.marcodemodesign.db.dao.ComplexDao
import com.example.marcodemodesign.db.dao.RatingsRepo
import com.example.marcodemodesign.db.dao.ReviewRepo
import com.example.marcodemodesign.db.entity.Rating
import com.example.marcodemodesign.db.entity.Review
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random


@RestController
class BaseTestController : CommandLineRunner {

    val log = LoggerFactory.getLogger(BaseTestController::class.java)

    @Autowired
    lateinit var ratingRepo: RatingsRepo

    @Autowired
    lateinit var reviewRepo: ReviewRepo

    @Autowired
    lateinit var complexDao: ComplexDao

    @GetMapping("v1/review")
    fun findReviews(): List<Review> {
        val findAll = reviewRepo.findAll().toList()
        return findAll
    }

    @GetMapping("v1/{retailerId}/review")
    fun findByRetailerId(@PathVariable retailerId: String): List<Rating> {
        val findAllRatingsForRetailers = complexDao.findAllRatingsForRetailers("1")

        return findAllRatingsForRetailers
    }


    @GetMapping("v1/setup")
    fun generateBoth(@RequestParam(required = false, defaultValue = "10") amount: Int) {
        generateReviews(amount)
        generateRatings(6)
    }

    @GetMapping("v1/generate-reviews")
    fun generateReviews(@RequestParam(required = false, defaultValue = "100") amount: Int) {
        log.info("generateReviews started [amount=$amount]")
        for (x in 0..amount) {
            var review = Review()
            review.retailerId = Random.nextInt(10).toString()
            Thread.sleep(1)
            reviewRepo.save(review)
        }
        log.info("generateReviews done [amount=$amount]")
    }

    @GetMapping("v1/generate-ratings")
    fun generateRatings(@RequestParam(required = false, defaultValue = "6") amount: Int) {
        log.info("generateRatings started [amount=$amount]")
        val reviewList = reviewRepo.findAll()

        reviewList.forEach {
            for (x in 0..amount) {
                var rating = Rating()
                rating.ratingName = x.toString()
                rating.ratingValue = Random.nextInt(10)
                rating.reviewId = it.id
                ratingRepo.save(rating)
            }
        }

        log.info("generateRatings done [amount=$amount]")
    }

    override fun run(vararg args: String?) {
        generateBoth(100)
    }
}