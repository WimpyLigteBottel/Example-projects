package nel.marco.password

import nel.marco.EncryptService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EncryptServiceTest {
    @Autowired
    private lateinit var encryptService: EncryptService

    @Test
    fun `expect encryption and description to work`() {
        val expected = "ABCDEF"
        val encryptedText = encryptService.encrypt(expected)
        val decryptedText = encryptService.decrypt(encryptedText)

        assertThat(decryptedText).isEqualTo(expected)
    }
}
