package nel.marco

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


fun main() {

    //Need to declare dependency directly
    val parentModule = module {
        // get() , retrieve from the context the specific dependency
        single { Child(RandomNameGenerator.getName().random(), get()) }
        single { Parent(RandomNameGenerator.getName().random(), get()) }
        single { GrandParent(RandomNameGenerator.getName().random()) }

        // If i dont want to specify everything i can also do this
        singleOf(::TalkingService)
    }


    // We need to start coin like this
    startKoin {
        loadKoinModules(parentModule)
    }

    //Launch main
    Main().main()

}

class Main : KoinComponent {

    private val talkingService: TalkingService by inject()

    fun main() {
        talkingService.introduce()
    }
}


class TalkingService(
    private val child: Child,
    private val parent: Parent,
    private val grandParent: GrandParent
) {

    fun introduce() {
        println("XXXXXXXXX")
        child.speak()
        println("XXXXXXXXX")
        parent.speak()
        println("XXXXXXXXX")
        grandParent.speak()
    }
}


data class Child(val name: String, val parent: Parent) {
    fun speak() {
        println("My name is $name and my parent ${parent.name} and my grand parent ${parent.grandParent.name}")
    }
}

data class Parent(val name: String, val grandParent: GrandParent) {
    fun speak() {
        println("My name is $name and my grandparent ${grandParent.name}")
    }
}

data class GrandParent(val name: String) {
    fun speak() {
        println("My name is $name")
    }
}

object RandomNameGenerator {
    fun getName(): List<String> {
        return listOf("Ben", "Jack", "Rose")
    }
}