package tacos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// This is a MVC controller.
@Controller
public class HomeController {

    // Mapped to "/" root.
    @GetMapping("/")
    public String home() {

        // return "home" view name.
        return "home";
    }
}
