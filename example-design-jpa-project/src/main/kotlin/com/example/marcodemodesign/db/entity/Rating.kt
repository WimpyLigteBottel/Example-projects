package com.example.marcodemodesign.db.entity

import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*

@Entity
@Table
data class Rating(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0L,

    @Column
    var reviewId: Long = 0L,

    @Column
    var ratingName: String = "",

    @Column
    var ratingValue: Int = 0,

    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    var updated: Date? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Rating

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , ratingName = $ratingName , ratingValue = $ratingValue , created = $created , updated = $updated )"
    }
}