package me.marco.customer.api

import me.marco.customer.api.models.CreateCustomerRequest
import me.marco.customer.api.models.CustomerResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.AssertionErrors.fail

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
)
class CustomerControllerTest {

    @Autowired
    lateinit var customerClient: CustomerClient

    @Test
    fun createCustomer() {
        val request = CreateCustomerRequest("max", 18, "email@email.com")
        when (val response = customerClient.createCustomer(request).body) {
            is CustomerResponse.OK -> {
                assertThat(response.age).isEqualTo(18)
                assertThat(response.name).isEqualTo("max")
                assertThat(response.email).isEqualTo("email@email.com")
            }
            else -> {
                fail("Shoul have hanled it")
            }
        }
    }

    @Test
    fun getCustomer() {
        val customer = customerClient.createCustomer(
            CreateCustomerRequest(
                "max",
                18,
                "email@email.com"
            )
        ).body as? CustomerResponse.OK ?: return fail("Response invalid")
        val response =
            customerClient.getCustomer(customer.id).body!! as? CustomerResponse.OK ?: return fail("Response invalid")

        assertThat(response.age).isEqualTo(18)
        assertThat(response.name).isEqualTo("max")
        assertThat(response.email).isEqualTo("email@email.com")
    }

    @Test
    fun deleteCustomer() {
        val id = customerClient.createCustomer(CreateCustomerRequest("max", 18, "email@email.com")).body!! as? CustomerResponse.OK ?: return fail("Response invalid")
        val deleted = customerClient.deleteCustomer(id.id)

        assertThat(deleted.statusCode.is2xxSuccessful).isTrue()

    }

}