package EllaDanse.vue;

import EllaDanse.controller.Main;
import EllaDanse.modeles.Cours;
import EllaDanse.modeles.Donnees;
import EllaDanse.modeles.Inscription;
import EllaDanse.modeles.Membre;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import java.util.*;
import java.util.stream.Collectors;

public class CtrlListeMembres {

    @FXML private TableView<Inscription> membresTable;  // Changé de Membre à Inscription
    @FXML private TableColumn<Inscription, Integer> idCol;
    @FXML private TableColumn<Inscription, String> nomCol;
    @FXML private TableColumn<Inscription, String> prenomCol;
    @FXML private TableColumn<Inscription, Integer> ageCol;
    @FXML private TableColumn<Inscription, String> emailCol;
    @FXML private TableColumn<Inscription, String> saisonCol;
    @FXML private TableColumn<Inscription, String> dateNaissanceCol;
    @FXML private TableColumn<Inscription, String> telephoneCol;
    @FXML private TableColumn<Inscription, String> coursCol;
    @FXML private TableColumn<Inscription, String> horaireCol;
    @FXML private TableColumn<Inscription, String> professeurCol;

    @FXML private Label titreLabel;
    @FXML private Label totalMembresLabel;
    @FXML private ToggleButton bureauToggle;
    @FXML private Button profilBtn;
    @FXML private Button supprimerBtn;
    @FXML private Button retourBtn;
    @FXML private TextField rechercheField;
    @FXML private ComboBox<String> saisonComboBox;
    @FXML private ComboBox<String> triComboBox;

    private ObservableList<Inscription> toutesLesInscriptions;
    private FilteredList<Inscription> inscriptionsFiltrees;
    private SortedList<Inscription> inscriptionsTriees;

    @FXML
    void ouvrirProfilMembre() {
        Inscription inscriptionSelectionnee = membresTable.getSelectionModel().getSelectedItem();
        if (inscriptionSelectionnee != null) {
            Main.openProfil(inscriptionSelectionnee.getMembre());
        }
    }

