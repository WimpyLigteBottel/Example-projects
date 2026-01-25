package me.marco.manual

interface Subscriber {
    fun pull()
}

class ConsoleSubscriber(
    private val name: String,
    private val publisher: Publisher
) : Subscriber {

    init {
        publisher.subscribe(this)
    }

    val hasSeen = HashSet<String>()

    override fun pull() {
        Thread.startVirtualThread {
            while (true) {
                val messages = publisher.poll(this, 5)
                messages.forEach { message ->

                    if (message.message == null) {
                        Thread.sleep(1000)
                        continue
                    }
                    if (hasSeen.contains(message.message)) {
                        throw RuntimeException("I have seen this message : $message")
                    }
                    hasSeen.add(message.message)

                    publisher.ack(message, this)
                }
                println("[$name] received: ${messages.map { it.message }}")


                Thread.sleep(1)
            }
        }
    }
}