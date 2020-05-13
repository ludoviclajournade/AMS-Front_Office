package fr.miage.m2.ams.frontoffice.membres;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.m2.ams.frontoffice.consumingrest.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        String json = restService.getJson("http://localhost:10000/");
        log.info(json);

        model.addAttribute("gestionProfil",new Membre());
        return "gestionProfil";
    }

    @PostMapping("/gestionProfil")
    public String postGestionProfil(@ModelAttribute Membre membre)
    {
        return "gestionProfil";
    }
}
