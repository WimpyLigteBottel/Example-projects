package nel.marco;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

}


@RestController
class CustomRestController {

    @GetMapping("/hello")
    public String furtherInfo(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname
    ) {

        return name + " " + surname;
    }

    @PostMapping("/color")
    public String changeColor() {
        var colors = List.of("red", "blue", "green");

        return colors.get(ThreadLocalRandom.current().nextInt(0, colors.size() - 1));
    }

}
