package fr.miage.m2.ams.frontoffice.securingweb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.m2.ams.frontoffice.consumingrest.RestService;
import fr.miage.m2.ams.frontoffice.membres.Membre;
import fr.miage.m2.ams.frontoffice.membres.MembreController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Properties;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(MembreController.class);
    private Gson gson;
    private RestService restService;

    public WebSecurityConfig() {
        GsonBuilder builder = new GsonBuilder();
        this.gson = builder.create();
        this.restService = new RestService();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/statistiques").access("hasRole('DIRECTEUR')")
                .antMatchers("/consulterMembres").access("hasRole('SECRETAIRE')")
                .antMatchers("/consulterMembres/{id}").access("hasRole('SECRETAIRE')")
                .antMatchers("/gestionProfil").access("hasRole('USER')")
                .antMatchers("/consultationCours").access("hasRole(('ENSEIGNANT'))")
                .antMatchers("/plannifierCours/{id}").access("hasRole(('ENSEIGNANT'))")
                .antMatchers("/creerCours").access("hasRole(('ENSEIGNANT'))")
                .and()
                .formLogin().loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .usernameParameter("username").passwordParameter("password")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsManager());
    }



    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {

        // Init default
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("user").password("password").roles("USER").build());
        manager.createUser(users.username("professeur").password("password").roles("ENSEIGNANT").build());
        manager.createUser(users.username("admin").password("password").roles("DIRECTEUR", "SECRETAIRE","ENSEIGNANT").build());
        manager.createUser(users.username("secretaire").password("password").roles("SECRETAIRE").build());
        manager.createUser(users.username("directeur").password("password").roles("DIRECTEUR").build());

        // Get membres
        String json = restService.getJson("http://localhost:10000/");
        log.info(json);
        Membre membres[] = gson.fromJson(json, Membre[].class);

        // Add membres as users
        for (Membre membre : membres)
        {
            if ( ! manager.userExists(membre.getMail())) {
                // Set role
                if (membre.getEnseignant() != null && membre.getEnseignant() == true) {
                    manager.createUser(users.username(membre.getMail()).password(membre.getMdp()).roles("USER","ENSEIGNANT").build());
                } else {
                    manager.createUser(users.username(membre.getMail()).password(membre.getMdp()).roles("USER").build());
                }
            }
        }


        return manager;
    }

}