package fr.miage.m2.ams.frontoffice.cours;

import com.google.gson.*;
import fr.miage.m2.ams.frontoffice.consumingrest.RestService;
import fr.miage.m2.ams.frontoffice.membres.Membre;
import fr.miage.m2.ams.frontoffice.membres.MembreController;
import fr.miage.m2.ams.frontoffice.welcome.WelcomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

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
        restService.postJsonCours("http://localhost:10001/cours/create/"+lieuId,cours);

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

        Cours[] listCours = gson.fromJson(json, Cours[].class);

        log.info(listCours.toString());

        model.addAttribute("listeCours",listCours);
        setConsultationCours(model);

        return "consultationCours";
    }

    @GetMapping("/supprimerCours/{id}")
    public String postSupprimerCours(Model model,@PathVariable("id") String id)
    {
        String json = restService.getJson("http://localhost:10001/cours/delete/"+id);
        setConsultationCours(model);
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
        String jsonMembres = restService.getJson("http://localhost:10000/getMembres");
        Membre membres[] = gson.fromJson(jsonMembres, Membre[].class);
        ArrayList<Membre> enseignants = new ArrayList<>();
        for (Membre membre : membres) {
            if (membre.getEnseignant()) {
                enseignants.add(membre);
            }
        }

        // Date min start
        String minDate = LocalDateTime.now().plusDays(7).toString();

        boolean haveTeacher = (enseignants.isEmpty()) ? true : false;

        // add in model
        model.addAttribute("cours",cours);
        model.addAttribute("enseignants",enseignants);
        model.addAttribute("minDate",minDate);
        model.addAttribute("haveTeacher",haveTeacher);

        return "plannifierCours";
    }

    @PostMapping("/plannifierCours")
    public String postPlannifierCours(Model model,
                                      @RequestParam("id") String idCours,
                                      @RequestParam("choixEnseignant") Long idEnseignant,
                                      @RequestParam("debut-seance") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
                                      @RequestParam("duree") int duree,
                                      @RequestParam("repetition") int repetition)
    {

        log.info("idCours:"+idCours+", idEnseignant:"+idEnseignant+", debut:"+debut+ ", duree:"+duree);

        // Nombre de weekends où le cours est répété
        for (int i=0; i<repetition;i++) {
            // Créer une séance
            //LocalDateTime fin = debut.plusMinutes(new Long(duree));
            Seance seance = new Seance(debut.plusWeeks(i),idEnseignant);
            restService.postJsonSeance("http://localhost:10001/cours/addSeance/"+idCours,seance);
        }

        setConsultationCours(model);

        return "consultationCours";
    }

    @GetMapping("/inscriptionCours")
    public String getInscriptionCours(Model model)
    {
        setConsultationCours(model);

        return "inscriptionCours";
    }

    @GetMapping("/desinscriptionCours/{idCours}/{idMembre}")
    public String postDesinscriptionCours(Model model,@PathVariable("idCours") String idCours,@PathVariable("idMembre") Long idMembre)
    {
        log.info("url: http://localhost:10001/cours/desinscrireCours/"+idCours+"/"+idMembre);
        restService.postJsonCours("http://localhost:10001/cours/desinscrireCours/"+idCours+"/"+idMembre,new Cours());

        setConsultationCours(model);

        return "inscriptionCours";
    }

    @GetMapping("/inscriptionCours/{idCours}/{idMembre}")
    public String postInscriptionCours(Model model,@PathVariable("idCours") String idCours,@PathVariable("idMembre") Long idMembre)
    {
        log.info("url: http://localhost:10001/cours/inscrireCours/"+idCours+"/"+idMembre);
        restService.postJsonCours("http://localhost:10001/cours/inscrireCours/"+idCours+"/"+idMembre, new Cours());

        setConsultationCours(model);

        return "inscriptionCours";
    }

    @GetMapping("/planning/{nextWeek}")
    public String getPlanning(Model model, @PathVariable("nextWeek") int nextWeek)
    {
        setPlanningModel(model,nextWeek);

        return "planning";
    }

    @GetMapping("/planning/{idCours}/{idSeance}")
    public String getPlanningDeleteSeance(Model model, @PathVariable("idCours") String idCours, @PathVariable("idSeance") int idSeance)
    {
        restService.postJsonCours("http://localhost:10001/cours/deleteSeance/"+idCours+"/"+idSeance, new Cours());
        setPlanningModel(model,0);

        return "planning";
    }

    private void setPlanningModel(Model model,int nextWeek) {
        // Liste des jours
        String[] daysList = new String[7]; //{ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        // Get Today
        LocalDate now = LocalDate.now();
        // Add week diff
        if (nextWeek > 0) {
            now = now.plusWeeks(nextWeek);
        } else if (nextWeek < 0) {
            now = now.minusWeeks(nextWeek * -1);
        }
        // Get Monday
        TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
        LocalDate monday = now.with(fieldISO, 1);
        for (int i=0;i<7;i++) {
            LocalDate localDate= monday.plusDays(i);
            daysList[i] =  localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();
        }
        log.info("dayList: "+daysList.toString());


        // Get all cours
        String json = restService.getJson("http://localhost:10001/cours/getAllCours");
        log.info(json);
        Cours[] listCours = gson.fromJson(json, Cours[].class);
        log.info(listCours.toString());


        // Fill planning
        int[] minutesList = new int[25];
        HashMap<Integer,String> hoursList = new HashMap<Integer, String>();
        HashMap<String,CoursPlanning> listSeancesPlan = new HashMap<>();
        int i = 0;
        for (int min=480;min<1200;min+=30) {
            minutesList[i]=min;
            // Hours
            String hoursValue = (min%60 == 0) ? (min/60)+"h" : (min/60)+"h30";
            int nextMin=min+30;
            String nextHoursValue = (nextMin%60 == 0) ? (nextMin/60)+"h" : (nextMin/60)+"h30";
            hoursList.put(min,hoursValue+"-"+nextHoursValue);

            // Loop days and fill cours
            for (String day : daysList) {
                // Cherche une Seance qui corresspond à la date
                for (Cours cours : listCours) {

                    Iterator it = cours.getListeSeances().entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        Seance seance = (Seance) pair.getValue();
                        Integer key = (Integer) pair.getKey();

                        int seanceMinutes = seance.getDebutSeance().getMinute() + (seance.getDebutSeance().getHour()*60);
                        String seanceDay = seance.getDebutSeance().getYear() + "-" + seance.getDebutSeance().getMonthValue() + "-" + seance.getDebutSeance().getDayOfMonth(); // seance.getDebutSeance().getDayOfWeek().toString();

                        // log.info("seance : {seanceMinutes:"+seanceMinutes+", seanceDay:"+seanceDay+"}");
                        // log.info("{min:"+min+", day:"+day.toUpperCase()+"}");
                        if (seanceMinutes == min && seanceDay.equals(day.toUpperCase())) {
                            // get user information
                            String jsonMembre = restService.getJson("http://localhost:10000/GetOne/"+seance.getIdEnseignant());
                            Membre membre = gson.fromJson(jsonMembre, Membre.class);

                            for (int duree=0;duree<cours.getDuree();duree+=30) {
                                CoursPlanning coursPlanning = new CoursPlanning(cours.getNom(),cours.getLieu().getNom(),membre.getNom() + " " + membre.getPrenom(),cours.getId(),key);
                                listSeancesPlan.put(day+""+(min+duree),coursPlanning);
                            }
                        }
                    }
                }
            }

            i++;
        }
        log.info(minutesList.toString());
        log.info("listSeancesPlan: " + listSeancesPlan.toString());

        model.addAttribute("minutesList",minutesList);
        model.addAttribute("hoursList",hoursList);
        model.addAttribute("daysList",daysList);
        model.addAttribute("listSeancesPlan",listSeancesPlan);
        model.addAttribute("nextWeek",nextWeek);

        WelcomeController.setRoleModel(model);
    }

    private void setConsultationCours(Model model) {

        // Récupération des cours
        String jsonCours = restService.getJson("http://localhost:10001/cours/getAllCours");
        log.info(jsonCours);

        Cours[] listCours = gson.fromJson(jsonCours, Cours[].class);

        // Hash map to know if user is already on the cours
        HashMap<String,Boolean> mapSubscribe = new HashMap<>();

        log.info(listCours.toString());


        for (Cours cours : listCours) {


            // get username
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("username:"+username);
            // get user information
            String json = restService.getJson("http://localhost:10000/email/"+username);
            // If it's an member
            if (json != null && json != "") {
                log.info(json);
                Membre membre = gson.fromJson(json, Membre.class);
                log.info(membre.toString());
                // Fill subscribe map
                for (Long membreId : cours.getListeMembres()) {
                    if (membreId.equals(membre.getId())) {
                        mapSubscribe.put(cours.getId(),true);
                    }
                }
                model.addAttribute("membre",membre);
            }

            // if not in, then it's not
            if ( ! mapSubscribe.containsKey(cours.getId()))
                mapSubscribe.put(cours.getId(),false);
        }

        log.info("mapSubscribe:"+mapSubscribe.toString());



        model.addAttribute("listeCours",listCours);
        model.addAttribute("mapSubscribe",mapSubscribe);
    }
}
