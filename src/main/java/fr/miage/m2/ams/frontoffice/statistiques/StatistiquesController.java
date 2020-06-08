package fr.miage.m2.ams.frontoffice.statistiques;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.m2.ams.frontoffice.consumingrest.RestService;
import fr.miage.m2.ams.frontoffice.cours.Cours;
import fr.miage.m2.ams.frontoffice.membres.Membre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class StatistiquesController {
    private static final Logger log = LoggerFactory.getLogger(StatistiquesController.class);
    private Gson gson;
    private RestService restService;

    public StatistiquesController()
    {
        GsonBuilder builder = new GsonBuilder();
        this.gson = new GsonBuilder()
                .setDateFormat("dd-MM-yyyy").create();
        this.restService = new RestService();
    }

    @GetMapping("/statistiques")
    public String getStatistiques(Model model)
    {
        // init
        int nbMembres=0;
        int nbEnseignants=0;

        int nbCotisationsOK=0;
        int nbCotisationsWaiting=0;

        int nbCoursPositionned=0;

        // Get all members
        String jsonMembres = restService.getJson("http://localhost:10000/");
        log.info(jsonMembres);
        Membre membres[] = gson.fromJson(jsonMembres, Membre[].class);

        // Get all cours
        String jsonCours = restService.getJson("http://localhost:10001/cours/getAllCours");
        log.info(jsonCours);
        Cours coursList[] = gson.fromJson(jsonCours, Cours[].class);

        // Get niveau enseigants
        String jsonNvEnseigants = restService.getJson("http://localhost:10000/nvEnseignant");
        log.info(jsonNvEnseigants);
        Map nivEnseigantsMap = gson.fromJson(jsonNvEnseigants, Map.class);

        // Count nb enseigants and members and cotisations
        for (Membre membre : membres)
        {
            if (membre.getEnseignant() != null && membre.getEnseignant()) {
                nbEnseignants+=1;
            } else {
                nbMembres+=1;
            }

            if (membre.getStatut() != null && membre.getStatut().equals("En règlege")) {
                nbCotisationsOK+=1;
            } else if (membre.getStatut() != null && membre.getStatut().equals("En retard de paiement")) {
                nbCotisationsWaiting+=1;
            }
        }


        // Count nb Cours positionned
        for (Cours cours : coursList)
        {
            // If cours have seance
            if ( ! cours.getListeSeances().isEmpty()) {
                nbCoursPositionned+=1;
            }
        }

        model.addAttribute("nbMembres",nbMembres);
        model.addAttribute("nbEnseignants",nbEnseignants);
        model.addAttribute("nbCours",coursList.length);
        model.addAttribute("nbCoursPositionned",nbCoursPositionned);
        model.addAttribute("nbCotisationsOK",nbCotisationsOK);
        model.addAttribute("nbCotisationsWaiting",nbCotisationsWaiting);

        return "statistiques";
    }

}
