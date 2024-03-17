package nel.marco;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public String furtherInfo(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String surname) {

        return name + " " + surname;
    }

}
