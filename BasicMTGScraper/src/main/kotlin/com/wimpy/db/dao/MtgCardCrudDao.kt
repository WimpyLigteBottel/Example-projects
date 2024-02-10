package com.wimpy.db.dao

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Entity
@Table
class MtgCard(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var name: String = "",

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date = Date(),

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date = Date(),
)


@Repository
interface MtgCardCrudDao : CrudRepository<MtgCard, Long?> {
    fun findByName(name: String): Optional<MtgCard>
}