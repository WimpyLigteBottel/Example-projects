package com.example.marcodemodesign.db.dao

import com.example.marcodemodesign.db.entity.Rating
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RatingsRepo : JpaRepository<Rating, Long> {


}

