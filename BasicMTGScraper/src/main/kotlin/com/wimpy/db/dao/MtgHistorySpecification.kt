package com.wimpy.db.dao

import com.wimpy.db.dao.entity.MtgHistory
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import java.time.OffsetDateTime


class MtgHistorySpecification(
    var id: String = "",
    var link: String = "",
    var isPricierThan: BigDecimal = BigDecimal.ZERO,
    var olderThan: OffsetDateTime? = null,
) : Specification<MtgHistory> {


    override fun toPredicate(
        root: Root<MtgHistory>,
        query: CriteriaQuery<*>,
        cb: CriteriaBuilder
    ): Predicate? {

        val listPredicate = mutableListOf<Predicate>()

        if (id.isNotBlank()) {
            listPredicate.add(cb.equal(root.get<String>("id"), id))
        }

        if (isPricierThan != BigDecimal.ZERO) {
            listPredicate.add(cb.greaterThan(root.get("price"), isPricierThan))
        }

        if (link.isNotBlank()) {
            listPredicate.add(cb.equal(root.get<String>("link"), link))
        }

        if (olderThan != null) {
            listPredicate.add(cb.lessThanOrEqualTo(root.get("created"), olderThan))
        }



        return cb.and(*listPredicate.toTypedArray())

    }

}