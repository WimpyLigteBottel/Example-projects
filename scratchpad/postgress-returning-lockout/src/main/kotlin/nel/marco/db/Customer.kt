package nel.marco.db

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime
import java.util.*


@Entity
@Table
data class Customer(
    @Id
    @GeneratedValue(generator = "uuid2")
    var id: UUID? = null,

    @Column
    var name: String? = null,

    @CreationTimestamp
    var created: OffsetDateTime = OffsetDateTime.now(),

    @UpdateTimestamp
    var updated: OffsetDateTime = OffsetDateTime.now(),
)