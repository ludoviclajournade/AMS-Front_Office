package fr.miage.m2.ams.frontoffice.authentification;

public class MyUser {
    private String email;
    private String password;
    private String statut;

    public MyUser()
    {
        this("test@hotmail.fr","test");
    }
    public MyUser(String email, String password)
    {
        this.email=email;
        this.password=password;
        this.statut="En ligne";
    }

    public String getEmail() {
        return email;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
