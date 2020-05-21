package fr.miage.m2.ams.frontoffice.membres;

public class Membre {
    private Long id;

    private String nom;

    private String prenom;

    private String adresse;

    private String mail;

    private String mdp;

    private int niveau;

    private String numLicence;

    private String dateCertif;

    private String statut;

    public Membre()
    {
        this.id=null;
        this.statut="En retard de paiement";
        this.niveau=0;
        this.numLicence=null;
    }
    public Membre(String nom, String prenom, String adresse, String mail, String mdp, String dateCertif)
    {
        this(null,nom,prenom,adresse,mail,mdp,0,null,dateCertif,"En retard de paiement");
    }

    public Membre(Long id, String nom, String prenom, String adresse, String mail, String mdp, int niveau, String numLicence, String dateCertif, String statut) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.mail = mail;
        this.mdp = mdp;
        this.niveau = niveau;
        this.numLicence = numLicence;
        this.dateCertif = dateCertif;
        this.statut = statut;
    }


    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getMail() {
        return mail;
    }

    public String getMdp() {
        return mdp;
    }

    public int getNiveau() {
        return niveau;
    }

    public String getNumLicence() {
        return numLicence;
    }

    public String getDateCertif() {
        return dateCertif;
    }

    public String getStatut() {
        return statut;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public void setNumLicence(String numLicence) {
        this.numLicence = numLicence;
    }

    public void setDateCertif(String dateCertif) {
        this.dateCertif = dateCertif;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

}
