package fr.miage.m2.ams.frontoffice.cours;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.m2.ams.frontoffice.consumingrest.RestService;
import fr.miage.m2.ams.frontoffice.membres.Membre;
import fr.miage.m2.ams.frontoffice.membres.MembreController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class CoursController {
    private static final Logger log = LoggerFactory.getLogger(CoursController.class);
    private Gson gson;
    private RestService restService;

    public CoursController() {
        GsonBuilder builder = new GsonBuilder();
        this.gson = new GsonBuilder()
                .setDateFormat("dd-MM-yyyy hh:mm").create();
        this.restService = new RestService();
    }

    @GetMapping("/creerCours")
    public String getCreerLesson(Model model)
    {
        return "creerCours";
    }

    @PostMapping("/creerCours")
    public String postCreerCours(@RequestParam String nom, @RequestParam Integer niveauCible,
                                 @RequestParam Integer duree, @RequestParam String jourPremierCours) throws ParseException {
        log.info(gson.toJson("nom:"+nom+", niveauCible:"+niveauCible+", duree:"+duree+", jourPremierCours:"+jourPremierCours));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date jourPremierCoursDate = dateFormat.parse(jourPremierCours);

        Cours cours = new Cours();
            cours.setNom(nom);
            cours.setNiveauCible(niveauCible);
            cours.setDuree(duree);
            cours.setJourPremierCours(jourPremierCoursDate);

        restService.postJsonCours("http://localhost:10001/cours/create",cours);

        return "creerCours";
    }

    @GetMapping("/planificationCours")
    public String getPlanificationCours(Model model)
    {
        String json = restService.getJson("http://localhost:10001/cours/getAllCours");
        log.info(json);

        Cours[] cours = gson.fromJson(json, Cours[].class);

        log.info(cours.toString());

        model.addAttribute("listeCours",cours);

        return "planificationCours";
    }
}
