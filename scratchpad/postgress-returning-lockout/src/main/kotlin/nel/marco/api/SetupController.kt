package nel.marco.api

import nel.marco.db.Customer
import nel.marco.db.CustomerRepo
import nl.wykorijnsburger.kminrandom.minRandom
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SetupController(
    private val customerRepo: CustomerRepo
) {

    @GetMapping("/generate")
    fun generate(@RequestParam(defaultValue = "100") amount: Int = 100) {
        val customers = (0..amount).map {
            minRandom<Customer>().copy(
                name = null
            )
        }
        customers.forEach {
            customerRepo.save(it)
        }
    }

    @GetMapping("/find")
    fun find(@RequestParam(defaultValue = "10") amount: Int = 10): List<Customer> {
        return customerRepo.findAllAndLockAndSkipLocked(amount)
    }
    @GetMapping("/findall")
    fun findAll(@RequestParam(defaultValue = "10000") amount: Int = 10000): List<Customer> {
        return customerRepo.findAllAndLock(amount)
    }

    @GetMapping("/delete")
    fun deletall() {
        customerRepo.deleteAll()
    }

    @GetMapping("/count")
    fun getCount(): Long {
        return customerRepo.count()
    }
}