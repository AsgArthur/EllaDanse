package EllaDanse.vue;

import application.*;
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
    @FXML private Label dateNaissanceLabel;
    @FXML private Label emailLabel;
    @FXML private Label telephoneLabel;
    @FXML private Label membreDepuisLabel;

    // Statistiques
    @FXML private Label totalInscriptionsLabel;
    @FXML private Label saisonsActivesLabel;
    @FXML private Label coursActuelsLabel;

    // Tableau des inscriptions
    @FXML private TableView<InscriptionViewModel> inscriptionsTable;
    @FXML private TableColumn<InscriptionViewModel, String> saisonCol;
    @FXML private TableColumn<InscriptionViewModel, String> coursCol;
    @FXML private TableColumn<InscriptionViewModel, String> horaireCol;
    @FXML private TableColumn<InscriptionViewModel, String> dateInscriptionCol;
    @FXML private TableColumn<InscriptionViewModel, String> statutCol;

    // Boutons
    @FXML private Button modifierBtn;
    @FXML private Button ajouterInscriptionBtn;
    @FXML private Button supprimerInscriptionBtn;
    @FXML private Button fermerBtn;

    // Filtres
    @FXML private ComboBox<String> filtreSaisonComboBox;
    @FXML private CheckBox afficherInactivesCheckBox;

    private GestionMembres gestionMembres;
    private Membre membre;
    private ObservableList<InscriptionViewModel> inscriptionsViewModel;

    @FXML
    public void initialize() {
        gestionMembres = GestionMembres.getInstance();

        // Configurer le tableau des inscriptions
        configurerTableauInscriptions();

        // Configurer les filtres
        configurerFiltres();

        // État initial des boutons
        mettreAJourEtatBoutons();
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
        if (membre != null) {
            chargerInformationsPersonnelles();
            chargerStatistiques();
            chargerInscriptions();
        }
    }

    private void configurerTableauInscriptions() {
        saisonCol.setCellValueFactory(new PropertyValueFactory<>("saison"));
        coursCol.setCellValueFactory(new PropertyValueFactory<>("cours"));
        horaireCol.setCellValueFactory(new PropertyValueFactory<>("horaire"));
        dateInscriptionCol.setCellValueFactory(new PropertyValueFactory<>("dateInscription"));
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Style pour les inscriptions inactives
        inscriptionsTable.setRowFactory(tv -> {
            TableRow<InscriptionViewModel> row = new TableRow<>();
            row.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && !newValue.isActive()) {
                    row.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: gray;");
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });

        inscriptionsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void configurerFiltres() {
        // Initialiser le filtre de saison
        filtreSaisonComboBox.getItems().add("Toutes les saisons");
        filtreSaisonComboBox.getItems().addAll(gestionMembres.getSaisons());
        filtreSaisonComboBox.setValue("Toutes les saisons");

        // Configurer la checkbox pour les inscriptions inactives
        afficherInactivesCheckBox.setSelected(true);
    }

    // Actions FXML
    @FXML
    public void onFiltreSaisonChanged() {
        filtrerInscriptions();
    }

    @FXML
    public void onAfficherInactivesChanged() {
        filtrerInscriptions();
    }

    @FXML
    public void onInscriptionSelected() {
        mettreAJourEtatBoutons();
    }

    private void chargerInformationsPersonnelles() {
        nomLabel.setText(membre.getNom());
        prenomLabel.setText(membre.getPrenom());
        ageLabel.setText(membre.getAge() + " ans");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateNaissanceLabel.setText(membre.getDateNaissance().format(formatter));

        emailLabel.setText(membre.getEmail().isEmpty() ? "Non renseigné" : membre.getEmail());
        telephoneLabel.setText(membre.getTelephone().isEmpty() ? "Non renseigné" : membre.getTelephone());

        // Date d'inscription (première inscription)
        List<Inscription> inscriptions = gestionMembres.getInscriptionsByMembre(membre);
        if (!inscriptions.isEmpty()) {
            LocalDate premiereInscription = inscriptions.stream()
                    .map(Inscription::getDateInscription)
                    .min(LocalDate::compareTo)
                    .orElse(LocalDate.now());
            membreDepuisLabel.setText("Membre depuis le " + premiereInscription.format(formatter));
        } else {
            membreDepuisLabel.setText("Aucune inscription");
        }
    }

    private void chargerStatistiques() {
        List<Inscription> inscriptions = gestionMembres.getInscriptionsByMembre(membre);

        totalInscriptionsLabel.setText(String.valueOf(inscriptions.size()));

        // Nombre de saisons distinctes
        long saisonsDistinctes = inscriptions.stream()
                .map(Inscription::getSaison)
                .distinct()
                .count();
        saisonsActivesLabel.setText(String.valueOf(saisonsDistinctes));

        // Cours actuels (dernière saison)
        if (!gestionMembres.getSaisons().isEmpty()) {
            String derniereSaison = gestionMembres.getSaisons().get(gestionMembres.getSaisons().size() - 1);
            long coursActuels = inscriptions.stream()
                    .filter(i -> i.getSaison().equals(derniereSaison))
                    .count();
            coursActuelsLabel.setText(String.valueOf(coursActuels));
        } else {
            coursActuelsLabel.setText("0");
        }
    }

    private void chargerInscriptions() {
        List<Inscription> inscriptions = gestionMembres.getInscriptionsByMembre(membre);

        inscriptionsViewModel = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Inscription inscription : inscriptions) {
            InscriptionViewModel vm = new InscriptionViewModel(
                    inscription.getSaison(),
                    inscription.getCours().getNom(),
                    inscription.getCours().getJour() + " " + inscription.getCours().getHoraireFormate(),
                    inscription.getDateInscription().format(formatter),
                    inscription.isActive() ? "Active" : "Inactive",
                    inscription.isActive(),
                    inscription
            );
            inscriptionsViewModel.add(vm);
        }

        // Trier par saison puis par cours
        inscriptionsViewModel.sort((vm1, vm2) -> {
            int saisonCompare = vm2.getSaison().compareTo(vm1.getSaison()); // Plus récent d'abord
            if (saisonCompare == 0) {
                return vm1.getCours().compareTo(vm2.getCours());
            }
            return saisonCompare;
        });

        inscriptionsTable.setItems(inscriptionsViewModel);
        filtrerInscriptions();
    }

    private void filtrerInscriptions() {
        if (inscriptionsViewModel == null) return;

        String saisonFiltre = filtreSaisonComboBox.getValue();
        boolean afficherInactives = afficherInactivesCheckBox.isSelected();

        ObservableList<InscriptionViewModel> inscriptionsFiltrees = inscriptionsViewModel.stream()
                .filter(vm -> {
                    boolean saisonOk = "Toutes les saisons".equals(saisonFiltre) || vm.getSaison().equals(saisonFiltre);
                    boolean statutOk = afficherInactives || vm.isActive();
                    return saisonOk && statutOk;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        inscriptionsTable.setItems(inscriptionsFiltrees);
    }

    private void mettreAJourEtatBoutons() {
        boolean inscriptionSelectionnee = inscriptionsTable.getSelectionModel().getSelectedItem() != null;
        supprimerInscriptionBtn.setDisable(!inscriptionSelectionnee);
    }

    @FXML
    public void modifierMembre() {
        try {
            ouvrirFenetreModification();
            // Recharger les informations après modification
            chargerInformationsPersonnelles();
            chargerStatistiques();
        } catch (IOException e) {
            afficherErreur("Erreur", "Impossible d'ouvrir la fenêtre de modification : " + e.getMessage());
        }
    }

    @FXML
    public void ajouterInscription() {
        try {
            ouvrirFenetreInscription();
            // Recharger les inscriptions après ajout
            chargerInscriptions();
            chargerStatistiques();
        } catch (IOException e) {
            afficherErreur("Erreur", "Impossible d'ouvrir la fenêtre d'inscription : " + e.getMessage());
        }
    }

    @FXML
    public void supprimerInscription() {
        InscriptionViewModel inscriptionVM = inscriptionsTable.getSelectionModel().getSelectedItem();
        if (inscriptionVM != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer cette inscription ?");
            alert.setContentText("Cours : " + inscriptionVM.getCours() + "\n" +
                    "Saison : " + inscriptionVM.getSaison() + "\n" +
                    "Cette action ne peut pas être annulée.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inscription inscription = inscriptionVM.getInscription();
                gestionMembres.desinscrireMembre(
                        inscription.getMembre(),
                        inscription.getCours(),
                        inscription.getSaison()
                );

                // Recharger
                chargerInscriptions();
                chargerStatistiques();

                afficherInfo("Inscription supprimée avec succès.");
            }
        }
    }

    @FXML
    public void exporterProfil() {
        if (membre != null) {
            List<Inscription> inscriptions = gestionMembres.getInscriptionsByMembre(membre);

            StringBuilder export = new StringBuilder();
            export.append("=== PROFIL MEMBRE ===\n\n");
            export.append("Nom complet : ").append(membre.getNomComplet()).append("\n");
            export.append("Email : ").append(membre.getEmail().isEmpty() ? "Non renseigné" : membre.getEmail()).append("\n");
            export.append("Téléphone : ").append(membre.getTelephone().isEmpty() ? "Non renseigné" : membre.getTelephone()).append("\n\n");

            if (!inscriptions.isEmpty()) {
                export.append("=== INSCRIPTIONS ===\n\n");
                inscriptions.stream()
                        .collect(Collectors.groupingBy(Inscription::getSaison))
                        .entrySet().stream()
                        .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                        .forEach(entry -> {
                            export.append("Saison ").append(entry.getKey()).append(" :\n");
                            entry.getValue().forEach(inscription -> {
                                Cours cours = inscription.getCours();
                                export.append("  • ").append(cours.getNom())
                                        .append(" - ").append(cours.getJour())
                                        .append(" ").append(cours.getHoraireFormate()).append("\n");
                            });
                            export.append("\n");
                        });
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export du profil");
            alert.setHeaderText("Profil de " + membre.getNomComplet());

            TextArea textArea = new TextArea(export.toString());
            textArea.setEditable(false);
            textArea.setPrefRowCount(20);
            textArea.setPrefColumnCount(60);

            alert.getDialogPane().setContent(textArea);
            alert.setResizable(true);
            alert.showAndWait();
        }
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

        InscriptionController inscriptionController = loader.getController();
        inscriptionController.setMembreAModifier(membre);

        Stage stage = new Stage();
        stage.setTitle("Modifier " + membre.getNomComplet());
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }

    private void ouvrirFenetreInscription() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        File fxmlFile = new File("C:/Users/crena/IdeaProjects/ellaDane/ressources/application/inscription.fxml");
        loader.setLocation(fxmlFile.toURI().toURL());
        Parent root = loader.load();

        InscriptionController inscriptionController = loader.getController();
        inscriptionController.setMembreAModifier(membre); // Pour pré-remplir avec ce membre

        Stage stage = new Stage();
        stage.setTitle("Nouvelle inscription pour " + membre.getNomComplet());
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

    // Classe interne pour le ViewModel des inscriptions
    public