package nel.marco.queuesystem.api

import nel.marco.queuesystem.service.QueueService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController


data class QueueNumber(
    val id: String,
    val number: Long
)

@RestController
class QueueApi(
    private val queueService: QueueService
) {

    @GetMapping("/queue")
    fun getQueue() = queueService.getQueue().map { it.convert() }

    @PostMapping("/queue")
    fun createNumber() = queueService.createNextNumber().convert()

    @GetMapping("/queue/{id}")
    fun getQueue(@PathVariable id: String) = queueService.getQueueNumber(id)?.convert()

    @PostMapping("/process")
    fun process() = queueService.processOldestInQueue()?.convert()
}