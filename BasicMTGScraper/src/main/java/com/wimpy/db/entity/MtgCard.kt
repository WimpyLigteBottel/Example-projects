package com.wimpy.db.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*

@Entity
@Table
class MtgCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(nullable = false)
    var name: String = ""

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date = Date()

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date = Date()

    fun setName(name: String): MtgCard {
        this.name = name
        return this
    }
}