package nel.marco.exposed.customer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import nel.marco.exposed.customer.dto.Customer
import nel.marco.exposed.customer.dto.DetailedCustomer
import nel.marco.hidden.annotation.UsageMarker
import nel.marco.hidden.clients.customer.CustomerHttpClient
import nel.marco.hidden.clients.customer.DetailedCustomerHttpClient
import nel.marco.hidden.dto.ApplicationEnum


interface CustomerService {

    @UsageMarker(
        applications = [ApplicationEnum.ApplicationA, ApplicationEnum.ApplicationB, ApplicationEnum.ApplicationC, ApplicationEnum.ApplicationD],
        consumingServices = [CustomerHttpClient::class],
        businessProblemBeingSolved = [
            "See if the customer exist",
            "Find more details regarding customer"
        ]
    )
    suspend fun findCustomer(id: String): Customer

    @UsageMarker(
        applications = [ApplicationEnum.ApplicationD],
        consumingServices = [DetailedCustomerHttpClient::class],
        businessProblemBeingSolved = [
            "Find more details regarding customer"
        ]
    )
    suspend fun findDetailedCustomer(id: String): DetailedCustomer
}

class CustomerServiceImpl(
    private val customerHttpClient: CustomerHttpClient,
    private val detailedCustomerHttpClient: DetailedCustomerHttpClient,
) : CustomerService {
    override suspend fun findCustomer(id: String): Customer {
        return withContext(Dispatchers.IO) {

            //Step 1 fetch as much as possible in parallel
            val customerDeferred = async { customerHttpClient.getCustomer(id) }

            //Step 2 await all the results
            awaitAll(customerDeferred)

            //Step 3 convert to your specific domain
            customerDeferred.await().convert()
        }
    }

    override suspend fun findDetailedCustomer(id: String): DetailedCustomer {
        return withContext(Dispatchers.IO) {
            //Step 1 fetch as much as possible in parallel
            val detailedCustomerDeferred = async { detailedCustomerHttpClient.getCustomer(id) }

            //Step 2 await all the results
            awaitAll(detailedCustomerDeferred)

            //Step 3 convert to your specific domain
            detailedCustomerDeferred.await().convert()
        }
    }
}

// Once this becomes too big move to separate file
private fun nel.marco.hidden.clients.customer.Customer.convert() = Customer(id = this.id)
private fun nel.marco.hidden.clients.customer.DetailedCustomer.convert() = DetailedCustomer(id = this.id)
