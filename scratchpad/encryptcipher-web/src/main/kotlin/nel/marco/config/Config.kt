package nel.marco.config

import nel.marco.service.EncryptService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.stereotype.Component
import java.io.File
import java.util.concurrent.TimeUnit

@Configuration
class Config {
    @Bean
    fun decryptedFile(encryptedFile: File) =
        File("./decrypted-v3.txt").also {
            if (!encryptedFile().exists() && !it.exists()) {
                it.createNewFile()
            }
        }

    @Bean
    fun encryptedFile() = File("./encrypted-v3.txt")

    @Bean
    fun encryptor(
        @Value("\${encrypt.password}")
        password: String,
        @Value("\${encrypt.salt}")
        salt: String,
    ): TextEncryptor = Encryptors.text(password, salt)
}

@Component
@EnableScheduling
class ScheduledTasks(
    private val encryptService: EncryptService,
) {
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    fun encrypt() {
        encryptService.encryptFile()
    }
}
