package org.example.api

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@RestController
class OrderController() {

    private val log = LoggerFactory.getLogger(this::class.java)

    private var orderCreationMap = ConcurrentHashMap<String, ActionAndState>()


    @GetMapping("/create")
    fun startProcess(@RequestParam id: String, @RequestParam name: String): ActionAndState {
        val actionAndState = ActionAndState(id = UUID.randomUUID().toString(), name = name, state = State.SUCCESS)


        val nextInt = Random().nextInt(0, 100)

        if (nextInt > 80) {
            actionAndState.state = State.FAILED

            return actionAndState
        }
        orderCreationMap.put(actionAndState.id!!, actionAndState)
        log.info("successfully create the order! [mainOrder=$id]")
        return actionAndState
    }


    @PostMapping("/rollback")
    fun handleOrder(@RequestBody actionAndState: ActionAndState): ActionAndState? {
        val remove = orderCreationMap.remove(actionAndState.id)

        log.info("rollback the order! [order=${actionAndState.id}]")

        return actionAndState.copy(state = State.FAILED)
    }
}


data class ActionAndState(var id: String? = null, var name: String, var state: State = State.PENDING)


enum class State {
    PENDING,
    SUCCESS,
    FAILED
}