package com.wimpy.dao.entity

import com.google.gson.Gson
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
class MtgHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(nullable = false)
    var name: String = ""

    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    var mtgCard: MtgCard? = null

    @Column(nullable = false)
    var link: String = ""

    @Column(nullable = false)
    var price: BigDecimal = BigDecimal.ZERO

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date = Date()

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date = Date()
    override fun toString(): String {
        return Gson().toJson(this)
    }
}