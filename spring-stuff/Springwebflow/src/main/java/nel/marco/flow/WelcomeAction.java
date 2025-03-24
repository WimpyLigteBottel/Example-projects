package nel.marco.flow;

import org.springframework.stereotype.Component;

@Component
public class WelcomeAction {

  public String doSomethingAwesome(String name) {
    System.out.println("I know this person  called ->" + name);
    return String.format("name=%s", name.toUpperCase());
  }
}
