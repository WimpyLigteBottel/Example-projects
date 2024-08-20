package nel.marco.queuesystem.api

import kotlinx.coroutines.*
import nel.marco.queuesystem.service.QueueService
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch

@SpringBootTest
class QueueApiTest {

    @Autowired
    lateinit var queueApi: QueueApi

    @Autowired
    lateinit var queueService: QueueService

    @BeforeEach
    fun clearQueue() {
        queueService.clearQueue()
    }

    @Test
    fun `createNumber is added to queue`() {
        val actual = queueApi.createNumber()

        assertThat(actual).isEqualTo(queueApi.getQueueNumber(actual.id))
    }

    @Test
    fun `when process is called on empty queue expect null`() {
        val expectedQueueNumber = queueApi.createNumber()

        assertThat(queueApi.process()).isEqualTo(expectedQueueNumber) // process oldest
        assertThat(queueApi.process()).isEqualTo(null) // nothing
    }

    @Test
    fun `when processing expect the oldest to be processed`() {
        val numbers: Map<Int, QueueNumber> = (1..5).associateWith { queueApi.createNumber() }

        assertThat(queueApi.process()).isEqualTo(numbers[1])
        assertThat(queueApi.process()).isEqualTo(numbers[2])
        assertThat(queueApi.process()).isEqualTo(numbers[3])
        assertThat(queueApi.process()).isEqualTo(numbers[4])
        assertThat(queueApi.process()).isEqualTo(numbers[5])
    }

    @Test
    fun `once processed can't get old queueNumber`() {
        val queueNumber = queueApi.createNumber()

        assertThat(queueApi.getQueueNumber(queueNumber.id)).isEqualTo(queueNumber)
        queueApi.process()
        assertThat(queueApi.getQueueNumber(queueNumber.id)).isEqualTo(null)
    }

    @Test
    fun `queue will get all current queueNumbers back`() {
        val queueNumber1 = queueApi.createNumber()
        val queueNumber2 = queueApi.createNumber()
        val queueNumber3 = queueApi.createNumber()

        assertThat(queueApi.getQueue()).containsOnlyOnce(queueNumber1, queueNumber2, queueNumber3)
        queueApi.process()
        assertThat(queueApi.getQueue()).containsOnlyOnce(queueNumber2, queueNumber3)
        queueApi.process()
        assertThat(queueApi.getQueue()).containsOnlyOnce(queueNumber3)
    }

    @Test
    fun `able to create multiple numbers at the same time`(): Unit = runBlocking(Dispatchers.IO) {

        withContext(Dispatchers.IO) {
            val countDownLatch = CountDownLatch(1)

            val tasks = mutableListOf<Deferred<*>>()

            repeat(30) { number ->
                async {
                    countDownLatch.await()
                    queueApi.createNumber()
                }.also {
                    tasks.add(it)
                }
            }

            val readDeffered = async {
                countDownLatch.await()
                queueApi.getQueue()
            }

            // Commence the threads
            countDownLatch.countDown()

            tasks.awaitAll()
            awaitAll(readDeffered)
        }
    }
}