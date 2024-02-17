package com.wimpy.rest.v1

import com.wimpy.core.MtgHistoryManager
import com.wimpy.db.dao.entity.MtgHistory
import com.wimpy.rest.v1.models.CardHistoryModel
import org.springframework.web.bind.annotation.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@RestController
@RequestMapping("/v1/history")
open class HistoryEndpoint(
    private val mtgHistoryManager: MtgHistoryManager
) {

    @PostMapping
    fun addNewCard(@RequestParam link: String): List<CardHistoryModel> = mtgHistoryManager.retrieveLatestHistory(link)

    @GetMapping
    fun findHistory(@RequestParam(required = true) name: String): List<CardHistoryModel> {
        return mtgHistoryManager.retrieveHistoriesOfCard(name)
    }


    @GetMapping("/all")
    fun findHistories(): List<List<MtgHistory>> {
        return mtgHistoryManager.retrieveHistoriesOfAllCards()
    }

    @GetMapping("/load")
    fun loadWithLatestCards() {
        val lines = Files.readAllLines(Paths.get("src/main/resources/preload"))

        lines.forEach {
            mtgHistoryManager.saveCardAndHistoryToDb(it)
        }

    }

    @GetMapping("/clearHistory")
    suspend fun clearHistory(): String {
        mtgHistoryManager.clearHistory()
        return "History cleared"
    }
}