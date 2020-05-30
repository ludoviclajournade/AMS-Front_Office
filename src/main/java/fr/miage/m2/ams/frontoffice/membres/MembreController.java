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

import java.util.ArrayList;

@Controller
public class MembreController {
    private static final Logger log = LoggerFactory.getLogger(MembreController.class);
    private Gson gson;
    private RestService restService;
    public MembreController()
    {
        GsonBuilder builder = new GsonBuilder();
        this.gson = builder.create();
        this.restService = new RestService();
    }

    @GetMapping("/inscriptionMembre")
    public String getMembreForm(Model model)
    {
        model.addAttribute("membreForm",new Membre());
        return "inscriptionMembre";
    }

    @PostMapping("/inscriptionMembre")
    public String postMembreForm(@ModelAttribute Membre membre)
    {
        log.info(gson.toJson(membre));
        restService.postJsonMembre("http://localhost:10000/",membre);
        restService.getJson("http://localhost:8080/user/add/"+membre.getMail()+"/"+membre.getMdp());

        return "login?membreCreer";
    }

    @GetMapping("/consulterMembres")
    public String getConsulterMembres(Model model)
    {
        String json = restService.getJson("http://localhost:10000/");
        log.info(json);

        Membre membres[] = gson.fromJson(json, Membre[].class);

        log.info(membres.toString());

        model.addAttribute("membres",membres);
        return "consulterMembres";
    }

    @PostMapping("/consulterMembres")
    public String postConsulterMembres(@RequestParam Integer id,@RequestParam String isEnseignant, Model model)
    {
        // Modifier le membre en enseignant
        log.info("id:"+id+", isEnseignant:"+isEnseignant);
        restService.postJsonMembre("http://localhost:10000/modifEnseignant/"+isEnseignant+"/"+id);

        // Récupérer la nouvelle liste des membres
        String json = restService.getJson("http://localhost:10000/");
        log.info(json);

        Membre membres[] = gson.fromJson(json, Membre[].class);

        log.info(membres.toString());

        model.addAttribute("membres",membres);


        return "consulterMembres";
    }

    @PostMapping("/changerStatutPayement")
    public String postValiderPaiement(@RequestParam Integer idMembrePaiement, @RequestParam String statutMembre, Model model)
    {
        // recup membre
        String json = restService.getJson("http://localhost:10000/"+idMembrePaiement);
        log.info(json);
        Membre membre = gson.fromJson(json, Membre.class);

        // Modifier le statut du membre
        membre.setStatut(statutMembre);
        restService.postJsonMembre("http://localhost:10000/"+idMembrePaiement,membre);

        // Récupérer la nouvelle liste des membres
        json = restService.getJson("http://localhost:10000/");
        log.info(json);

        Membre membres[] = gson.fromJson(json, Membre[].class);

        log.info(membres.toString());

        model.addAttribute("membres",membres);


        return "consulterMembres";
    }

    @PostMapping("/validerCertificatMedical")
    public String postValiderCertificatMedical(@RequestParam Integer idMembreCertifMedic, @RequestParam String dateCertifMedic, Model model)
    {
        // recup membre
        String json = restService.getJson("http://localhost:10000/"+idMembreCertifMedic);
        log.info(json);
        Membre membre = gson.fromJson(json, Membre.class);

        // Modifier le statut du membre
        membre.setDateCertif(dateCertifMedic);
        restService.postJsonMembre("http://localhost:10000/"+idMembreCertifMedic,membre);

        // Récupérer la nouvelle liste des membres
        json = restService.getJson("http://localhost:10000/");
        log.info(json);

        Membre membres[] = gson.fromJson(json, Membre[].class);

        log.info(membres.toString());

        model.addAttribute("membres",membres);


        return "consulterMembres";
    }

    @PostMapping("/numLicenceNationnal")
    public String postNumLicenceNationnal(@RequestParam Integer idMembreLicence, @RequestParam String numLicence, Model model)
    {
        // recup membre
        String json = restService.getJson("http://localhost:10000/"+idMembreLicence);
        log.info(json);
        Membre membre = gson.fromJson(json, Membre.class);

        log.info("idMembreLicence:"+idMembreLicence+", numLicence:"+numLicence);

        // Modifier le statut du membre
        membre.setNumLicence(numLicence);
        restService.postJsonMembre("http://localhost:10000/"+idMembreLicence,membre);

        // Récupérer la nouvelle liste des membres
        json = restService.getJson("http://localhost:10000/");
        log.info(json);

        Membre membres[] = gson.fromJson(json, Membre[].class);

        log.info(membres.toString());

        model.addAttribute("membres",membres);


        return "consulterMembres";
    }

    @PostMapping("/modifierNiveau")
    public String postModifierNiveau(@RequestParam Integer idMembreNiveau, @RequestParam Integer niveau, Model model)
    {
        // recup membre
        String json = restService.getJson("http://localhost:10000/"+idMembreNiveau);
        log.info(json);
        Membre membre = gson.fromJson(json, Membre.class);

        log.info("idMembreNiveau:"+idMembreNiveau+", niveau:"+niveau);

        // Modifier le statut du membre
        membre.setNiveau(niveau);
        restService.postJsonMembre("http://localhost:10000/"+idMembreNiveau,membre);

        // Récupérer la nouvelle liste des membres
        json = restService.getJson("http://localhost:10000/");
        log.info(json);

        Membre membres[] = gson.fromJson(json, Membre[].class);

        log.info(membres.toString());

        model.addAttribute("membres",membres);


        return "consulterMembres";
    }

}
