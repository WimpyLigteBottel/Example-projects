package me.marco.customer.api.models

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import me.marco.customer.service.dto.CreateCustomerDto
import me.marco.customer.service.dto.Customer
import org.springframework.modulith.NamedInterface


data class CreateCustomerRequest(val name: String, val age: Int, val email: String) {
    fun internalize(): CreateCustomerDto {
        return CreateCustomerDto(
            name,
            age,
            email
        )
    }
}


fun Customer.toResponse() = CustomerResponse.OK(
    id = id,
    name = name,
    age = age,
    email = email,
)

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = CustomerResponse.OK::class, name = "OK"),
    JsonSubTypes.Type(value = CustomerResponse.Problem::class, name = "PROBLEM")
)
@NamedInterface("api")
sealed class CustomerResponse(
    type: String
) {

    data class OK(
        val id: String,
        val name: String,
        val age: Int,
        val email: String,
    ) : CustomerResponse("OK")

    data class Problem(
        val message: String
    ) : CustomerResponse("PROBLEM")
}