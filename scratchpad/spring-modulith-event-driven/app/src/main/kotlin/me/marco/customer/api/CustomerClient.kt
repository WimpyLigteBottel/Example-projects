package me.marco.customer.api

import me.marco.customer.api.models.CreateCustomerRequest
import me.marco.customer.api.models.CustomerResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.DeleteExchange
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange("/api/customers", accept = ["application/json"])
interface CustomerClient {
    @PostExchange
    fun createCustomer(
        @RequestBody request: CreateCustomerRequest,
    ): ResponseEntity<CustomerResponse>

    @GetExchange("/{id}")
    fun getCustomer(
        @PathVariable id: String,
    ): ResponseEntity<CustomerResponse>


    @DeleteExchange("/{id}")
    fun deleteCustomer(@PathVariable id: String): ResponseEntity<Any>
}