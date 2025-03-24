package nel.marco.example.c

import nel.marco.example.c.dto.Customer
import nel.marco.exposed.customer.CustomerService

class ApplicationCContract(
    private val customerService: CustomerService
) {

    suspend fun findCustomer(customerId: String): Customer {

        val customer = customerService.findCustomer(customerId)

        return Customer(
            customerId = customer.id
        )
    }
}