package nel.marco

import nel.marco.db.Customer
import nel.marco.db.CustomerRepo
import nl.wykorijnsburger.kminrandom.minRandom
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

@SpringBootApplication
open class Launcher

fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}


@RestController
class CustomerController(
    private val customerRepo: CustomerRepo
) {


    @GetMapping("/generate")
    fun generate() {
        val customers = (0..100).map {
            minRandom<Customer>().copy(
                name = null
            )
        }
        customers.forEach {
            customerRepo.save(it)
        }
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

    @GetMapping("/count")
    fun getCount(): Long {
        return customerRepo.count()
    }
}