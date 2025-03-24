package nel.marco

import nel.marco.db.entity.Customer
import nel.marco.db.repo.CustomerRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomController {

    @Autowired
    lateinit var customerRepo: CustomerRepo

    @GetMapping
    fun helloWorld(): String {
        return "hello world!"
    }

    @GetMapping("/v1/find")
    fun findCustomer(): MutableList<Customer> {
        return customerRepo.findAll().toMutableList()
    }

}


