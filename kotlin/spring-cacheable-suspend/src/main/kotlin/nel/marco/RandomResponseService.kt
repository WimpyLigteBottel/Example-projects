package nel.marco

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.random.Random

@Service
class RandomResponseService {

    // 1. What will happen on the first run?
    // A: return result and print?
    // B: will it throw runtime exception?

    // 2. Doing second call what will happen?
    // A: return result and print?
    // B: will it throw exception?
    // C: Something else?
    @Cacheable(value = [BASIC_CACHE_NAME])
    fun V1(input: String): String {
        println("randomResponse -> V1")
        return "$input -> ${Random.Default.nextInt(0, 100)}"
    }


    // 1. What will happen on the first run?
    // A: return result and print?
    // B: will it throw runtime exception?

    // 2. Doing second call what will happen?
    // A: return result and print?
    // B: will it throw exception?
    // C: Something else?
    @Cacheable(value = [BASIC_CACHE_NAME])
    suspend fun V2(input: String): String {
        println("randomResponse -> V2")
        return "$input -> ${Random.Default.nextInt(0, 100)}"
    }


    // 1. What will happen on the first run?
    // A: return result and print?
    // B: will it throw runtime exception?

    // 2. Doing second call what will happen?
    // A: return result and print?
    // B: will it throw runtime exception?
    // C: Something else?

    // 3. How to still cache this?
    @Cacheable(value = [BASIC_CACHE_NAME], key = "#input")
    suspend fun V3(input: String, randomTextThatDoesNotMatter: String = UUID.randomUUID().toString()): String {
        println("randomResponse -> V3 -> $randomTextThatDoesNotMatter")
        return "$input -> ${Random.Default.nextInt(0, 100)}"
    }


}