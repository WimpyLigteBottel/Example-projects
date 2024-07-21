package nel.marco

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.withPackage
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test

/**
 * This test can be copied to your application with konsist to make sure you are not using hidden packages
 */
class SimpleArchitectureTest {

    // TOOD: INSERT YOUR APPLICATION NAME AND package
    val yourBaseApplicationPackage = "nel.marco.example.a.."
    val yourApplicationName = "application"

    val application = Layer(yourApplicationName, yourBaseApplicationPackage)


    //internal
    val hiddenPackage = "nel.marco.hidden.."
    val exposed = Layer("service", "nel.marco.exposed..")

    val scopeFromProject = Konsist.scopeFromProject()

    @Test
    fun `application only allowed to depend on the exposed package`() {
        scopeFromProject.assertArchitecture {
            application.dependsOn(exposed)
        }
    }

    @Test
    fun `example is not allowed to depend on hidden package`() {
        val classes = scopeFromProject.classes()

        val applicationClasses = classes.withPackage(yourBaseApplicationPackage)
        val hiddenClasses = classes.withPackage(hiddenPackage).map { it.fullyQualifiedName }

        val rulesBroken = applicationClasses.map { classDeclaration ->
            val hiddenImportsUsed = classDeclaration.containingFile.imports
                .map { it.name }
                .filter { import -> import in hiddenClasses }

            val classReferences = hiddenClasses.mapNotNull {
                if (classDeclaration.text.contains(it)) it else null
            }

            classDeclaration to (classReferences + hiddenImportsUsed)
        }.filter { it.second.isNotEmpty() }

        if (rulesBroken.isNotEmpty()) {
            fail<String>("rules have been broken \n" + rulesBroken.joinToString("\n"))
        }
    }

}