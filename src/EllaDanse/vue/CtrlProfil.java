package EllaDanse.vue;

import EllaDanse.controller.Main;
import EllaDanse.modeles.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Array;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.remainderUnsigned;

public class CtrlProfil {

    @FXML
    private CheckBox afficherInactivesCheckBox;

    @FXML
    private Label ageLabel;

    @FXML
    private Button ajouterInscriptionBtn;

    @FXML
    private Label dateNaissanceLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Button fermerBtn;

    @FXML
    private ComboBox<String> filtreSaisonComboBox;

    @FXML
    private TableColumn<Inscription, String> horaireCol;

    @FXML
    private TableColumn<Inscription, String> professeurCol;

    @FXML
    private TableColumn<Inscription, String> coursCol;

    @FXML
    private TableColumn<Inscription, String> saisonCol;

    @FXML
    private TableView<Inscription> inscriptionsTable;

    @FXML
    private Label membreDepuisLabel;

    @FXML
    private Button modifierBtn;

    @FXML
    private Button validerBtn;

    @FXML
    private Label nomLabel;

    @FXML
    private TextField txtAge;

    @FXML
    private TextField txtDateNaissance;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtTelephone;

    @FXML
    private Label prenomLabel;

    private int id;

    @FXML
    private Button supprimerInscriptionBtn;

    @FXML
    private Label telephoneLabel;

    private Membre membre;
    private GestionnaireInscription toutesInscriptions;

    @FXML
    void ajouterInscription(ActionEvent event) {
        Main.openInscription();
        Main.nouvelleInscription(membre);

    }

    @FXML
    void fermer(ActionEvent event) {
        validerBtn.setDisable(true);

        txtNom.setDisable(true);
        txtPrenom.setDisable(true);
        txtAge.setDisable(true);
        txtDateNaissance.setDisable(true);
        txtEmail.setDisable(true);
        txtTelephone.setDisable(true);

        modifierBtn.setDisable(false);
        Main.closeProfil();
    }

    @FXML
    void modifierMembre(ActionEvent event) {
        txtNom.setDisable(false);
        txtPrenom.setDisable(false);
        txtAge.setDisable(false);
        txtDateNaissance.setDisable(false);
        txtEmail.setDisable(false);
        txtTelephone.setDisable(false);

        modifierBtn.setDisable(true);
        validerBtn.setDisable(false);
    }

    @FXML
    void validerModifs(ActionEvent event) {
        String telephone = txtTelephone.getText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate dateNaissance = LocalDate.parse(txtDateNaissance.getText(), formatter);

        int ageRenseigne = Integer.parseInt(txtAge.getText());

        int ageReel = Period.between(dateNaissance, LocalDate.now()).getYears();
        if (!telephone.matches("\\d{10}")) {
            event.consume();
            Alert erreur = new Alert(Alert.AlertType.ERROR, "Le numéro de téléphone doit contenir exactement 10 chiffres.", ButtonType.OK);
            erreur.setTitle("Téléphone : format invalide");
            erreur.showAndWait();
        }

        else if (LocalDate.parse(txtDateNaissance.getText()).isAfter(LocalDate.now())) {
            event.consume();
            Alert erreur = new Alert(Alert.AlertType.ERROR, "La date de naissance doit être antérieure à aujourd’hui.", ButtonType.OK);
            erreur.setTitle("Date de naissance : invalide");
            erreur.showAndWait();
        }

        else if (!txtEmail.getText().contains("@")){
            event.consume();
            Alert erreur = new Alert(Alert.AlertType.ERROR, "Le mail doit avoir un format adapté.", ButtonType.OK);
            erreur.setTitle("Email : format invalide");
            erreur.showAndWait();
        }


        else if (ageReel != ageRenseigne) {
            event.consume();
            Alert erreur = new Alert(Alert.AlertType.ERROR, "La date de naissance et l'age doivent correspondre", ButtonType.OK);
            erreur.setTitle("DateNaissance : format invalide");
            erreur.showAndWait();
        }
        else{
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de la modification");
            confirmation.setHeaderText("modifier le membre ?");
            confirmation.setContentText("Êtes-vous sûr de vouloir modifier le membre n°" + membre.getId() +
                    "?\nCette action ne peut pas être annulée.");
            Optional<ButtonType> resultat = confirmation.showAndWait();



            if (resultat.isPresent() && resultat.get() == ButtonType.OK) {

                Donnees.modifierMembre(membre.getId(), txtNom.getText(), txtPrenom.getText(), Integer.parseInt(txtAge.getText()),
                        txtDateNaissance.getText(), txtEmail.getText(), txtTelephone.getText(), membre.getSaison(), membre.isMembreBureau());

                validerBtn.setDisable(true);

                txtNom.setDisable(true);
                txtPrenom.setDisable(true);
                txtAge.setDisable(true);
                txtDateNaissance.setDisable(true);
                txtEmail.setDisable(true);
                txtTelephone.setDisable(true);

                modifierBtn.setDisable(false);

                Main.rafraichirListeEmploye();
            }
        }
    }

