package fr.miage.m2.ams.frontoffice.authentification;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MyUserController {

    @GetMapping("/login")
    public String authForm(Model model)
    {
        model.addAttribute("login",new MyUser());
        return "login";
    }

    @PostMapping("/login")
    public String authSend(@ModelAttribute MyUser myUser)
    {
        return "home";
    }
}
