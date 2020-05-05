package fr.miage.m2.ams.frontoffice.welcome;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String getWelcome(Model model)
    {
        model.addAttribute("welcome",new Welcome());
        return "welcome";
    }

    @PostMapping("/")
    public String postWelcome(@ModelAttribute Welcome welcome)
    {
        return "welcome";
    }
}
