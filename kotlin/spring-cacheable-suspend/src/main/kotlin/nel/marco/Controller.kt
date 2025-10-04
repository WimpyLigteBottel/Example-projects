package nel.marco

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    val randomResponseService: RandomResponseService,
    val manualCacheService: ManualCacheService
) {

    @GetMapping("manual")
    suspend fun manual(@RequestParam(defaultValue = "0") number: String): String {
        return manualCacheService.manualCache(number)
    }

    @GetMapping("v1")
    suspend fun v1(@RequestParam(defaultValue = "0") number: String): String {
        return randomResponseService.V1(number)
    }

    @GetMapping("v2")
    suspend fun v2(@RequestParam(defaultValue = "0") number: String): String {
        return randomResponseService.V2(number)
    }

    @GetMapping("v3")
    suspend fun v3(@RequestParam(defaultValue = "0") number: String): String {
        return randomResponseService.V3(number)
    }
}