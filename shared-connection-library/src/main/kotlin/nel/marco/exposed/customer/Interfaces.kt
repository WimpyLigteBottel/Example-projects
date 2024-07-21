package nel.marco.exposed.customer

import nel.marco.hidden.clients.CustomerHttpClient
import nel.marco.hidden.annotation.UsageMarker
import nel.marco.hidden.dto.ApplicationEnum
import nel.marco.exposed.customer.dto.Customer

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
}

