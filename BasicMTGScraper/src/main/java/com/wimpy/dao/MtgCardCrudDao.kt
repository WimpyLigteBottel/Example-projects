package com.wimpy.dao

import org.springframework.data.repository.CrudRepository
import com.wimpy.dao.entity.MtgCard
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MtgCardCrudDao : CrudRepository<MtgCard, Long?> {
    fun findByName(name: String): Optional<MtgCard>
}