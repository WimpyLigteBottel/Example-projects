package nel.marco

import com.google.inject.Guice
import nel.marco.configure.SingletonConfigure
import nel.marco.singletons.Child
import nel.marco.singletons.GrandParent
import nel.marco.singletons.Parent


fun main() {

    val createInjector = Guice.createInjector(SingletonConfigure())

    val grandParent = createInjector.getInstance(GrandParent::class.java)
    val parent = createInjector.getInstance(Parent::class.java)
    val child = createInjector.getInstance(Child::class.java)

    grandParent.children()
    println("XXXXXXXXXXXX")
    parent.children()
    println("XXXXXXXXXXXX")
    child.children()
}


