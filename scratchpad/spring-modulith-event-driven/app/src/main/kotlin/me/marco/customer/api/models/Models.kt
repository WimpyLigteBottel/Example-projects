package me.marco.customer.api.models

import me.marco.customer.service.dto.CreateCustomerDto
import me.marco.customer.service.dto.Customer


data class CreateCustomerRequest(val name: String, val age: Int, val email: String) {
    fun internalize(): CreateCustomerDto {
        return CreateCustomerDto(
            name,
            age,
            email
        )
    }
}


fun Customer.toResponse() = CustomerResponse(
    id = id,
    name = name,
    age = age,
    email = email,
)

data class CustomerResponse(
    val id: String,
    val name: String,
    val age: Int,
    val email: String,
)