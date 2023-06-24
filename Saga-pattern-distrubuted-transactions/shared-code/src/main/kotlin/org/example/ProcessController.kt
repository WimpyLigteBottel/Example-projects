package org.example

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*
import java.util.concurrent.ConcurrentHashMap

open class ProcessController(
    private val notifyMainService: NotifyService,
    private val processName: String
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    private var internalMemory = ConcurrentHashMap<String, Action>()


    @PostMapping("/create")
    fun startProcess(@RequestBody action: Action) {
        var temp = action.copy(
            internalId = UUID.randomUUID().toString(),
            state = getRandomState()
        )
        internalMemory.put(action.globalId, temp)
        notifyMainService.notifyMainServer(temp)
        log.info("successfully create the $processName! [mainOrderId=${action.globalId};state=${temp.state}]")
    }


    @PostMapping("/rollback")
    fun handleOrder(@RequestBody action: Action) {
        internalMemory.remove(action.globalId)
        notifyMainService.notifyMainServer(action.copy(state = State.ROLLBACK))
        log.info("rollback the $processName! [mainOrderId=${action.globalId};internalId=${action.internalId}]")
    }


    open fun getRandomState(): State {
        Random().nextInt(0, 100)
            .let {
                if (it > 80) {
                    return State.FAILED
                }

                return State.SUCCESS
            }
    }
}