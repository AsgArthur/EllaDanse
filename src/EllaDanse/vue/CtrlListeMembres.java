package EllaDanse.vue;

import EllaDanse.controller.Main;
import EllaDanse.modeles.Donnees;
import EllaDanse.modeles.Membre;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.*;
import java.util.stream.Collectors;

public class CtrlListeMembres {

    @FXML private TableView<Membre> membresTable;
    @FXML private TableColumn<Membre, Integer> idCol;
    @FXML private TableColumn<Membre, String> nomCol;
    @FXML private TableColumn<Membre, String> prenomCol;
    @FXML private TableColumn<Membre, Integer> ageCol;
    @FXML private TableColumn<Membre, String> emailCol;
    @FXML private TableColumn<Membre, String> saisonCol;
    @FXML private TableColumn<Membre, String> coursCol;

    @FXML private Label titreLabel;
    @FXML private Label totalMembresLabel;
    @FXML private ToggleButton bureauToggle;
    @FXML private Button profilBtn;
    @FXML private Button supprimerBtn;
    @FXML private TextField rechercheField;
    @FXML private ComboBox<String> saisonComboBox;
    @FXML private ComboBox<String> triComboBox;

    private ObservableList<Membre> tousLesMembres;
    private FilteredList<Membre> membresFiltres;
    private SortedList<Membre> membresTries;

    @FXML
    public void modifierMembre() {
        Membre membreSelectionne = membresTable.getSelectionModel().getSelectedItem();
        if (membreSelectionne != null) {
            // TODO: Appeler Main.ouvrirModifierMembre(membreSelectionne);

            // Pour l'instant, affichons juste une alerte
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modification");
            alert.setHeaderText("Modification de membre");
            alert.setContentText("Modification de : " + membreSelectionne.getNom() + " " + membreSelectionne.getPrenom());
            alert.showAndWait();

            membresTable.refresh();
        }
    }

    @FXML
    public void ouvrirProfilMembre() {
        Membre membreSelectionne = membresTable.getSelectionModel().getSelectedItem();
        if (membreSelectionne != null) {
            // TODO: Appeler Main.ouvrirProfilMembre(membreSelectionne);

            // Pour l'instant, affichons juste une alerte
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Profil");
            alert.setHeaderText("Profil du membre");
            alert.setContentText("Profil de : " + membreSelectionne.getNom() + " " + membreSelectionne.getPrenom());
            alert.showAndWait();
        }
    }

