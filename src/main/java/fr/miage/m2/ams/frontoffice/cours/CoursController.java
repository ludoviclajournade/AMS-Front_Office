package fr.miage.m2.ams.frontoffice.cours;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.m2.ams.frontoffice.consumingrest.RestService;
import fr.miage.m2.ams.frontoffice.membres.Membre;
import fr.miage.m2.ams.frontoffice.membres.MembreController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Controller
public class CoursController {
    private static final Logger log = LoggerFactory.getLogger(CoursController.class);
    private Gson gson;
    private RestService restService;

    public CoursController() {
        GsonBuilder builder = new GsonBuilder();
        this.gson = new GsonBuilder()
                .setDateFormat("dd-MM-yyyy").create();
        this.restService = new RestService();
    }

    @GetMapping("/creerCours")
    public String getCreerLesson(Model model)
    {
        return "creerCours";
    }

    @PostMapping("/creerCours")
    public String postCreerCours(Model model,
                                 @RequestParam String nom, @RequestParam Integer niveauCible,
                                 @RequestParam Integer duree) throws ParseException {
        log.info(gson.toJson("nom:"+nom+", niveauCible:"+niveauCible+", duree:"+duree));

        Cours cours = new Cours();
            cours.setNom(nom);
            cours.setNiveauCible(niveauCible);
            cours.setDuree(duree);

        restService.postJsonCours("http://localhost:10001/cours/create",cours);

        String json = restService.getJson("http://localhost:10001/cours/getAllCours");
        log.info(json);

        Cours[] listeCours = gson.fromJson(json, Cours[].class);

        log.info(listeCours.toString());

        model.addAttribute("listeCours",listeCours);

        return "consultationCours";
    }

    @GetMapping("/consultationCours")
    public String getConsultationCours(Model model)
    {
        String json = restService.getJson("http://localhost:10001/cours/getAllCours");
        log.info(json);

        Cours[] cours = gson.fromJson(json, Cours[].class);

        log.info(cours.toString());

        model.addAttribute("listeCours",cours);

        return "consultationCours";
    }

    @RequestMapping("/plannifierCours/{id}")
    public String requestPlannifierCours(Model model,@PathVariable("id") String id)
    {

        // Get cours
        String json = restService.getJson("http://localhost:10001/cours/getCoursById/"+id);
        log.info(json);
        Cours cours = gson.fromJson(json, Cours.class);

        // add in model
        model.addAttribute("cours",cours);

        return "plannifierCours";
    }

    @PostMapping("/plannifierCours")
    public String postPlannifierCours(Model model, @RequestParam("id") String id,
                                      @RequestParam("debut-seance") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
                                      @RequestParam("fin-seance") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin)
    {

        log.info("id:"+id+", debut:"+debut+ ", fin:"+fin);

        // if debut before fin, count nb hours
        if (debut.isBefore(fin)) {
            long hours = debut.until( fin, ChronoUnit.HOURS );
            log.info("hours:"+hours);
            // Créer une séance
        }

        // Get cours
        String json = restService.getJson("http://localhost:10001/cours/getCoursById/"+id);
        log.info(json);
        Cours cours = gson.fromJson(json, Cours.class);

        // add in model
        model.addAttribute("cours",cours);

        return "plannifierCours";
    }
}
