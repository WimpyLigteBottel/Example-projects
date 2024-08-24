package nel.marco.singletons

import jakarta.inject.Inject
import jakarta.inject.Named
import jakarta.inject.Singleton


@Singleton
data class GrandParent @Inject constructor(@Named("grandparentName") val name: String, val parent: Parent) {
    fun children() {
        println("${this::class.simpleName}: My name is ${name}, my child is ${parent.name}")

        parent.children()
    }
}


@Singleton
data class Parent @Inject constructor(@Named("parentName") val name: String, val child: Child) {

    fun children() {
        println("${this::class.simpleName}: My name is ${name}, my child is ${child.name}")

        child.children()
    }
}

@Singleton
data class Child @Inject constructor(@Named("childName") val name: String) {
    fun children() {
        println("${this::class.simpleName}: My name is ${name}, I have no children")
    }
}