package com.wimpy.db.dao

import org.springframework.data.repository.CrudRepository
import com.wimpy.db.entity.MtgHistory
import com.wimpy.db.entity.MtgCard
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface MtgHistoryCrudDao : CrudRepository<MtgHistory, Long>, JpaSpecificationExecutor<MtgHistory> {
    fun findByMtgCard(mtgCard: MtgCard): List<MtgHistory>
}