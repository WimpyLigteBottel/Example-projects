package com.wimpy.db.dao

import com.google.gson.Gson
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.*


@Entity
data class MtgHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,

    @Column(nullable = false)
    var name: String = "",

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    var mtgCard: MtgCard? = null,

    @Column(nullable = false)
    var link: String = "",

    @Column(nullable = false)
    var price: BigDecimal = BigDecimal.ZERO,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date = Date(),

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date = Date(),


    ) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

@Repository
interface MtgHistoryCrudDao : CrudRepository<MtgHistory, Long>, JpaSpecificationExecutor<MtgHistory> {
    fun findByMtgCard(mtgCard: MtgCard): List<MtgHistory>
}