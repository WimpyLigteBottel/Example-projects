import me.marco.Launcher
import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

class ApplicationTests {

    @Test
    fun writeDocumentationSnippets() {

        var modules = ApplicationModules.of(Launcher::class.java).verify();


         Documenter(modules)
            .writeModulesAsPlantUml()
            .writeIndividualModulesAsPlantUml();
    }
}
