package fr.miage.m2.ams.frontoffice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.m2.ams.frontoffice.consumingrest.RestService;
import fr.miage.m2.ams.frontoffice.cours.Cours;
import fr.miage.m2.ams.frontoffice.membres.MembreController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
public class FrontofficeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontofficeApplication.class, args);

		/** Créer 2 cours
		Logger log = LoggerFactory.getLogger(MembreController.class);
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		RestService restService  = new RestService();

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 10);
		// convert calendar to date
		Date currentDatePlusTen = c.getTime();

		Cours coursMath = new Cours("Mathematiques",3,new Long(1),currentDatePlusTen,10);
		Cours coursFr = new Cours("Français",3,new Long(1),currentDatePlusTen,10);

		try {
			restService.postJsonCours("http://localhost:10001/cours/create", coursMath);
			restService.postJsonCours("http://localhost:10001/cours/create", coursFr);
		} catch (ResourceAccessException e) {
			log.error(e.getMessage());
		} catch (HttpClientErrorException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		 **/
	}

}
