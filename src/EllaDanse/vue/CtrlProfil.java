package EllaDanse.vue;

import EllaDanse.modeles.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProfilController {

    // Informations personnelles
    @FXML private Label nomLabel;
    @FXML private Label prenomLabel;
    @FXML private Label ageLabel;
    @FXML private Label emailLabel;
    @FXML private Label saisonLabel;
    @FXML private Label coursLabel;
    @FXML private Label statutLabel;

    // Statistiques
    @FXML private Label totalMembresLabel;
    @FXML private Label membresBureauLabel;
    @FXML private Label membresNormauxLabel;

    // Tableau des autres membres (pour comparaison)
    @FXML private TableView<Membre> autresMembresTable;
    @FXML private TableColumn<Membre, Integer> idCol;
    @FXML private TableColumn<Membre, String> nomCol;
    @FXML private TableColumn<Membre, String> prenomCol;
    @FXML private TableColumn<Membre, Integer> ageCol;
    @FXML private TableColumn<Membre, String> emailCol;
    @FXML private TableColumn<Membre, String> saisonCol;
    @FXML private TableColumn<Membre, String> coursCol;
    @FXML private TableColumn<Membre, String> statutCol;

    // Boutons
    @FXML private Button modifierBtn;
    @FXML private Button supprimerBtn;
    @FXML private Button fermerBtn;

    // Filtres
    @FXML private ComboBox<String> filtreSaisonComboBox;
    @FXML private ComboBox<String> filtreCoursComboBox;
    @FXML private CheckBox afficherBureauCheckBox;

    private Membre membreCourant;

    @FXML
    public void initialize() {
        // Configurer le tableau des autres membres
        configurerTableauAutresMembres();

        // Configurer les filtres
        configurerFiltres();

        // Charger les statistiques générales
        chargerStatistiques();

        // État initial des boutons
        mettreAJourEtatBoutons();
    }

    public void setMembre(Membre membre) {
        this.membreCourant = membre;
        if (membre != null) {
            chargerInformationsMembre();
            chargerAutresMembres();
        }
    }

    private void configurerTableauAutresMembres() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        saisonCol.setCellValueFactory(new PropertyValueFactory<>("saison"));
        coursCol.setCellValueFactory(new PropertyValueFactory<>("cours"));

        // Colonne statut avec style
        statutCol.setCellValueFactory(cellData -> {
            boolean estBureau = cellData.getValue().isMembreBureau();
            return new javafx.beans.property.SimpleStringProperty(estBureau ? "Bureau" : "Membre");
        });

        // Style pour les membres du bureau
        autresMembresTable.setRowFactory(tv -> {
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

        autresMembresTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void configurerFiltres() {
        // Filtres de saison
        filtreSaisonComboBox.getItems().add("Toutes les saisons");
        filtreSaisonComboBox.getItems().addAll(Donnees.getLesSaisons());
        filtreSaisonComboBox.setValue("Toutes les saisons");

        // Filtres de cours
        filtreCoursComboBox.getItems().add("Tous les cours");
        filtreCoursComboBox.getItems().addAll(Donnees.getLesNomsCours());
        filtreCoursComboBox.setValue("Tous les cours");

        // Checkbox pour afficher les membres du bureau
        afficherBureauCheckBox.setSelected(true);
    }

    // Actions FXML
    @FXML
    public void onFiltreSaisonChanged() {
        filtrerAutresMembres();
    }

    @FXML
    public void onFiltreCoursChanged() {
        filtrerAutresMembres();
    }

    @FXML
    public void onAfficherBureauChanged() {
        filtrerAutresMembres();
    }

    @FXML
    public void onMembreSelected() {
        mettreAJourEtatBoutons();
    }

    private void chargerInformationsMembre() {
        nomLabel.setText(membreCourant.getNom());
        prenomLabel.setText(membreCourant.getPrenom());
        ageLabel.setText(membreCourant.getAge() + " ans");
        emailLabel.setText(membreCourant.getEmail());
        saisonLabel.setText(membreCourant.getSaison());
        coursLabel.setText(membreCourant.getCours());
        statutLabel.setText(membreCourant.isMembreBureau() ? "Membre du bureau" : "Membre normal");

        // Style du statut
        if (membreCourant.isMembreBureau()) {
            statutLabel.setStyle("-fx-text-fill: #1976d2; -fx-font-weight: bold;");
        } else {
            statutLabel.setStyle("-fx-text-fill: #388e3c;");
        }
    }

    private void chargerStatistiques() {
        totalMembresLabel.setText(String.valueOf(Donnees.getNombreTotalMembres()));
        membresBureauLabel.setText(String.valueOf(Donnees.getNombreMembresBureau()));
        membresNormauxLabel.setText(String.valueOf(Donnees.getNombreTotalMembres() - Donnees.getNombreMembresBureau()));
    }

    private void chargerAutresMembres() {
        // Charger tous les membres sauf le membre courant
        List<Membre> autresMembres = Donnees.getLesMembres().stream()
                .filter(m -> m.getId() != membreCourant.getId())
                .collect(Collectors.toList());

        autresMembresTable.setItems(FXCollections.observableArrayList(autresMembres));
        filtrerAutresMembres();
    }

    private void filtrerAutresMembres() {
        String saisonFiltre = filtreSaisonComboBox.getValue();
        String coursFiltre = filtreCoursComboBox.getValue();
        boolean afficherBureau = afficherBureauCheckBox.isSelected();

        List<Membre> membresFiltres = Donnees.getLesMembres().stream()
                .filter(m -> m.getId() != membreCourant.getId()) // Exclure le membre courant
                .filter(m -> {
                    // Filtre par saison
                    boolean saisonOk = "Toutes les saisons".equals(saisonFiltre) || m.getSaison().equals(saisonFiltre);

                    // Filtre par cours
                    boolean coursOk = "Tous les cours".equals(coursFiltre) || m.getCours().equals(coursFiltre);

                    // Filtre par statut bureau
                    boolean bureauOk = afficherBureau || !m.isMembreBureau();

                    return saisonOk && coursOk && bureauOk;
                })
                .collect(Collectors.toList());

        autresMembresTable.setItems(FXCollections.observableArrayList(membresFiltres));
    }

    private void mettreAJourEtatBoutons() {
        boolean membreSelectionne = autresMembresTable.getSelectionModel().getSelectedItem() != null;
        // Pour l'instant, on peut seulement visualiser les autres membres
        // Les boutons modifier/supprimer concernent le membre courant
    }

    @FXML
    public void modifierMembre() {
        try {
            ouvrirFenetreModification();
            // Recharger les informations après modification
            chargerInformationsMembre();
            chargerStatistiques();
            chargerAutresMembres();
        } catch (IOException e) {
            afficherErreur("Erreur", "Impossible d'ouvrir la fenêtre de modification : " + e.getMessage());
        }
    }

    @FXML
    public void supprimerMembre() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le membre " + membreCourant.getNom() + " " + membreCourant.getPrenom() + " ?");
        alert.setContentText("Cette action ne peut pas être annulée.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean supprime = Donnees.supprimerMembre(membreCourant);

            if (supprime) {
                afficherInfo("Membre supprimé avec succès.");
                fermer(); // Fermer la fenêtre car le membre n'existe plus
            } else {
                afficherErreur("Erreur", "Impossible de supprimer le membre.");
            }
        }
    }

    @FXML
    public void exporterProfil() {
        StringBuilder export = new StringBuilder();
        export.append("=== PROFIL MEMBRE ===\n\n");
        export.append("Nom : ").append(membreCourant.getNom()).append("\n");
        export.append("Prénom : ").append(membreCourant.getPrenom()).append("\n");
        export.append("Âge : ").append(membreCourant.getAge()).append(" ans\n");
        export.append("Email : ").append(membreCourant.getEmail()).append("\n");
        export.append("Saison : ").append(membreCourant.getSaison()).append("\n");
        export.append("Cours : ").append(membreCourant.getCours()).append("\n");
        export.append("Statut : ").append(membreCourant.isMembreBureau() ? "Membre du bureau" : "Membre normal").append("\n\n");

        // Ajouter des statistiques contextuelles
        List<Membre> memesCours = Donnees.getMembresByCours(membreCourant.getCours());
        List<Membre> memeSaison = Donnees.getMembresBySaison(membreCourant.getSaison());

        export.append("=== STATISTIQUES ===\n\n");
        export.append("Membres dans le même cours : ").append(memesCours.size()).append("\n");
        export.append("Membres dans la même saison : ").append(memeSaison.size()).append("\n");
        export.append("Total membres association : ").append(Donnees.getNombreTotalMembres()).append("\n");
        export.append("Membres du bureau : ").append(Donnees.getNombreMembresBureau()).append("\n");

        export.append("\n=== AUTRES MEMBRES DU MÊME COURS ===\n");
        memesCours.stream()
                .filter(m -> m.getId() != membreCourant.getId())
                .forEach(m -> export.append("- ").append(m.getPrenom()).append(" ").append(m.getNom())
                        .append(m.isMembreBureau() ? " (Bureau)" : "").append("\n"));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export du profil");
        alert.setHeaderText("Profil de " + membreCourant.getPrenom() + " " + membreCourant.getNom());

        TextArea textArea = new TextArea(export.toString());
        textArea.setEditable(false);
        textArea.setPrefRowCount(20);
        textArea.setPrefColumnCount(60);

        alert.getDialogPane().setContent(textArea);
        alert.setResizable(true);
        alert.showAndWait();
    }

    @FXML
    public void fermer() {
        Stage stage = (Stage) fermerBtn.getScene().getWindow();
        stage.close();
    }

    private void ouvrirFenetreModification() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        File fxmlFile = new File("C:/Users/crena/IdeaProjects/ellaDane/ressources/application/inscription.fxml");
        loader.setLocation(fxmlFile.toURI().toURL());
        Parent root = loader.load();

        // Passer le membre à modifier au contrôleur d'inscription
        CtrlInscription inscriptionController = loader.getController();
        if (inscriptionController != null) {
            inscriptionController.setMembreAModifier(membreCourant);
        }

        Stage stage = new Stage();
        stage.setTitle("Modifier " + membreCourant.getPrenom() + " " + membreCourant.getNom());
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}