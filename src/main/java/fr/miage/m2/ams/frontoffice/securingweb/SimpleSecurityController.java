package fr.miage.m2.ams.frontoffice.securingweb;

import fr.miage.m2.ams.frontoffice.membres.MembreController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("user")
public class SimpleSecurityController {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private static final Logger log = LoggerFactory.getLogger(SimpleSecurityController.class);

    @Autowired
    public SimpleSecurityController(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }

    @RequestMapping("exists/{username}")
    public boolean userExists(@PathVariable("username") String username ) {
        return inMemoryUserDetailsManager.userExists(username);
    }

    @RequestMapping("add/{username}/{password}")
    public String add(@PathVariable("username") String username, @PathVariable("password") String password) {
        log.info("Username:"+username+", password:"+password);
        inMemoryUserDetailsManager.createUser(User.withDefaultPasswordEncoder().username(username).password(password).roles("USER").build());
        return "added";
    }

    @RequestMapping("delete/{username}")
    public String delete(@PathVariable("username") String username) {
        log.info("Username:"+username);
        inMemoryUserDetailsManager.deleteUser(username);
        return "deleted";
    }
}
