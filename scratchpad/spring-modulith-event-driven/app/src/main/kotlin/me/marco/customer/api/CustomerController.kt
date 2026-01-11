package me.marco.customer.api

import me.marco.customer.api.models.CreateCustomerRequest
import me.marco.customer.api.models.CustomerResponse
import me.marco.customer.api.models.toResponse
import me.marco.customer.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService,
) : CustomerClient {
    @PostMapping
    override fun createCustomer(
        @RequestBody request: CreateCustomerRequest,
    ): ResponseEntity<CustomerResponse> {

        try {
            val customer = customerService.createCustomer(request.internalize())
            return ResponseEntity.ok(customer.toResponse())
        } catch (_: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomerResponse.Problem("FAiled"))
        }
    }

    override fun getCustomer(id: String): ResponseEntity<CustomerResponse> {
        val customer = customerService.getCustomer(id)

        if (customer.version == 0L || customer.deleted)
            return ResponseEntity.notFound().build()

        return ResponseEntity.ok(customer.toResponse())
    }

    override fun exists(id: String): ResponseEntity<Boolean> {
        val customer = customerService.getCustomer(id)

        if (customer.version == 0L || customer.deleted || id == "")
            return ResponseEntity.ok(false)

        return ResponseEntity.ok(true)
    }

    override fun deleteCustomer(id: String): ResponseEntity<Any> {
        return try {
            customerService.deleteCustomer(id)
            ResponseEntity.accepted().build()
        } catch (_: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

}