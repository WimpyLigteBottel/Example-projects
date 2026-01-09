package me.marco.customer.service.dto

import java.time.OffsetDateTime

// ============= DTOs =============
data class CreateCustomerDto(val name: String, val age: Int, val email: String)

data class Customer(
    val id: String,
    val name: String = "",
    val age: Int = 0,
    val email: String = "",
    val deleted: Boolean = false,
    val version: Long = 0,
    val created: OffsetDateTime = OffsetDateTime.now(),
    val lastUpdated: OffsetDateTime = OffsetDateTime.now(),
) {

    fun incrementVersion(): Customer =
        copy(
            version = version + 1,
            lastUpdated = OffsetDateTime.now(),
        )
}
