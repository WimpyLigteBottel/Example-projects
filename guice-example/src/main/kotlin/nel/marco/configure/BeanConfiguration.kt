package nel.marco.configure

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Scopes
import jakarta.inject.Named
import jakarta.inject.Singleton
import nel.marco.singletons.Child
import nel.marco.singletons.GrandParent
import nel.marco.singletons.Parent
import nel.marco.singletons.HelloWorld
import java.io.File
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class SingletonConfigure : AbstractModule() {

//    Not needed because i am working with concrete classes. Only needed for interfaces/abstract classes
//    override fun configure() {
//        bind(HelloWorld::class.java).`in`(Singleton::class.java)
//        bind(GrandParent::class.java).`in`(Singleton::class.java)
//        bind(Parent::class.java).`in`(Singleton::class.java)
//        bind(Child::class.java).`in`(Singleton::class.java)
//    }

    @Provides
    @Named("grandparentName")
    fun provideGrandparentName(): String {
        return "Grandparent"
    }

    @Provides
    @Named("parentName")
    fun provideParentName(): String {
        return "Parent"
    }

    @Provides
    @Named("childName")
    fun provideChildName(): String {
        return "Child"
    }
}