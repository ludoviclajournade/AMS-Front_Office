package fr.miage.m2.ams.frontoffice.welcome;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.m2.ams.frontoffice.membres.MembreController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.Iterator;

@Controller
public class WelcomeController {
    private static final Logger log = LoggerFactory.getLogger(WelcomeController.class);
    private Gson gson;

    public WelcomeController()
    {
        GsonBuilder builder = new GsonBuilder();
        this.gson = builder.create();
    }

    @GetMapping("/")
    public String getWelcome(Model model)
    {
        // Init roles boolean
        boolean ROLE_ANONYMOUS=false;
        boolean ROLE_ADMIN=false;
        boolean ROLE_SECRETAIRE=false;
        boolean ROLE_USER=false;
        boolean ROLE_ENSEIGNANT=false;

        // Get ROLES of user
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        log.info(gson.toJson(authorities));

        // Set roles
        Iterator<SimpleGrantedAuthority> iterator = authorities.iterator();
        // while loop
        while (iterator.hasNext()) {
            switch (iterator.next().toString())
            {
                case "ROLE_ANONYMOUS":
                    ROLE_ANONYMOUS=true;
                    break;
                case "ROLE_ADMIN":
                    ROLE_ADMIN=true;
                    break;
                case "ROLE_SECRETAIRE":
                    ROLE_SECRETAIRE=true;
                    break;
                case "ROLE_USER":
                    ROLE_USER=true;
                    break;
                case "ROLE_ENSEIGNANT":
                    ROLE_ENSEIGNANT=true;
                    break;
            }
        }

        // Add roles into params
        model.addAttribute("ROLE_ANONYMOUS",ROLE_ANONYMOUS);
        model.addAttribute("ROLE_ADMIN",ROLE_ADMIN);
        model.addAttribute("ROLE_SECRETAIRE",ROLE_SECRETAIRE);
        model.addAttribute("ROLE_USER",ROLE_USER);
        model.addAttribute("ROLE_ENSEIGNANT",ROLE_ENSEIGNANT);

        return "welcome.html";
    }
}
