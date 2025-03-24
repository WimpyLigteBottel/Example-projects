package com.wimpy.db.dao.entity

import com.google.gson.Gson
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
data class MtgHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    var mtgCard: MtgCard = MtgCard(),

    @Column(nullable = false)
    var link: String = "",

    @Column(nullable = false)
    var price: BigDecimal = BigDecimal.ZERO,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: OffsetDateTime = OffsetDateTime.now(),

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: OffsetDateTime = OffsetDateTime.now(),


    )