import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {
      "src/test/resources/bankfeatures/CreatingUsers.feature",
      "src/test/resources/bankfeatures/CreatingAccountForUsers.feature",
      "src/test/resources/bankfeatures/MakingPayments.feature"
    })
public class RunFeatures {}
