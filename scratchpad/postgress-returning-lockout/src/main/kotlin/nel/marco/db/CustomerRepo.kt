package nel.marco.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
open interface CustomerRepo : JpaRepository<Customer, Long> {

    @Query("select count(*) from Customer")
    fun findSizeOfCustomers(): Int

    @Query(
        value = "SELECT * FROM customer ORDER BY id LIMIT :limit FOR UPDATE SKIP LOCKED",
        nativeQuery = true
    )
    fun findAllForUpdate(limit: Int): List<Customer>

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