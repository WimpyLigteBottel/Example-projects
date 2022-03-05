package com.wimpy.rest.v1;

import com.wimpy.core.MtgHistoryManager;
import com.wimpy.rest.v1.model.CardHistoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/history")
public class HistoryEndpoint {


    private final MtgHistoryManager mtgHistoryManager;

    @Autowired
    public HistoryEndpoint(MtgHistoryManager mtgHistoryManager) {
        this.mtgHistoryManager = mtgHistoryManager;
    }


    @GetMapping("/card")
    private ResponseEntity<CardHistoryResponse> findHistory(@RequestParam String cardName) {

        Optional<CardHistoryResponse> cardHistoryResponse = mtgHistoryManager.retrieveHistory(cardName);


        if (cardHistoryResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(cardHistoryResponse.get());
    }
}
