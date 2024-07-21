package nel.marco

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.withPackage
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test

class ArchitectureTest {

    //internal
    val service = Layer("service", "nel.marco.internal.service..")
    val dto = Layer("dto", "nel.marco.internal.dto..")
    val annotation = Layer("annotation", "nel.marco.internal.annotation..")


    // hidden
    val clients = Layer("integration", "nel.marco.hidden.clients..")
    val configuration = Layer("configuration", "nel.marco.hidden.configuration..")

    // can only depend on internal
    val example = Layer("example", "nel.marco.example..")


    @Test
    fun `example code can depend on domain`() {
        val classes = Konsist
            .scopeFromProject("shared-connection-library")
            .classes()

        val exampleClasses = classes
            .withPackage("nel.marco.example..")

        val hiddenClasses = classes
            .withPackage("nel.marco.hidden..")
            .map { it.fullyQualifiedName }

        exampleClasses.forEach { classDeclaration ->
            val containHiddenClasses = classDeclaration.containingFile.imports
                .map { it.name }
                .filter { import -> import in hiddenClasses }
                .toMutableList()

            hiddenClasses.forEach {
                if(classDeclaration.text.contains(it)){
                    containHiddenClasses.add(it)
                }
            }

            if (containHiddenClasses.isNotEmpty()) {
                fail<String>("${classDeclaration.name} contains invalid references [hiddenClasses=$containHiddenClasses]")
            }
        }

    }

    @Test
    fun `domain is allowed to depend on clients`() {
        Konsist
            .scopeFromProduction()
            .assertArchitecture {
                service.dependsOn(clients, dto, annotation)
            }
    }

    @Test
    fun `clients should not depend on anything`() {
        Konsist
            .scopeFromProduction()
            .assertArchitecture {
                configuration.dependsOn(clients)
                clients.dependsOnNothing()
            }
    }


}