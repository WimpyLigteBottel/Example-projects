package com.wimpy.dao

import org.springframework.data.repository.CrudRepository
import com.wimpy.dao.entity.MtgHistory
import com.wimpy.dao.entity.MtgCard
import org.springframework.stereotype.Repository

@Repository
interface MtgHistoryCrudDao : CrudRepository<MtgHistory, Long?> {
    fun findByMtgCard(mtgCard: MtgCard): List<MtgHistory>
}