package com.example.marcodemodesign.service

import com.example.marcodemodesign.db.dao.ComplexDao
import com.example.marcodemodesign.db.dao.RatingsRepo
import com.example.marcodemodesign.db.dao.ReviewRepo
import com.example.marcodemodesign.db.entity.Rating
import com.example.marcodemodesign.db.entity.Review
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class ReviewService(val ratingsRepo: RatingsRepo, val reviewRepo: ReviewRepo, val complexDao: ComplexDao) :
    CommandLineRunner {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun findAllReviews(): MutableList<Review> {
        return reviewRepo.findAll()
    }

    fun findAllRatingsFor(retailerId: String): List<Rating> {
        return complexDao.findAllRatingsForRetailers(retailerId)
    }

    fun createReview(review: Review) {
        review.retailerId = Random.nextInt(10).toString()
        reviewRepo.saveAndFlush(review)
        log.info("review created [$review]")
    }

    fun createRating(rating: Rating) {
        ratingsRepo.saveAndFlush(rating)
    }

    override fun run(vararg args: String?) {
        runBlocking {
            for (x in 0..10000) {
                async { createReview(Review()) }
            }
        }
        log.info("generating reviews done")
        val findAll = reviewRepo.findAll()
        runBlocking {
            findAll.forEach {
                for (x in 0..6) {
                    async { createReview(x, it) }
                }
            }

        }
        log.info("generating ratings done")
    }

    private fun createReview(x: Int, it: Review) {
        var rating = Rating()
        rating.ratingName = x.toString()
        rating.ratingValue = Random.nextInt(10)
        rating.reviewId = it.id
        createRating(rating)
        log.info("rating created [$rating]")
    }
}

