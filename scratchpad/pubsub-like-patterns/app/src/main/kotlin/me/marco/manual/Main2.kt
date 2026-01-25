package me.marco.manual

import java.time.Duration

//rename to work
fun main() {

    val publisher = SimplePublisher(SchedulerCleanup())

    publisher.publish("No one should see this")


    val sub1 = ConsoleSubscriber("Subscriber-1", publisher)
    val sub2 = ConsoleSubscriber("Subscriber-2", publisher)

    publisher.publish("Hello subscribers!")
    publisher.publish("Second message")

    sub1.pull()
    sub2.pull()

    publisher.unsubscribe(sub1)

    repeat(1000) {
        publisher.publish(it.toString())
        if (it == 10)
            publisher.subscribe(sub1)
    }

    Thread.sleep(Duration.ofSeconds(10))
}
