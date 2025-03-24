package nel.marco.db.repo

import nel.marco.db.entity.Customer
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
open interface CustomerRepo : CrudRepository<Customer, Long> {
}