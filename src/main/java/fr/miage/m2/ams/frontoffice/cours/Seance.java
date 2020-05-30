package fr.miage.m2.ams.frontoffice.cours;


import java.time.LocalTime;

public class Seance {

    LocalTime debutSeanceHeure;
    String jourSeance;
    String idEnseignant;

    public Seance(LocalTime debutSeanceHeure, String jourSeance, String idEnseignant) {
        this.jourSeance = jourSeance;
        this.debutSeanceHeure = debutSeanceHeure;
        this.idEnseignant = idEnseignant;
    }

    public LocalTime getDebutSeanceHeure() {
        return debutSeanceHeure;
    }

    public void setDebutSeanceHeure(LocalTime debutSeanceHeure) {
        this.debutSeanceHeure = debutSeanceHeure;
    }

    public String getJourSeance() {
        return jourSeance;
    }

    public void setJourSeance(String jourSeance) {
        this.jourSeance = jourSeance;
    }

    public String getIdEnseignant() {
        return idEnseignant;
    }

    public void setIdEnseignant(String idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    @Override
    public String toString() {
        return "Seance{" +
                "debutSeanceHeure=" + debutSeanceHeure +
                ", jourSeance='" + jourSeance + '\'' +
                ", idEnseignant='" + idEnseignant + '\'' +
                '}';
    }
}
