package com.example.marcodemodesign.db.dao

import com.example.marcodemodesign.db.entity.Rating
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class ComplexDao {

    @Autowired
    lateinit var entityManager: EntityManager


    fun findAllRatingsForRetailers(retailerId: String): List<Rating> {
        var query = """
            select r
            FROM Rating r
            JOIN Review re
            ON r.reviewId = re.id
            where re.retailerId = :retailerId
        """.trimIndent()


        return entityManager
            .createQuery(query, Rating::class.java)
            .setParameter("retailerId", retailerId)
            .resultList
    }
}