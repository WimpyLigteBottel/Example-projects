package me.marco.customer.api

import me.marco.customer.api.models.CreateCustomerRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
)
class CustomerControllerTest {

    @Autowired
    lateinit var customerClient: CustomerClient

    @Test
    fun createCustomer() {
        val request = CreateCustomerRequest("max", 18, "email@email.com")
        val response = customerClient.createCustomer(request).body!!

        assertThat(response.age).isEqualTo(18)
        assertThat(response.name).isEqualTo("max")
        assertThat(response.email).isEqualTo("email@email.com")
    }

    @Test
    fun getCustomer() {
        val id = customerClient.createCustomer(CreateCustomerRequest("max", 18, "email@email.com")).body!!.id
        val response = customerClient.getCustomer(id).body!!

        assertThat(response.age).isEqualTo(18)
        assertThat(response.name).isEqualTo("max")
        assertThat(response.email).isEqualTo("email@email.com")
    }

    @Test
    fun deleteCustomer() {
        val id = customerClient.createCustomer(CreateCustomerRequest("max", 18, "email@email.com")).body!!.id
        val deleted = customerClient.deleteCustomer(id)

        assertThat(deleted.statusCode.is2xxSuccessful).isTrue()

    }

}