package com.wimpy.db.dao

import com.wimpy.db.dao.entity.MtgCard
import com.wimpy.db.dao.entity.MtgHistory
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository


@Repository
open interface MtgHistoryCrudDao : JpaRepository<MtgHistory, Long>, JpaSpecificationExecutor<MtgHistory> {
    fun findAllByMtgCard(
        mtgCard: MtgCard
    ): List<MtgHistory>
}