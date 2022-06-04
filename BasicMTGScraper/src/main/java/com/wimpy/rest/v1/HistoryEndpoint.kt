package com.wimpy.rest.v1

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.beans.factory.annotation.Autowired
import com.wimpy.core.MtgHistoryManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.http.ResponseEntity
import com.wimpy.rest.v1.model.CardHistoryResponse

@RestController
@RequestMapping("/v1/history")
class HistoryEndpoint @Autowired constructor(private val mtgHistoryManager: MtgHistoryManager) {
    @GetMapping("/card")
    private fun findHistory(@RequestParam(required = true) cardName: String): CardHistoryResponse {
        return mtgHistoryManager.retrieveHistory(cardName)
    }
}