    @FXML
    void supprimerInscription(ActionEvent event) {

        Inscription inscription = inscriptionsTable.getSelectionModel().getSelectedItem();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer l'inscription'");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ?\n" +
                inscription.getCours() + " " + inscription.getHoraire() + " ?\n\n" +
                "Cette action ne peut pas être annulée ni remboursée.");
        Optional<ButtonType> resultat = confirmation.showAndWait();

        if (resultat.isPresent() && resultat.get() == ButtonType.OK) {

            Donnees.suppInscription(inscription.getMembre(), inscription.getVraiCours());

            List<Inscription> liste = toutesInscriptions.getInscriptions();           // ← extrait la liste
            List<Inscription> listeCoursMembre = new ArrayList<>();
            for (Inscription i : liste){
                if (i.getMembre().equalsTo(membre)){
                    listeCoursMembre.add(i);
                }
            }

            inscriptionsTable.setItems(FXCollections.observableArrayList(listeCoursMembre));

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Suppression réussie");
            info.setHeaderText(null);
            info.setContentText("Le membre " + inscription.getCours() + " " +
                    inscription.getHoraire() + " a été supprimé avec succès.");
            info.showAndWait();

            Main.rafraichirListeEmploye();
        }


    }

    public void afficherMembre(Membre m) {
        this.membre = m;
        txtNom.setText(m.getNom());
        txtPrenom.setText(m.getPrenom());
        txtAge.setText(Integer.toString(m.getAge()));
        txtDateNaissance.setText(m.getDateNaissance());
        txtEmail.setText(m.getEmail());
        txtTelephone.setText(m.getTelephone());
    }

    public void afficherLesInscriptions(){

        GestionnaireInscription toutesInscriptions = Donnees.getLesInscriptions();

        List<Inscription> liste = toutesInscriptions.getInscriptions();           // ← extrait la liste
        List<Inscription> listeCoursMembre = new ArrayList<>();
        for (Inscription i : liste){
            if (i.getMembre().equalsTo(membre)){
                listeCoursMembre.add(i);
            }
        }

        inscriptionsTable.setItems(FXCollections.observableArrayList(listeCoursMembre));

        supprimerInscriptionBtn.disableProperty().bind(Bindings.isNull(inscriptionsTable.getSelectionModel().selectedItemProperty()));
    }

    public void initialize() {
        saisonCol.setCellValueFactory(new PropertyValueFactory<>("saison"));
        coursCol.setCellValueFactory(new PropertyValueFactory<>("cours"));
        horaireCol.setCellValueFactory(new PropertyValueFactory<>("horaire"));
        professeurCol.setCellValueFactory(new PropertyValueFactory<>("professeur"));

        toutesInscriptions = Donnees.getLesInscriptions();
    }
}

