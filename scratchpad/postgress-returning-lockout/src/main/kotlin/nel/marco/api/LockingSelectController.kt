package nel.marco.api

import nel.marco.db.Customer
import nel.marco.db.CustomerRepo
import nel.marco.service.CustomerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LockingSelectController(
    private val customerRepo: CustomerRepo,
    private val customerService: CustomerService
) {

    @GetMapping("/lock-update")
    fun longUpdate(@RequestParam(defaultValue = "10") amount: Int = 10): List<Customer> {
        return customerService.lockAndUpdateAll(amount)
    }
}