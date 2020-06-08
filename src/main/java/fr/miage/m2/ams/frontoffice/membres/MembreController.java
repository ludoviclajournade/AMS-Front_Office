package fr.miage.m2.ams.frontoffice.membres;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.m2.ams.frontoffice.consumingrest.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    public String postMembreForm(Model model, @ModelAttribute Membre membre)
    {
        Boolean exists = new Boolean(restService.getJson("http://localhost:8080/user/exists/"+membre.getMail()));
        if (! exists) {
            membre.setEnseignant(false);
            log.info(gson.toJson(membre));
            restService.postJsonMembre("http://localhost:10000/",membre);
            restService.getJson("http://localhost:8080/user/add/"+membre.getMail()+"/"+membre.getMdp()+"/false");
            return "login";
        } else {
         model.addAttribute("alreadyExist",true);
            return "inscriptionMembre";
        }
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

    @PostMapping("/consulterMembres/{id}")
    public String postModifierMembre(Model model,
                                     @PathVariable("id") String id,
                                     @RequestParam("numLicence") String numLicence,
                                     @RequestParam("niveau") Integer niveau,
                                     @RequestParam("dateCertif") String dateCertif,
                                     @RequestParam("statut") String statut,
                                     @RequestParam("enseignant") String enseignant) throws ParseException {

        String json = restService.getJson("http://localhost:10000/");
        log.info(json);

        Membre membres[] = gson.fromJson(json, Membre[].class);

        // Retrive membre and update him
        Membre membre = null;
        int i=0;
        while (membre == null && i < membres.length)
        {
            if (membres[i].getId().equals(new Long(id))) {
                membre=membres[i];
            }
            i+=1;
        }
        // update membre

        if (membre != null) {
            boolean isEnseignant = (enseignant.equals("1"))  ? true : false;
            // Only if enseignant statut changed
            if (membre.getEnseignant() == null || membre.getEnseignant() != isEnseignant) {
                log.info("Statut enseignant modifié : "+membre.getEnseignant()+ " to "+isEnseignant);
                restService.postJsonMembre("http://localhost:10000/modifEnseignant/"+enseignant+"/"+id);
                restService.getJson("http://localhost:8080/user/delete/"+membre.getMail());
                restService.getJson("http://localhost:8080/user/add/"+membre.getMail()+"/"+membre.getMdp()+"/"+isEnseignant);
            }

            // Modifie le membre
            membre.setNumLicence(numLicence);
            membre.setNiveau(niveau);
            if (dateCertif != null && dateCertif != "") {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date realDateCCertif = dateFormat.parse(dateCertif);
                membre.setDateCertif(realDateCCertif);
            }
            membre.setStatut(statut);
            membre.setEnseignant(isEnseignant);
            restService.postJsonMembre("http://localhost:10000/"+id,membre);
            log.info("Membre " + id + " modifié");
        }


        model.addAttribute("membres",membres);
        return "consulterMembres";
    }

}
