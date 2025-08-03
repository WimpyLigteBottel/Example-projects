package nel.marco.service

import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

@Service
class EncryptService(
    private val encryptor: TextEncryptor,
    private val encryptedFile: File,
    private val decryptedFile: File,
) {
    fun encryptFile() {
        if (!decryptedFile.exists()) {
            return
        }

        encryptedFile.createNewFile()
        Files
            .readAllLines(Path(decryptedFile.absolutePath))
            .map { encrypt(it) }
            .forEach {
                encryptedFile.appendText(it)
            }
        decryptedFile.delete()
    }

    fun decryptFile() {
        if (!encryptedFile.exists()) {
            return
        }

        decryptedFile.createNewFile()

        Files
            .readAllLines(Path(encryptedFile.absolutePath))
            .map { decrypt(it) }
            .forEach {
                decryptedFile.appendText(it)
            }

        encryptedFile.delete()
    }

    internal fun encrypt(text: String): String = encryptor.encrypt(text)

    internal fun decrypt(text: String): String = encryptor.decrypt(text)
}
