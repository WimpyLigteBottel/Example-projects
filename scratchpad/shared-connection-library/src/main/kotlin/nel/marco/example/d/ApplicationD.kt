package nel.marco.example.d

import nel.marco.example.c.dto.Customer
import nel.marco.exposed.customer.CustomerService

class ApplicationDContract(
    private val customerService: CustomerService
) {

    suspend fun findCustomer(customerId: String): Customer {

        val customer = customerService.findDetailedCustomer(customerId)

        return Customer(
            customerId = customer.id
        )
    }

}