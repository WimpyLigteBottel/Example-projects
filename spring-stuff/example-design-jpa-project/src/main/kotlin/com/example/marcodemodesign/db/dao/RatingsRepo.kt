package com.example.marcodemodesign.db.dao

import com.example.marcodemodesign.db.entity.Rating
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RatingsRepo : JpaRepository<Rating, Long> {

    @Query(
        nativeQuery = true, value = """
            select *
            FROM Rating r
            JOIN Review re
            ON r.REVIEW_ID = re.id
            WHERE re.RETAILER_ID = ?1
    """
    )
    fun findRetailerRatings(retailerId: String): List<Rating>

}

