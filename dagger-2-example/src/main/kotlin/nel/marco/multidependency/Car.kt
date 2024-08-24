package nel.marco.multidependency

import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class Car @Inject constructor(
    private val engine: Engine,
    private val brand: Brand
) {

    fun doThing() {
        engine.wroom()
        brand.shine()
    }
}

class Engine {
    fun wroom() {
        println("Wroom wroom")
    }
}

class Brand {
    fun shine() {
        println("Shine")
    }
}