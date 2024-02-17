package com.wimpy.rest.v1.models

import java.math.BigDecimal
import java.time.OffsetDateTime


data class CardHistoryModel(val id: Long, val cardName: String, val price: BigDecimal, val timeCreated: OffsetDateTime)
