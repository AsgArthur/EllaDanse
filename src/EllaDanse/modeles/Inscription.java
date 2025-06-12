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

        if (cours != null) {
            this.saison = cours.getSaison();
        } else {
            this.saison = membre.getSaison(); // Utiliser la saison du membre si pas de cours
        }
    }

    public Membre getMembre() {
        return membre;
    }

    public String getSaison(){
        return saison;
    }

    public String getCours() {
        if (cours != null) {
            return cours.getNom();
        }
        return "Aucun cours";
    }

    public Cours getVraiCours(){
        return cours;
    }

    public String getProfesseur() {
        if (cours != null) {
            return cours.getProfesseur();
        }
        return "-";
    }

    public String getHoraire() {
        if (cours != null) {
            return cours.getHoraire();
        }
        return "-";
    }
}