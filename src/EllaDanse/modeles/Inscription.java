package EllaDanse.modeles;

import java.util.List;
import java.util.stream.Collectors;

public class Inscription {
    private Membre membre;
    private Cours cours;
    private String saison;

    public Inscription(Membre membre, Cours cours){
        this.membre = membre;
        this.cours = cours;
        this.saison = cours.getSaison();
    }

    public Membre getMembre() {
        return membre;
    }

    public String getSaison(){
        return saison;
    }

    public String getCours() {
        return cours.getNom();
    }

    public Cours getVraiCours(){
        return cours;
    }

    public String getProfesseur() {
        return cours.getProfesseur();
    }

    public String getHoraire() {
        return cours.getHoraire();
    }
}
