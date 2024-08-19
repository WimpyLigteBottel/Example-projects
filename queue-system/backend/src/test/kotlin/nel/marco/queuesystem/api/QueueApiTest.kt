package nel.marco.queuesystem.api

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class QueueApiTest {

    @Autowired
    lateinit var queueApi: QueueApi


    @Test
    fun `createNumber is added to queue`() {
        val actual = queueApi.createNumber()

        assertThat(actual).isEqualTo(queueApi.getQueue(actual.id))
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
    fun `once processed can't`() {
        val queueNumber = queueApi.createNumber()

        assertThat(queueApi.getQueue(queueNumber.id)).isEqualTo(queueNumber)
        queueApi.process()
        assertThat(queueApi.getQueue(queueNumber.id)).isEqualTo(null)
    }


}