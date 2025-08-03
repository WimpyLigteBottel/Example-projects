package nel.marco.api

import nel.marco.service.EncryptService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DecryptController(
    @Value("\${encrypt.password}")
    private val password: String,
    private val service: EncryptService,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/decrypt")
    fun decrypt(
        @RequestParam password: String,
    ) {
        if (password != this.password) {
            logger.error("Wrong password!")
            return
        }

        service.decryptFile()
    }

    @GetMapping("/encrypt")
    fun encrypt() {
        service.encryptFile()
    }
}
