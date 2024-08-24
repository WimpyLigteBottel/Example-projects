package nel.marco

import nel.marco.multidependency.DaggerVehiclesComponent

fun main() {

    val create = DaggerVehiclesComponent.create()

    val buildCar = create.buildCar()

    buildCar.doThing()
}




