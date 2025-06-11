package EllaDanse.modeles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionnaireInscription {
    private List<Inscription> inscriptions = new ArrayList<>();

    public void ajouterInscription(Membre membre, Cours cours, String saison) {
        inscriptions.add(new Inscription(membre, cours, saison));
    }

    public List<Inscription> getInscriptions() {
        return inscriptions;
    }

    public List<Cours> getCoursDuMembre(Membre membre) {
        return inscriptions.stream()
                .filter(i -> i.getMembre().equals(membre))
                .map(Inscription::getCours)
                .collect(Collectors.toList());
    }
}