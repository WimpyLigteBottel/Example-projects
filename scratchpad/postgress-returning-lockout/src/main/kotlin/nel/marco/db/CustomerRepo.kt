package nel.marco.db

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
open interface CustomerRepo : JpaRepository<Customer, Long> {

    @Query("select count(*) from Customer")
    fun findSizeOfCustomers(): Int

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Customer c")
    fun findAllForUpdate(): List<Customer>

    @Query(
        value = """
                    UPDATE customer SET name = :name
                    WHERE name is null
                    RETURNING *;
                """, nativeQuery = true
    )
    @Modifying
    fun updateAndReturnCustomers(name: String): List<Customer>


    @Query(
        """
        UPDATE customer
        SET name = :name
        RETURNING *;
        """,
        nativeQuery = true
    )
    @Modifying
    fun longUpdate(name: String): List<Customer>

}