package EllaDanse.modeles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionnaireInscription {
    private List<Inscription> inscriptions = new ArrayList<>();

    public void ajouterInscription(Membre membre, Cours cours) {
        inscriptions.add(new Inscription(membre, cours));
    }

    public void supprimerInscription(Membre membre, Cours cours) {
        inscriptions.removeIf(i -> i.getMembre().equalsTo(membre) && i.getVraiCours().equals(cours));
    }


    public List<Inscription> getInscriptions() {
        return inscriptions;
    }
}