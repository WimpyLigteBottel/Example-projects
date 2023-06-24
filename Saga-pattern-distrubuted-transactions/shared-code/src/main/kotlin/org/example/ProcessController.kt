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

    private var internalMemory = ConcurrentHashMap<String, ActionAndState>()


    @PostMapping("/create")
    fun startProcess(@RequestBody actionAndState: ActionAndState) {
        var temp = actionAndState.copy(
            internalId = UUID.randomUUID().toString(),
            state = getRandomState()
        )
        internalMemory.put(actionAndState.globalId, temp)
        notifyMainService.notifyMainServer(temp)
        log.info("successfully create the $processName! [mainOrderId=${actionAndState.globalId};state=${temp.state}]")
    }


    @PostMapping("/rollback")
    fun handleOrder(@RequestBody actionAndState: ActionAndState) {
        internalMemory.remove(actionAndState.globalId)
        notifyMainService.notifyMainServer(actionAndState.copy(state = State.ROLLBACK))
        log.info("rollback the $processName! [mainOrderId=${actionAndState.globalId};internalId=${actionAndState.internalId}]")
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