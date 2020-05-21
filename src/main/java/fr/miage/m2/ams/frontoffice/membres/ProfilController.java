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
        Membre membre = new Membre(new Long(1),"Lajournade","Ludovic","Université","ludovic.lajournade@toulouse.miage.fr","mdp",3,"numLicence","01/01/2020","En retard de paiement");

        model.addAttribute("membre",membre);
        return "gestionProfil";
    }

    @PostMapping("/gestionProfil")
    public String postGestionProfil(@RequestParam String IBAN,@RequestParam int montant, @ModelAttribute Membre membre)
    {
        membre.setPrenom("Ludovic");
        membre.setNom("Lajournade");
        membre.setStatut("en règle");

        log.info("IBAN:"+IBAN+", montant:"+montant);
        restService.postJsonMembre("http://localhost:10000/payement/18-04-2020/"+IBAN,membre); // TODO: tester le post

        return "gestionProfil";
    }
}
