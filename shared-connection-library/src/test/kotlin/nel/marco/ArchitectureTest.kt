package nel.marco

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.withPackage
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test

class ArchitectureTest {

    val exposed = Layer("service", "nel.marco.exposed..")
    val hidden = Layer("integration", "nel.marco.hidden..")
    val example = Layer("example", "nel.marco.example..")

    val scopeFromProject = Konsist.scopeFromProject("shared-connection-library")

    @Test
    fun `example is not allowed to depend on hidden package`() {
        val classes = scopeFromProject.classes()

        val exampleClasses = classes.withPackage("nel.marco.example..")

        val hiddenClasses = classes.withPackage("nel.marco.hidden..").map { it.fullyQualifiedName }

        exampleClasses.forEach { classDeclaration ->
            val hiddenImportsUsed =
                classDeclaration.containingFile.imports.map { it.name }.filter { import -> import in hiddenClasses }
                    .toMutableList()

            val classReferences = hiddenClasses.mapNotNull {
                if (classDeclaration.text.contains(it)) it else null
            }

            val combinedClassNames = classReferences + hiddenImportsUsed

            if (combinedClassNames.isNotEmpty()) {
                fail<String>("${classDeclaration.name} contains invalid references [hiddenClasses=$combinedClassNames]")
            }
        }
    }

    @Test
    fun `examples cant depend on hidden`() {
        scopeFromProject.assertArchitecture {
            example.dependsOn(exposed)
        }
    }

    @Test
    fun `exposed is allowed to depend on hidden`() {
        scopeFromProject.assertArchitecture {
            exposed.dependsOn(hidden)
        }
    }

    @Test
    fun `hidden should not depend on anything`() {
        scopeFromProject.assertArchitecture {
            hidden.dependsOnNothing()
        }
    }
}