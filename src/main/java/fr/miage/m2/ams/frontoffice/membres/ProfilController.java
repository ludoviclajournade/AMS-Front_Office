package fr.miage.m2.ams.frontoffice.membres;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.m2.ams.frontoffice.consumingrest.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class ProfilController {
    private static final Logger log = LoggerFactory.getLogger(MembreController.class);
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
        String json = restService.getJson("http://localhost:10000/1");
        log.info(json);

        Membre membre = gson.fromJson(json, Membre.class);

        log.info(membre.toString());

        model.addAttribute("membre",membre);
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
}
