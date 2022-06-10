package com.wimpy.rest.v1

import com.wimpy.core.MtgHistoryManager
import com.wimpy.rest.v1.model.CardHistoryResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/history")
class HistoryEndpoint @Autowired constructor(private val mtgHistoryManager: MtgHistoryManager) {
    @GetMapping("/card")
    private fun findHistory(@RequestParam(required = true) cardName: String): CardHistoryResponse {
        return mtgHistoryManager.retrieveHistory(cardName)
    }


    @GetMapping("/clearHistory")
    private fun clearHistory(): String {
        mtgHistoryManager.clearHistory()
        return "History cleared"
    }
}