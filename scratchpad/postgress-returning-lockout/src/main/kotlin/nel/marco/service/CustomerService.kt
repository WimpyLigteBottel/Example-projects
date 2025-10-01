package nel.marco.service

import nel.marco.db.Customer
import nel.marco.db.CustomerRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class CustomerService(
    private val customerRepo: CustomerRepo
) {

    @Transactional
    open fun lockAndUpdateAll(amount: Int): List<Customer> {
        // 1. Acquire locks
        val locked = customerRepo.findAllAndLock(amount)

        Thread.sleep(5000)

        // 2. Update them while locks are held
        val updated = customerRepo.longUpdate("longUpdate")

        return updated
    }
}