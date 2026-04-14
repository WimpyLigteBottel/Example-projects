package me.marco.spelexample

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/spel/advance")
class AdvanceUseCasesRestController(
    private val cachingService: CachingService
) {

    @Value($$"${spel.number}")
    var number: Long? = null

    @Value($$"#{${spel.number} * 3}")
    var numberWithMath: Long? = null

    @PostConstruct
    fun onStartup() {
        // NOtice the math on on context loa
        println("number = $number")
        println("numberWithMath = $numberWithMath")
    }

    @GetMapping("/generate-number")
    fun generateNumber(@RequestParam(defaultValue = "2") number: Long): Long {
        // http://localhost:8080/spel/advance/generate-number  (Expect no caching)
        // http://localhost:8080/spel/advance/generate-number?number=1000 (expect caching)
        return cachingService.generateNumber(number)
    }

}


@Service
class CachingService {


    @Cacheable("cache", key = "#ownNumber", condition = "#ownNumber > 100")
    fun generateNumber(ownNumber: Long): Long {
        println("ownNumber = $ownNumber")
        return ownNumber * ownNumber
    }

}