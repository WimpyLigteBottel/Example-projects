package com.example.marcodemodesign.db.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table
data class Review(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @Column(name = "retailer_id")
    var retailerId: String = "",

    @OneToMany(fetch = FetchType.LAZY)
    var ratings: MutableList<Rating>? = mutableListOf(),

    @CreationTimestamp
    var created: OffsetDateTime = OffsetDateTime.now(),

    @UpdateTimestamp
    var updated: OffsetDateTime = OffsetDateTime.now(),
    ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Review

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , retailerId = $retailerId , created = $created , updated = $updated )"
    }
}