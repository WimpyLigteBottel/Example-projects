package nel.marco.exposed.customer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import nel.marco.hidden.clients.CustomerHttpClient
import nel.marco.exposed.customer.dto.Customer

class CustomerServiceImpl(
    private val customerHttpClient: CustomerHttpClient
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
}

// Once this becomes too big move to separate file
private fun nel.marco.hidden.clients.Customer.convert() =
    Customer(id = this.id)
