package com.wimpy.db.filter

import com.wimpy.db.dao.MtgHistory
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*


data class MtgHistoryFilter(
    var name: String = "",
    var link: String = "",
    var isPricierThan: BigDecimal = BigDecimal.ZERO,
    var olderThan: LocalDateTime? = null
)


class MtgHistorySpecification(private val mtgHistoryFilter: MtgHistoryFilter) : Specification<MtgHistory> {


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