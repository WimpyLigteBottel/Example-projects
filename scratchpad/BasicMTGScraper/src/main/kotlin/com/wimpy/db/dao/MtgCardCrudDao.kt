package com.wimpy.db.dao

import com.wimpy.db.dao.entity.MtgCard
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
open interface MtgCardCrudDao : JpaRepository<MtgCard, Long?> {
    fun findByName(name: String): Optional<MtgCard>
    fun getByName(name: String): MtgCard
}