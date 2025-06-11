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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
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
            try {
                // Ouvrir la fenêtre de modification
                ouvrirFenetreProfil(membreSelectionne);

                // Rafraîchir la table après modification
                membresTable.refresh();
                appliquerFiltres();

            } catch (IOException e) {
                afficherErreur("Erreur", "Impossible d'ouvrir la fenêtre de modification : " + e.getMessage());
            }
        }
    }

    @FXML
    public void ouvrirProfilMembre() {
        Membre membreSelectionne = membresTable.getSelectionModel().getSelectedItem();
        if (membreSelectionne != null) {
            try {
                // Ouvrir la fenêtre de profil
                ouvrirFenetreProfil(membreSelectionne);

                // Rafraîchir la table au retour
                membresTable.refresh();
                appliquerFiltres();

            } catch (IOException e) {
                afficherErreur("Erreur", "Impossible d'ouvrir la fenêtre de profil : " + e.getMessage());
            }
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
                    membreSelectionne.getNom() + " " + membreSelectionne.getPrenom() + " ?\n\n" +
                    "Cette action ne peut pas être annulée.");

            Optional<ButtonType> resultat = confirmation.showAndWait();
            if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
                // Supprimer de la liste locale ET de la classe Donnees
                tousLesMembres.remove(membreSelectionne);
                boolean supprime = Donnees.supprimerMembre(membreSelectionne);

                if (supprime) {
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Suppression réussie");
                    info.setHeaderText(null);
                    info.setContentText("Le membre " + membreSelectionne.getPrenom() + " " +
                            membreSelectionne.getNom() + " a été supprimé avec succès.");
                    info.showAndWait();

                    // Mettre à jour le compteur
                    mettreAJourCompteur();
                } else {
                    afficherErreur("Erreur", "Impossible de supprimer le membre de la base de données.");
                }
            }
        }
    }

    @FXML
    public void toggleBureau() {
        if (bureauToggle.isSelected()) {
            titreLabel.setText("Liste des membres du bureau (" + Donnees.getNombreMembresBureau() + ")");
            bureauToggle.setText("Voir membres normaux");
            membresFiltres.setPredicate(membre -> {
                if (!membre.isMembreBureau()) return false;
                return appliquerAutresFiltres(membre);
            });
        } else {
            int nombresNormaux = Donnees.getNombreTotalMembres() - Donnees.getNombreMembresBureau();
            titreLabel.setText("Liste des membres normaux (" + nombresNormaux + ")");
            bureauToggle.setText("Voir membres bureau");
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

        // Style pour différencier les membres du bureau
        membresTable.setRowFactory(tv -> {
            TableRow<Membre> row = new TableRow<>();
            row.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && newValue.isMembreBureau()) {
                    row.setStyle("-fx-background-color: #e3f2fd; -fx-font-weight: bold;");
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });


        tousLesMembres = Donnees.getLesMembres();

        // Configurer les listes filtrées et triées
        membresFiltres = new FilteredList<>(tousLesMembres, p -> true);
        membresTries = new SortedList<>(membresFiltres);
        membresTries.comparatorProperty().bind(membresTable.comparatorProperty());
        membresTable.setItems(membresTries);

        // Remplir les ComboBox
        configurerComboBoxes();

        // Désactiver les boutons si aucune sélection
        profilBtn.disableProperty().bind(Bindings.isNull(membresTable.getSelectionModel().selectedItemProperty()));
        supprimerBtn.disableProperty().bind(Bindings.isNull(membresTable.getSelectionModel().selectedItemProperty()));

        // Configuration initiale
        titreLabel.setText("Liste de tous les membres (" + Donnees.getNombreTotalMembres() + ")");
        bureauToggle.setText("Voir membres bureau");

        // Appliquer les filtres initiaux
        appliquerFiltres();
    }

    private void configurerComboBoxes() {
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
    }

    // ===== NOUVELLES MÉTHODES POUR OUVRIR LES FENÊTRES =====

    private void ouvrirFenetreProfil(Membre membre) throws IOException {
        Main.openProfil();
    }

    private void appliquerFiltres() {
        membresFiltres.setPredicate(membre -> {
            // Filtre bureau (si le toggle n'est pas sélectionné, on affiche tous)
            if (bureauToggle.isSelected()) {
                // Mode bureau seulement
                if (!membre.isMembreBureau()) return false;
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
                || membre.getEmail().toLowerCase().contains(rechercheLower)
                || membre.getCours().toLowerCase().contains(rechercheLower);
    }

    private void mettreAJourCompteur() {
        int total = membresFiltres.size();
        String typeAffichage = bureauToggle.isSelected() ? "membre(s) du bureau" : "membre(s)";
        totalMembresLabel.setText("Affichés : " + total + " " + typeAffichage);
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ===== MÉTHODES PUBLIQUES POUR L'INTÉGRATION =====

    public void ajouterMembre(Membre nouveauMembre) {
        // Le membre est déjà ajouté dans Donnees, on refresh juste la vue
        membresTable.refresh();
        appliquerFiltres();

        // Mettre à jour le titre avec le nouveau total
        titreLabel.setText("Liste de tous les membres (" + Donnees.getNombreTotalMembres() + ")");
    }

    public ObservableList<Membre> getTousLesMembres() {
        return tousLesMembres;
    }

    public void rafraichirVue() {
        // Recharger les données depuis Donnees
        tousLesMembres = Donnees.getLesMembres();
        membresFiltres = new FilteredList<>(tousLesMembres, p -> true);
        membresTries = new SortedList<>(membresFiltres);
        membresTries.comparatorProperty().bind(membresTable.comparatorProperty());
        membresTable.setItems(membresTries);

        // Reconfigurer les ComboBoxes au cas où de nouvelles saisons auraient été ajoutées
        configurerComboBoxes();

        // Réappliquer les filtres
        appliquerFiltres();

        // Mettre à jour le titre
        titreLabel.setText("Liste de tous les membres (" + Donnees.getNombreTotalMembres() + ")");
    }

    public void fermerPage(ActionEvent actionEvent) {
        Main.closeListeMembre();
    }
}