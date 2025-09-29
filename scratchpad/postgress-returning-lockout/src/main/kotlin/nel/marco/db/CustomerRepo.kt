package nel.marco.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
open interface CustomerRepo : JpaRepository<Customer, Long> {

    @Query("select count(*) from Customer", nativeQuery = true)
    fun findSizeOfCustomers(): Int

    @Query(
        value = """
                UPDATE customer SET name = :name
                WHERE name is null
                RETURNING *;
            """,
        nativeQuery = true
    )
    @Modifying
    fun updateAndReturnCustomers(name: String): List<Customer>

}