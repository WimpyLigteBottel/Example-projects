package nel.marco.api

import nel.marco.service.CustomerService
import nel.marco.db.Customer
import nel.marco.db.CustomerRepo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ReturningCustomerController(
    private val customerRepo: CustomerRepo,
    private val customerService: CustomerService
) {

    /*
        1. Showing how to 'find' and update customer records and save again and return

        except for bad code and naming and dumb logic... what reason is this code not optimal?
     */
    @GetMapping("/example")
    fun updateAllCustomersNamesAndSaveIt(): List<Customer> {
        val customers = customerRepo.findAll()
        val updatedCustomers = customers.map { it.copy(name = "example") }

        customerRepo.saveAllAndFlush(updatedCustomers)

        return customerRepo.findAll()
    }

    @GetMapping("/update-returning")
    fun updateAndReturning2(): List<Customer> {
        val updateAndReturnCustomers = customerRepo.updateAndReturnCustomers("update-returning")
        return updateAndReturnCustomers
    }


}