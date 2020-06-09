package fr.miage.m2.ams.frontoffice.cours;

import com.google.gson.*;
import fr.miage.m2.ams.frontoffice.consumingrest.RestService;
import fr.miage.m2.ams.frontoffice.membres.Membre;
import fr.miage.m2.ams.frontoffice.membres.MembreController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class CoursController {
    private static final Logger log = LoggerFactory.getLogger(CoursController.class);
    private Gson gson;
    private RestService restService;

    public CoursController() {
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

                return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")); }

        }).create();

        this.restService = new RestService();
    }

    @GetMapping("/creerCours")
    public String getCreerLesson(Model model)
    {
        // Get lieux
        String json = restService.getJson("http://localhost:10001/cours/getLieux");
        log.info(json);
        Lieu[] lieux = gson.fromJson(json, Lieu[].class);


        model.addAttribute("lieux",lieux);

        return "creerCours";
    }

    @PostMapping("/creerCours")
    public String postCreerCours(Model model,
                                 @RequestParam String nom, @RequestParam Integer niveauCible,
                                 @RequestParam Integer duree,
                                 @RequestParam String lieuId) throws ParseException {
        log.info(gson.toJson("nom:"+nom+", niveauCible:"+niveauCible+", duree:"+duree+", lieuId:"+lieuId));

        Cours cours = new Cours();
            cours.setNom(nom);
            cours.setNiveauCible(niveauCible);
            cours.setDuree(duree);
            cours.setIdLieu(lieuId);
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


        // Get Enseignants
        String jsonMembres = restService.getJson("http://localhost:10000/");
        Membre membres[] = gson.fromJson(jsonMembres, Membre[].class);
        ArrayList<Membre> enseignants = new ArrayList<>();
        for (Membre membre : membres) {
            if (membre.getEnseignant()) {
                enseignants.add(membre);
            }
        }

        // add in model
        model.addAttribute("cours",cours);
        model.addAttribute("enseignants",enseignants);

        return "plannifierCours";
    }

    @PostMapping("/plannifierCours")
    public String postPlannifierCours(Model model,
                                      @RequestParam("id") String idCours,
                                      @RequestParam("choixEnseignant") Long idEnseignant,
                                      @RequestParam("debut-seance") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
                                      @RequestParam("duree") int duree)
    {

        log.info("idCours:"+idCours+", idEnseignant:"+idEnseignant+", debut:"+debut+ ", duree:"+duree);



        // Créer une séance
        //LocalDateTime fin = debut.plusMinutes(new Long(duree));
        Seance seance = new Seance(debut,idEnseignant);
        restService.postJsonSeance("http://localhost:10001/cours/addSeance/"+idCours,seance);


        return "welcome";
    }
}
