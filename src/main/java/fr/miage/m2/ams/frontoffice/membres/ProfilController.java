package fr.miage.m2.ams.frontoffice.membres;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.m2.ams.frontoffice.consumingrest.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Controller
public class ProfilController {
    private static final Logger log = LoggerFactory.getLogger(ProfilController.class);
    private Gson gson;
    private RestService restService;

    public ProfilController()
    {
        GsonBuilder builder = new GsonBuilder();
        this.gson = builder.create();
        this.restService = new RestService();
    }

    @GetMapping("/gestionProfil")
    public String getGestionProfil(Model model)
    {
        // get username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("username:"+username);

        // get user information
        String json = restService.getJson("http://localhost:10000/email/"+username);
        log.info(json);

        Membre membre = gson.fromJson(json, Membre.class);
        log.info(membre.toString());

        // check payement
        boolean payementEnvoye= (membre.getPayement() == null) ? true : false;

        model.addAttribute("membre",membre);
        model.addAttribute("payementEnvoye",payementEnvoye);
        return "gestionProfil";
    }

    @PostMapping("/gestionProfil")
    public String postGestionProfil(@RequestParam String IBAN, Model model)
    {
        String json = restService.getJson("http://localhost:10000/1");
        log.info(json);
        Membre membre = gson.fromJson(json, Membre.class);

        DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
        String formattedDate = formatter.format(LocalDate.now());

        log.info("IBAN:"+IBAN+", formattedDate:"+formattedDate);

        restService.postJsonMembre("http://localhost:10000/payement/"+formattedDate+"/"+IBAN+"/"+membre.getId()); // TODO: tester le post

        membre.setPayement(formattedDate);
        model.addAttribute("membre",membre);
        return "gestionProfil";
    }

    @PostMapping("/modifierMembre")
    public String postModifierMembre(@ModelAttribute Membre membre)
    {
        log.info("postModifierMembre: " + gson.toJson(membre));
        restService.postJsonMembre("http://localhost:10000/"+membre.getId(),membre);

        return "gestionProfil";
    }
}
