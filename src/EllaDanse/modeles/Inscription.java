package EllaDanse.modeles;

import java.util.List;
import java.util.stream.Collectors;

public class Inscription {
    private Membre membre;
    private Cours cours;
    private String saison;

    public Inscription(Membre membre, Cours cours, String saison){
        this.membre = membre;
        this.cours = cours;
        this.saison = saison;
    }

    public Membre getMembre() {
        return membre;
    }

    public Cours getCours() {
        return cours;
    }
}
