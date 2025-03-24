package com.example.marcodemodesign.service

import com.example.marcodemodesign.db.dao.ComplexDao
import com.example.marcodemodesign.db.dao.RatingsRepo
import com.example.marcodemodesign.db.dao.ReviewRepo
import com.example.marcodemodesign.db.entity.Rating
import com.example.marcodemodesign.db.entity.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    fun findAllReviews(): MutableList<Review> = reviewRepo.findAll()

    fun findAllRatingsFor(
        retailerId: String,
        version: Int = 1
    ): List<Rating> = when (version) {
        1 -> complexDao.findAllRatingsForRetailers(retailerId)
        else -> ratingsRepo.findRetailerRatings(retailerId)
    }


    override fun run(vararg args: String?) {
        runBlocking(Dispatchers.IO) {
            repeat(100) {
                launch { createReview(Review()) }
            }
            log.info("generating reviews done")
        }
        val findAll = reviewRepo.findAll()
        runBlocking(Dispatchers.Default) {
            findAll.forEach {
                for (x in 0..6) {
                    launch { createRating(x, it) }
                }
            }
            log.info("generating ratings done")
        }
    }

    fun createReview(review: Review) {
        review.retailerId = Random.nextInt(10).toString()
        reviewRepo.saveAndFlush(review)
        log.info("review created [$review]")
    }

    private fun createRating(x: Int, it: Review) {
        var rating = Rating()
        rating.ratingName = x.toString()
        rating.ratingValue = Random.nextInt(10)
        rating.reviewId = it.id
        rating = ratingsRepo.saveAndFlush(rating)
        log.info("rating created [$rating]")
    }
}

