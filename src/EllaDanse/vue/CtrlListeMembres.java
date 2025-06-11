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
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
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
    @FXML private Button retourBtn;
    @FXML private TextField rechercheField;
    @FXML private ComboBox<String> saisonComboBox;
    @FXML private ComboBox<String> triComboBox;

    private ObservableList<Membre> tousLesMembres;
    private FilteredList<Membre> membresFiltres;
    private SortedList<Membre> membresTries;


    @FXML
    void ouvrirProfilMembre() {
        Main.openProfil(membresTable.getSelectionModel().getSelectedItem());
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

                if (Donnees.supprimerMembre(membreSelectionne)) {
                    // 🔁 Rafraîchir toute la vue (table, filtre, compteur, tri)
                    rafraichirVue();

                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Suppression réussie");
                    info.setHeaderText(null);
                    info.setContentText("Le membre " + membreSelectionne.getPrenom() + " " +
                            membreSelectionne.getNom() + " a été supprimé avec succès.");
                    info.showAndWait();
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
            case "Ordre alphabétique et saison":
                comparator = Comparator.comparing(Membre::getNom)
                        .thenComparing(Membre::getPrenom)
                        .thenComparing(Membre::getSaison);
                break;

            case "Saison et ordre alphabétique":
                comparator = Comparator.comparing(Membre::getSaison)
                        .thenComparing(Membre::getNom)
                        .thenComparing(Membre::getPrenom);
                break;

            case "Saison, cours et ordre alphabétique":
                comparator = Comparator.comparing(Membre::getSaison)
                        .thenComparing(Membre::getCours)
                        .thenComparing(Membre::getNom)
                        .thenComparing(Membre::getPrenom);
                break;

            case "Saison, ordre alphabétique et cour":
                comparator = Comparator.comparing(Membre::getSaison)
                        .thenComparing(Membre::getNom)
                        .thenComparing(Membre::getPrenom)
                        .thenComparing(Membre::getCours);
                break;

            default:
                comparator = Comparator.comparing(Membre::getNom)
                        .thenComparing(Membre::getPrenom);
                break;
        }

        membresTries.setComparator(comparator);
        membresTable.setItems(membresTries); // pour forcer la mise à jour
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

        // Configuration des lignes + style + double-clic
        membresTable.setRowFactory(tv -> {
            TableRow<Membre> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && newVal.isMembreBureau()) {
                    row.setStyle("-fx-background-color: #e3f2fd; -fx-font-weight: bold;");
                } else {
                    row.setStyle("");
                }
            });

            membresTable.setOnMouseClicked((MouseEvent e) -> {
                if (( e.getClickCount()==2)
                        && (e.getButton()== MouseButton.PRIMARY)
                        && (e.getTarget() instanceof Text)) {
                    Main.openProfil(membresTable.getSelectionModel().getSelectedItem());
                }
            });

            return row;
        });

        tousLesMembres = Donnees.getLesMembres();
        membresFiltres = new FilteredList<>(tousLesMembres, p -> true);
        membresTries = new SortedList<>(membresFiltres);


        membresTable.setItems(membresTries);

        configurerComboBoxes();
        profilBtn.disableProperty().bind(Bindings.isNull(membresTable.getSelectionModel().selectedItemProperty()));
        supprimerBtn.disableProperty().bind(Bindings.isNull(membresTable.getSelectionModel().selectedItemProperty()));

        titreLabel.setText("Liste de tous les membres (" + Donnees.getNombreTotalMembres() + ")");
        bureauToggle.setText("Voir membres bureau");

        appliquerFiltres();
    }

    private void configurerComboBoxes() {
        // Options de tri disponibles
        triComboBox.getItems().setAll(
                "Ordre alphabétique et saison",
                "Saison et ordre alphabétique",
                "Saison, cours et ordre alphabétique",
                "Saison, ordre alphabétique et cour"
        );
        triComboBox.setValue("Ordre alphabétique et saison");

        // Remplissage des saisons disponibles
        saisonComboBox.getItems().clear();
        saisonComboBox.getItems().add("Toutes");

        List<String> saisons = Donnees.getLesSaisons();
        saisonComboBox.getItems().addAll(saisons);
        saisonComboBox.setValue("Toutes");
    }



    // ===== NOUVELLES MÉTHODES POUR OUVRIR LES FENÊTRES =====

    private void ouvrirFenetreProfil(Membre membre) throws IOException {
        Main.openProfil(membre);
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

    @FXML
    private TableView<Membre> tableMembres;

    @FXML
    private void gererClicSurTable(MouseEvent event) {
        Membre membre = tableMembres.getSelectionModel().getSelectedItem();

        if (membre == null) return;

        if (event.getClickCount() == 2 && event.isPrimaryButtonDown()) {
            // Double-clic gauche → ouvrir le profil
            Main.openProfil(membre);
        } else if (event.getClickCount() == 1 && event.isPrimaryButtonDown()) {
            // Simple clic gauche → action par défaut (sélection, affichage, etc.)
            selectionnerMembre(membre);
        }
    }

    private void selectionnerMembre(Membre membre) {
    }


    public void fermerPage(ActionEvent actionEvent) {
        Main.closeListeMembre();
    }

    public Membre membreClique(){
        Membre membreClique = tableMembres.getSelectionModel().getSelectedItem();
        return membreClique;
    }

}