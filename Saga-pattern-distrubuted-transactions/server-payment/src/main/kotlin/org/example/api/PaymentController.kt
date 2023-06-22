package org.example.api

import org.example.service.NotifyMainServerService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@RestController
class PaymentController(private val notifyMainServerService: NotifyMainServerService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    private var paymentCreationMap = ConcurrentHashMap<String, ActionAndState>()


    @GetMapping("/create")
    fun startProcess(@RequestParam id: String, @RequestParam name: String) {
        val actionAndState = ActionAndState(id = UUID.randomUUID().toString(), name = name, state = State.SUCCESS)


        val nextInt = Random().nextInt(0, 100)

        if (nextInt > 80) {
            actionAndState.state = State.FAILED
        } else {
            paymentCreationMap.put(actionAndState.id!!, actionAndState)
            log.info("successfully create the payment! [mainOrder=$id]")
        }

        notifyMainServerService.respond(id, actionAndState)
    }


    @PostMapping("/rollback")
    fun handleOrder(@RequestParam id: String, @RequestBody actionAndState: ActionAndState) {
        val remove = paymentCreationMap.remove(actionAndState.id)

        log.info("rollback the payment! [payment=${actionAndState.id}]")
        notifyMainServerService.respond(id, actionAndState.copy(state = State.FAILED))
    }
}


data class ActionAndState(var id: String? = null, var name: String, var state: State = State.PENDING)


enum class State {
    PENDING,
    SUCCESS,
    FAILED,
}