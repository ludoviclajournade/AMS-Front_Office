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
        return "inscriptionMembre";
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
    public String postConsulterMembres(@RequestParam Integer id)
    {
        log.info("id:"+id);
        // restService.postJsonMembre("http://localhost:10000/",membre);
        return "consulterMembres";
    }

}