    @FXML
    public void supprimerMembre() {
        Membre membreSelectionne = membresTable.getSelectionModel().getSelectedItem();
        if (membreSelectionne != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText("Supprimer le membre");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer " +
                    membreSelectionne.getNom() + " " + membreSelectionne.getPrenom() + " ?");

            Optional<ButtonType> resultat = confirmation.showAndWait();
            if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
                tousLesMembres.remove(membreSelectionne);
                Donnees.supprimerMembre(membreSelectionne);

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Suppression réussie");
                info.setHeaderText(null);
                info.setContentText("Le membre a été supprimé avec succès.");
                info.showAndWait();
            }
        }
    }

    @FXML
    public void toggleBureau() {
        if (bureauToggle.isSelected()) {
            titreLabel.setText("Liste des membres du bureau");
            membresFiltres.setPredicate(membre -> {
                if (!membre.isMembreBureau()) return false;
                return appliquerAutresFiltres(membre);
            });
        } else {
            titreLabel.setText("Liste des membres");
            membresFiltres.setPredicate(membre -> {
                if (membre.isMembreBureau()) return false;
                return appliquerAutresFiltres(membre);
            });
        }
        mettreAJourCompteur();
    }

    @FXML
    public void filtrerParSaison() {
        appliquerFiltres();
    }

    @FXML
    public void filtrerParRecherche() {
        appliquerFiltres();
    }

    @FXML
    public void changerTri() {
        String tri = triComboBox.getValue();
        if (tri == null) return;

        Comparator<Membre> comparator = null;

        switch (tri) {
            case "Alphabétique":
                comparator = Comparator.comparing(Membre::getNom)
                        .thenComparing(Membre::getPrenom);
                break;
            case "Saison et alphabétique":
                comparator = Comparator.comparing(Membre::getSaison)
                        .thenComparing(Membre::getNom)
                        .thenComparing(Membre::getPrenom);
                break;
            case "Saison, cours et alphabétique":
                comparator = Comparator.comparing(Membre::getSaison)
                        .thenComparing(Membre::getCours)
                        .thenComparing(Membre::getNom)
                        .thenComparing(Membre::getPrenom);
                break;
            case "Saison, alphabétique et cours":
                comparator = Comparator.comparing(Membre::getSaison)
                        .thenComparing(Membre::getNom)
                        .thenComparing(Membre::getPrenom)
                        .thenComparing(Membre::getCours);
                break;
        }

        membresTries.setComparator(comparator);
    }

    @FXML
    public void gererDoubleClic() {
        if (membresTable.getSelectionModel().getSelectedItem() != null) {
            ouvrirProfilMembre();
        }
    }

    public void initialize() {
        // Configuration des colonnes
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        saisonCol.setCellValueFactory(new PropertyValueFactory<>("saison"));
        coursCol.setCellValueFactory(new PropertyValueFactory<>("cours"));

        // Récupérer les données depuis Donnees
        tousLesMembres = Donnees.getLesMembres();

        // Configurer les listes filtrées et triées
        membresFiltres = new FilteredList<>(tousLesMembres, p -> true);
        membresTries = new SortedList<>(membresFiltres);
        membresTries.comparatorProperty().bind(membresTable.comparatorProperty());
        membresTable.setItems(membresTries);

        // Remplir les ComboBox
        // Options de tri
        triComboBox.getItems().clear();
        triComboBox.getItems().addAll(
                "Alphabétique",
                "Saison et alphabétique",
                "Saison, cours et alphabétique",
                "Saison, alphabétique et cours"
        );
        triComboBox.setValue("Alphabétique");

        // Saisons disponibles
        List<String> saisons = Donnees.getLesSaisons();
        saisonComboBox.getItems().clear();
        saisonComboBox.getItems().add("Toutes");
        saisonComboBox.getItems().addAll(saisons);
        saisonComboBox.setValue("Toutes");

        // Désactiver les boutons si aucune sélection
        profilBtn.disableProperty().bind(Bindings.isNull(membresTable.getSelectionModel().selectedItemProperty()));
        supprimerBtn.disableProperty().bind(Bindings.isNull(membresTable.getSelectionModel().selectedItemProperty()));

        // Appliquer les filtres initiaux
        appliquerFiltres();
    }

    private void initialiserDonnees() {
        // TODO: Récupérer les données depuis votre classe Donnees
        // Pour l'instant, données de test
        tousLesMembres.add(new Membre(1, "Dupont", "Marie", 25, "marie.dupont@email.com", "2024-2025", "Jazz - Intermédiaire", false));
        tousLesMembres.add(new Membre(2, "Martin", "Pierre", 30, "pierre.martin@email.com", "2024-2025", "Classique - Avancé", true));
        tousLesMembres.add(new Membre(3, "Bernard", "Sophie", 22, "sophie.bernard@email.com", "2023-2024", "Contemporain - Débutant", false));
        tousLesMembres.add(new Membre(4, "Durand", "Jean", 28, "jean.durand@email.com", "2024-2025", "Hip-Hop - Intermédiaire", true));
        tousLesMembres.add(new Membre(5, "Moreau", "Emma", 19, "emma.moreau@email.com", "2024-2025", "Jazz - Débutant", false));
        tousLesMembres.add(new Membre(6, "Petit", "Lucas", 35, "lucas.petit@email.com", "2023-2024", "Salsa - Avancé", false));
        tousLesMembres.add(new Membre(7, "Roux", "Chloé", 27, "chloe.roux@email.com", "2024-2025", "Classique - Intermédiaire", true));
    }

    private void appliquerFiltres() {
        membresFiltres.setPredicate(membre -> {
            // Filtre bureau
            boolean afficherBureau = bureauToggle.isSelected();
            if (afficherBureau && !membre.isMembreBureau()) {
                return false;
            } else if (!afficherBureau && membre.isMembreBureau()) {
                return false;
            }

            return appliquerAutresFiltres(membre);
        });

        mettreAJourCompteur();
    }

    private boolean appliquerAutresFiltres(Membre membre) {
        // Filtre saison
        String saisonSelectionnee = saisonComboBox.getValue();
        if (saisonSelectionnee != null && !saisonSelectionnee.equals("Toutes")
                && !membre.getSaison().equals(saisonSelectionnee)) {
            return false;
        }

        // Filtre recherche
        String recherche = rechercheField.getText();
        if (recherche == null || recherche.isEmpty()) {
            return true;
        }

        String rechercheLower = recherche.toLowerCase();
        return membre.getNom().toLowerCase().contains(rechercheLower)
                || membre.getPrenom().toLowerCase().contains(rechercheLower)
                || membre.getEmail().toLowerCase().contains(rechercheLower);
    }

    private void mettreAJourCompteur() {
        int total = membresFiltres.size();
        totalMembresLabel.setText("Total : " + total + " membre(s)");
    }

    public void ajouterMembre(Membre nouveauMembre) {
        tousLesMembres.add(nouveauMembre);
        appliquerFiltres();
    }

    public ObservableList<Membre> getTousLesMembres() {
        return tousLesMembres;
    }

    public void fermerPage(ActionEvent actionEvent) {
        Main.closeListeMembre();
    }
}