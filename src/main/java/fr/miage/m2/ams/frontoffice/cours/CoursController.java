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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String getCreateLesson(Model model)
    {
        model.addAttribute("cours",new Lesson("mon cours",3));
        return "creerCours";
    }

    @PostMapping("/creerCours")
    public String postCreateLesson(@ModelAttribute Lesson lesson)
    {
        RestService restService = new RestService();
        String res = restService.getPostsPlainJSON();
        log.info(res);

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