    @FXML
    public void supprimerMembre() {
        Inscription inscriptionSelectionnee = membresTable.getSelectionModel().getSelectedItem();
        if (inscriptionSelectionnee != null) {
            Membre membre = inscriptionSelectionnee.getMembre();
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText("Supprimer l'inscription");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer l'inscription de " +
                    membre.getNom() + " " + membre.getPrenom() + " au cours " +
                    inscriptionSelectionnee.getCours() + " ?\n\n" +
                    "Cette action ne peut pas être annulée.");

            Optional<ButtonType> resultat = confirmation.showAndWait();
            if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
                // Supprimer l'inscription
                Donnees.suppInscription(membre, inscriptionSelectionnee.getVraiCours());
                // Rafraîchir la vue
                rafraichirVue();

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Suppression réussie");
                info.setHeaderText(null);
                info.setContentText("L'inscription a été supprimée avec succès.");
                info.showAndWait();
            }
        }
    }

    @FXML
    public void toggleBureau() {
        appliquerFiltres();
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

        Comparator<Inscription> comparator;

        switch (tri) {
            case "Ordre alphabétique et saison":
                comparator = Comparator.comparing((Inscription i) -> i.getMembre().getNom(), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(i -> i.getMembre().getPrenom(), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Inscription::getSaison);
                break;

            case "Saison et ordre alphabétique":
                comparator = Comparator.comparing(Inscription::getSaison)
                        .thenComparing((Inscription i) -> i.getMembre().getNom(), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(i -> i.getMembre().getPrenom(), String.CASE_INSENSITIVE_ORDER);
                break;

            case "Cours et ordre alphabétique":
                comparator = Comparator.comparing(Inscription::getCours)
                        .thenComparing((Inscription i) -> i.getMembre().getNom(), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(i -> i.getMembre().getPrenom(), String.CASE_INSENSITIVE_ORDER);
                break;

            default:
                comparator = Comparator.comparing((Inscription i) -> i.getMembre().getNom(), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(i -> i.getMembre().getPrenom(), String.CASE_INSENSITIVE_ORDER);
                break;
        }

        inscriptionsTriees.setComparator(comparator);
    }

    @FXML
    public void gererDoubleClic() {
        if (membresTable.getSelectionModel().getSelectedItem() != null) {
            ouvrirProfilMembre();
        }
    }

    public void initialize() {
        // Configuration des colonnes pour afficher les données du membre via l'inscription
        idCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getMembre().getId()).asObject());

        nomCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMembre().getNom()));

        prenomCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMembre().getPrenom()));

        ageCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getMembre().getAge()).asObject());

        emailCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMembre().getEmail()));

        saisonCol.setCellValueFactory(new PropertyValueFactory<>("saison"));

        dateNaissanceCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMembre().getDateNaissance()));

        telephoneCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMembre().getTelephone()));

        // Configuration des colonnes spécifiques aux cours
        if (coursCol != null) {
            coursCol.setCellValueFactory(new PropertyValueFactory<>("cours"));
        }
        if (horaireCol != null) {
            horaireCol.setCellValueFactory(new PropertyValueFactory<>("horaire"));
        }
        if (professeurCol != null) {
            professeurCol.setCellValueFactory(new PropertyValueFactory<>("professeur"));
        }

        // Style conditionnel sur les lignes pour les membres du bureau
        membresTable.setRowFactory(tv -> {
            TableRow<Inscription> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && newVal.getMembre().isMembreBureau()) {
                    row.setStyle("-fx-background-color: #e3f2fd; -fx-font-weight: bold;");
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });

        // Gestion du double-clic
        membresTable.setOnMouseClicked((MouseEvent e) -> {
            if ((e.getClickCount() == 2) && (e.getButton() == MouseButton.PRIMARY) && (e.getTarget() instanceof Text)) {
                Inscription inscription = membresTable.getSelectionModel().getSelectedItem();
                if (inscription != null) {
                    Main.openProfil(inscription.getMembre());
                }
            }
        });

        // Chargement des données
        chargerInscriptions();

        // Configuration des comboBox
        configurerComboBoxes();

        // Désactivation des boutons si aucune inscription sélectionnée
        profilBtn.disableProperty().bind(Bindings.isNull(membresTable.getSelectionModel().selectedItemProperty()));
        supprimerBtn.disableProperty().bind(Bindings.isNull(membresTable.getSelectionModel().selectedItemProperty()));

        // Titre de départ
        mettreAJourTitre();

        // Appliquer filtres + tri par défaut
        appliquerFiltres();
        changerTri();
    }

    private void chargerInscriptions() {
        // Récupérer toutes les inscriptions
        List<Inscription> inscriptions = Donnees.getLesInscriptions().getInscriptions();

        // Créer aussi des "inscriptions vides" pour les membres sans cours
        List<Inscription> toutesInscriptions = new ArrayList<>(inscriptions);

        // Ajouter les membres sans inscription
        for (Membre membre : Donnees.getLesMembres()) {
            boolean aDesInscriptions = inscriptions.stream()
                    .anyMatch(i -> i.getMembre().equalsTo(membre));

            if (!aDesInscriptions) {
                // Créer une inscription "vide" pour ce membre
                toutesInscriptions.add(new InscriptionVide(membre));
            }
        }

        toutesLesInscriptions = FXCollections.observableArrayList(toutesInscriptions);
        inscriptionsFiltrees = new FilteredList<>(toutesLesInscriptions, p -> true);
        inscriptionsTriees = new SortedList<>(inscriptionsFiltrees);
        membresTable.setItems(inscriptionsTriees);
    }

    private void configurerComboBoxes() {
        // Options de tri disponibles
        triComboBox.getItems().setAll(
                "Ordre alphabétique et saison",
                "Saison et ordre alphabétique",
                "Cours et ordre alphabétique"
        );
        triComboBox.setValue("Ordre alphabétique et saison");

        // Remplissage des saisons disponibles
        saisonComboBox.getItems().clear();
        saisonComboBox.getItems().add("Toutes");
        List<String> saisons = Donnees.getLesSaisons();
        saisonComboBox.getItems().addAll(saisons);
        saisonComboBox.setValue("Toutes");
    }

    private void appliquerFiltres() {
        inscriptionsFiltrees.setPredicate(inscription -> {
            Membre membre = inscription.getMembre();

            // Filtre bureau
            if (bureauToggle.isSelected()) {
                if (!membre.isMembreBureau()) return false;
            } else {
                if (membre.isMembreBureau()) return false;
            }

            // Filtre saison
            String saisonSelectionnee = saisonComboBox.getValue();
            if (saisonSelectionnee != null && !saisonSelectionnee.equals("Toutes")
                    && !inscription.getSaison().equals(saisonSelectionnee)) {
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
                    || inscription.getCours().toLowerCase().contains(rechercheLower)
                    || inscription.getProfesseur().toLowerCase().contains(rechercheLower);
        });

        mettreAJourCompteur();
    }

    private void mettreAJourTitre() {
        if (bureauToggle.isSelected()) {
            titreLabel.setText("Inscriptions des membres du bureau");
        } else {
            titreLabel.setText("Toutes les inscriptions");
        }
    }

    private void mettreAJourCompteur() {
        int total = inscriptionsFiltrees.size();
        totalMembresLabel.setText("Affichées : " + total + " inscription(s)");
        mettreAJourTitre();
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void rafraichirVue() {
        chargerInscriptions();
        configurerComboBoxes();
        appliquerFiltres();
        mettreAJourCompteur();
    }

    @FXML
    private TableView<Inscription> tableMembres;

    @FXML
    private void gererClicSurTable(MouseEvent event) {
        Inscription inscription = tableMembres.getSelectionModel().getSelectedItem();

        if (inscription == null) return;

        if (event.getClickCount() == 2 && event.isPrimaryButtonDown()) {
            Main.openProfil(inscription.getMembre());
        }
    }

    public void fermerPage(ActionEvent actionEvent) {
        Main.closeListeMembre();
    }

    // Classe interne pour représenter un membre sans inscription
    private static class InscriptionVide extends Inscription {
        public InscriptionVide(Membre membre) {
            super(membre, null);
        }
    }
}