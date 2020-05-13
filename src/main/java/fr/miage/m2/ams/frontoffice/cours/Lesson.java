package fr.miage.m2.ams.frontoffice.cours;

public class Lesson {
    private String nom;
    private int niveauCible;

    public Lesson(String nom, int niveauCible) {
        this.nom = nom;
        this.niveauCible = niveauCible;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNiveauCible() {
        return niveauCible;
    }

    public void setNiveauCible(int niveauCible) {
        this.niveauCible = niveauCible;
    }
}
