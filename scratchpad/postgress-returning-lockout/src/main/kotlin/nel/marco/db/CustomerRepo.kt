package nel.marco.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
open interface CustomerRepo : JpaRepository<Customer, Long>, CustomerReturningRepo {

    @Query(
        value = "SELECT * FROM customer ORDER BY id LIMIT :limit FOR UPDATE SKIP LOCKED",
        nativeQuery = true
    )
    fun findAllAndLockAndSkipLocked(limit: Int): List<Customer>

    @Query(
        value = "SELECT * FROM customer ORDER BY id LIMIT :limit FOR UPDATE",
        nativeQuery = true
    )
    fun findAllAndLock(limit: Int): List<Customer>

}