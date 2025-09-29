package nel.marco

import nel.marco.db.Customer
import nel.marco.db.CustomerRepo
import nl.wykorijnsburger.kminrandom.minRandom
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

@SpringBootApplication
open class Launcher

fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}


@RestController
class CustomerController(
    private val customerRepo: CustomerRepo,
    private val customerService: CustomerService
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
    fun findAll(): List<Customer> {
        return customerRepo.findAll()
    }

    @GetMapping("/example")
    fun updateAndReturning(): List<Customer> {
        val customers = customerRepo.findAll()
        val updatedCustomers = customers.map { it.copy(name = OffsetDateTime.now().toString()) }

        customerRepo.saveAllAndFlush(updatedCustomers)

        return customerRepo.findAll()
    }

    @GetMapping("/example2")
    fun updateAndReturning2(): List<Customer> {
        return customerRepo.updateAndReturnCustomers(OffsetDateTime.now().toString())
    }


    @GetMapping("/delete")
    fun deletall() {
        customerRepo.deleteAll()
    }


    @GetMapping("/update")
    open fun longUpdate(): List<Customer> {
        return customerService.lockAndUpdateAll("changed")
    }

    @GetMapping("/count")
    fun getCount(): Long {
        return customerRepo.count()
    }
}


@Service
open class CustomerService(
    private val customerRepo: CustomerRepo
) {

    @Transactional
    open fun lockAndUpdateAll(name: String): List<Customer> {
        // 1. Acquire locks
        val locked = customerRepo.findAllForUpdate()

        Thread.sleep(5000)

        // 2. Update them while locks are held
        val updated = customerRepo.longUpdate(name)

        return updated
    }
}