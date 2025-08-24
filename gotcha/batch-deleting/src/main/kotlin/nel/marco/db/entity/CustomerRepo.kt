package nel.marco.db.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
interface CustomerRepo : JpaRepository<Customer, Long> {

    @Query("select count(*) from Customer", nativeQuery = true)
    fun findSizeOfCustomers(): Int

    @Query("select new Customer(c.id, c.name, c.created, c.updated) from Customer c order by RANDOM() LIMIT 1")
    fun findRandomCustomer(): Customer

    /*
            explain delete from Customer
            where id in (
                SELECT id FROM Customer
                WHERE created < NOW()
                LIMIT 1000
            );

            explain delete from Customer
            where id in (
                SELECT id FROM Customer
                WHERE created < NOW()
                LIMIT 10000
            );

            explain delete from Customer
            where id in (
            SELECT id FROM Customer
            WHERE created < NOW()
            LIMIT 100000
            );

            explain DELETE FROM customer
            WHERE ctid IN (
            SELECT ctid
            FROM customer
            WHERE created < now()
            LIMIT 100000
            );
     */

    @Modifying
    @Query(
        """
            delete from Customer
            where id in (
                SELECT id FROM Customer
                WHERE created < :createdDate
                LIMIT :limit
            )
        """,
        nativeQuery = true
    )
    fun deleteInBatch(createdDate: OffsetDateTime, limit: Long): Int

}