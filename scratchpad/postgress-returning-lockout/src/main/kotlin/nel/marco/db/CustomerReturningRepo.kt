package nel.marco.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
open interface CustomerReturningRepo : JpaRepository<Customer, Long> {

    @Query(
        value = """
                    UPDATE customer SET name = :name
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