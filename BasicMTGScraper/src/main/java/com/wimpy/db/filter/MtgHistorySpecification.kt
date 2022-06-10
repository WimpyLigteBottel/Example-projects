package com.wimpy.db.filter

import com.wimpy.db.entity.MtgHistory
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import java.time.ZoneOffset
import java.util.*
import javax.persistence.criteria.*

class MtgHistorySpecification constructor(private val mtgHistoryFilter: MtgHistoryFilter) : Specification<MtgHistory> {


    override fun toPredicate(
        root: Root<MtgHistory>,
        query: CriteriaQuery<*>,
        cb: CriteriaBuilder
    ): Predicate? {

        var listPredicate = mutableListOf<Predicate>()

        if (mtgHistoryFilter.name.isNotBlank()) {
            listPredicate.add(cb.equal(root.get<String>("name"), mtgHistoryFilter.name))
        }

        if (mtgHistoryFilter.isPricierThan != BigDecimal.ZERO) {
            listPredicate.add(cb.greaterThan(root.get("price"), mtgHistoryFilter.isPricierThan))
        }

        if (mtgHistoryFilter.link.isNotBlank()) {
            listPredicate.add(cb.equal(root.get<String>("link"), mtgHistoryFilter.link))
        }

        if (mtgHistoryFilter.olderThan != null) {
            val olderThan = Date.from(mtgHistoryFilter.olderThan!!.toInstant(ZoneOffset.ofHours(2)))
            listPredicate.add(cb.lessThanOrEqualTo(root.get("created"), olderThan))
        }


        return cb.and(*listPredicate.toTypedArray())

    }

}