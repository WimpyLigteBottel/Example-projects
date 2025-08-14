package nel.marco.db.entity

import kotlinx.coroutines.*
import nl.wykorijnsburger.kminrandom.minRandom
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.system.measureTimeMillis

@RestController
@EnableScheduling
open class CustomController(
    private val customerRepo: CustomerRepo,
    private val batchService: BatchService
) {

    @GetMapping("/v1/size")
    open fun size(): Int {
        return customerRepo.findSizeOfCustomers()
    }

    @GetMapping("/v1/delete")
    open fun delete(@RequestParam batches: Boolean = true): String {
        var deletedCount = 0
        val time = measureTimeMillis {
            deletedCount = batchService.batchDelete(batches)
        }

        println(
            """
            delete (batches=${batches}) took $time ms and deleted (deletedCount=$deletedCount)
        """.trimIndent()
        )
        return "$time ms"
    }


    @GetMapping("/v1/batch")
    open suspend fun createBatch(@RequestParam size: Int = 100000): Int = coroutineScope {
        val customers = (1..size)
            .map {
                minRandom<Customer>().copy(name = minRandom())
            }.chunked(10000)
        val time = measureTimeMillis {
            customers.map { chunkCustomers ->
                async(Dispatchers.IO) {
                    batchService.batchInsert(chunkCustomers)
                }
            }.awaitAll()
        }
        println("create took $time ms")

        customerRepo.findSizeOfCustomers()
    }


}