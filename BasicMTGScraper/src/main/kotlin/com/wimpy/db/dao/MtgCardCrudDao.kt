package com.wimpy.db.dao

import org.springframework.data.repository.CrudRepository
import com.wimpy.db.entity.MtgCard
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MtgCardCrudDao : CrudRepository<MtgCard, Long?> {
    fun findByName(name: String): Optional<MtgCard>
}