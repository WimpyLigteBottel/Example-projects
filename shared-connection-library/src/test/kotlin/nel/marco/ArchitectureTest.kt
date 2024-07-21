package nel.marco

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.withPackage
import nel.marco.hidden.dto.ApplicationEnum
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test

class ArchitectureTest {

    //internal
    val exposed = Layer("service", "nel.marco.exposed..")

    // hidden
    val hidden = Layer("integration", "nel.marco.hidden..")

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
        val applicationA = Layer("example.a", "nel.marco.example.a..")
        val applicationB = Layer("example.b", "nel.marco.example.b..")
        val applicationC = Layer("example.c", "nel.marco.example.c..")
        val applicationD = Layer("example.d", "nel.marco.example.d..")

        scopeFromProject.assertArchitecture {
            applicationA.dependsOn(exposed)
            applicationB.dependsOn(exposed)
            applicationC.dependsOn(exposed)
            applicationD.dependsOn(exposed)
        }

        assertThat(ApplicationEnum.entries.size)
            .withFailMessage("apply a rule for the new application")
            .isEqualTo(4)
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