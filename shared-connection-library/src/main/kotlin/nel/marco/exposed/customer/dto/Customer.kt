package nel.marco.exposed.customer.dto

import java.time.OffsetDateTime

data class Customer(
    val id: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val dateOfBirth: OffsetDateTime? = null,
    val accountReference: String? = null
)
