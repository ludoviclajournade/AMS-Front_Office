package fr.miage.m2.ams.frontoffice.cours;

public class CoursPlanning {
    public String cours;
    public String lieu;
    public String enseignant;
    public String idCours;

    public String getIdCours() {
        return idCours;
    }

    public void setIdCours(String idCours) {
        this.idCours = idCours;
    }

    public Integer getIdSeance() {
        return idSeance;
    }

    public void setIdSeance(Integer idSeance) {
        this.idSeance = idSeance;
    }

    public Integer idSeance;

    public CoursPlanning(String cours, String lieu, String enseignant, String idCours, Integer idSeance) {
        this.cours = cours;
        this.lieu = lieu;
        this.enseignant = enseignant;
        this.idCours = idCours;
        this.idSeance = idSeance;
    }

    public String getCours() {
        return cours;
    }

    public void setCours(String cours) {
        this.cours = cours;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(String enseignant) {
        this.enseignant = enseignant;
    }
}
