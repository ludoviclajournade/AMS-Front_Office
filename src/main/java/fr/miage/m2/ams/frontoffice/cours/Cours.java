package fr.miage.m2.ams.frontoffice.cours;


import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;

public class Cours {

    public String id;

    @NotNull
    public String nom;

    @NotNull
    public int niveauCible;

    @NotNull
    public Lieu lieu;

    @NotNull
    //durée exprimée en minutes
    public int duree;

    public int nbPlacesOccupees;

    public int getCptIdSeance() {
        return cptIdSeance;
    }

    public void setCptIdSeance(int cptIdSeance) {
        this.cptIdSeance = cptIdSeance;
    }

    //liste des séances de cours
    public HashMap<Integer, Seance> listeSeances;

    //liste des membres participant au cours
    public ArrayList<Long> listeMembres;

    private int cptIdSeance;

    public Cours() {
        this.cptIdSeance = 0;
        this.lieu = null;
        this.nbPlacesOccupees = 0;
        this.listeMembres = new ArrayList<>();
        this.listeSeances = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Lieu getLieu() {
        return lieu;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public int getNbPlacesOccupees() {
        return nbPlacesOccupees;
    }

    public void setNbPlacesOccupees(int nbPlacesOccupees) {
        this.nbPlacesOccupees = nbPlacesOccupees;
    }

    public HashMap<Integer, Seance> getListeSeances() {
        return listeSeances;
    }

    public void setListeSeances(HashMap<Integer, Seance> listeSeances) {
        this.listeSeances = listeSeances;
    }

    public ArrayList<Long> getListeMembres() {
        return listeMembres;
    }

    public void setListeMembres(ArrayList<Long> listeMembres) {
        this.listeMembres = listeMembres;
    }

    public void addParticipant(Long idMembre) {
        this.listeMembres.add(idMembre);
    }

    public void addSeance(Seance seance) {
        this.listeSeances.put(cptIdSeance, seance);
        cptIdSeance += 1;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", niveauCible=" + niveauCible +
                ", lieu=" + lieu +
                ", duree=" + duree +
                ", nbPlacesOccupees=" + nbPlacesOccupees +
                ", listeSeances=" + listeSeances +
                ", listeMembres=" + listeMembres +
                ", cptIdSeance=" + cptIdSeance +
                '}';
    }
}