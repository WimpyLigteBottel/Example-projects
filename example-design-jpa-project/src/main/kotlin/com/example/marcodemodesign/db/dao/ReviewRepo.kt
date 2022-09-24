package com.example.marcodemodesign.db.dao

import com.example.marcodemodesign.db.entity.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepo : JpaRepository<Review, Long> {

    fun findByRetailerId(retailerId: String): List<Review>

}