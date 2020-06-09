package fr.miage.m2.ams.frontoffice.cours;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Seance {

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    public LocalDateTime debutSeance;

    public Seance() {
    }

    public Long idEnseignant;


    public Long getIdEnseignant() {
        return idEnseignant;
    }

    public void setIdEnseignant(Long idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    public LocalDateTime getDebutSeance() {
        return debutSeance;
    }

    public void setDebutSeance(LocalDateTime debutSeance) {
        this.debutSeance = debutSeance;
    }

    @Override
    public String toString() {
        return "Seance{" +
                "debutSeance=" + debutSeance +
                ", idEnseignant=" + idEnseignant +
                '}';
    }

    public Seance(LocalDateTime debutSeance, Long idEnseignant) {
        this.debutSeance = debutSeance;
        this.idEnseignant = idEnseignant;
    }
}
