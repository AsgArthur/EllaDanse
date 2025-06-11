package EllaDanse.modeles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionnaireInscription {
    private List<Inscription> inscriptions = new ArrayList<>();

    public void ajouterInscription(Membre membre, Cours cours) {
        inscriptions.add(new Inscription(membre, cours));
    }

    public List<Inscription> getInscriptions() {
        return inscriptions;
    }
}