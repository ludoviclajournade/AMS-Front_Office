package fr.miage.m2.ams.frontoffice.home;

import fr.miage.m2.ams.frontoffice.authentification.MyUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String getHome(Model model)
    {
        model.addAttribute("myUser",new MyUser());
        return "home";
    }

    @PostMapping("/home")
    public String postHome(@ModelAttribute MyUser myUser)
    {
        return "home";
    }
}
