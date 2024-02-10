package com.wimpy.db.filter

import java.math.BigDecimal
import java.time.LocalDateTime


data class MtgHistoryFilter(var name: String = "", var link: String= "", var isPricierThan: BigDecimal = BigDecimal.ZERO, var olderThan: LocalDateTime? = null) {

}