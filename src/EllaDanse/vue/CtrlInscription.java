package EllaDanse.vue;


import EllaDanse.controller.Main;
import EllaDanse.modeles.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CtrlInscription {

    @FXML
    private Button annulerBtn;

    @FXML
    private ListView<Cours> coursListView;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private CheckBox cbNon;

    @FXML
    private CheckBox cbOui;

    @FXML
    private TextField emailField;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private ComboBox<String> saisonComboBox;

    @FXML
    private TextField telephoneField;

    @FXML
    private Button validerBtn;

    @FXML
    private TextField ageField;

    private boolean mbBureau;
    private Membre membre;

    public void initialize(){
        validerBtn.disableProperty().bind(
                nomField.textProperty().isEmpty()
                        .or(prenomField.textProperty().isEmpty())
                        .or(telephoneField.textProperty().isEmpty())
                        .or(emailField.textProperty().isEmpty())
                        .or(dateNaissancePicker.valueProperty().isNull())
                        .or(Bindings.createBooleanBinding(
                                () -> "Choisir".equals(saisonComboBox.getValue()),
                                saisonComboBox.valueProperty()
                        ))
                        .or(ageField.textProperty().isEmpty())
                        .or(cbOui.selectedProperty().not().and(cbNon.selectedProperty().not()))
        );
        cbOui.disableProperty().bind(cbNon.selectedProperty());
        cbNon.disableProperty().bind(cbOui.selectedProperty());
        List<String> saisons = Donnees.getLesSaisons();
        saisonComboBox.getItems().clear();
        saisonComboBox.getItems().add("Choisir");
        saisonComboBox.getItems().addAll(saisons);
        saisonComboBox.setValue("Choisir");

        coursListView.setItems(Donnees.getLesCours());
        coursListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        saisonComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.equals("Choisir")) {
                coursListView.setItems(Donnees.getLesCours()); // affiche tous les cours
            } else {
                List<Cours> coursFiltres = Donnees.getLesCours().stream()
                        .filter(c -> c.getSaison().equals(newVal))
                        .collect(Collectors.toList());

                coursListView.setItems(FXCollections.observableArrayList(coursFiltres));
            }
        });
    }

    @FXML
    void annuler(ActionEvent event) {
        reinitialiserChamps();
        Main.closeInscription();
    }

    private void reinitialiserChamps() {
        nomField.clear();
        prenomField.clear();
        telephoneField.clear();
        emailField.clear();
        ageField.clear();
        dateNaissancePicker.setValue(null);
        saisonComboBox.setValue("Choisir"); // ou null si tu préfères
        cbOui.setSelected(false);
        cbNon.setSelected(false);
        coursListView.getSelectionModel().clearSelection();
        messageLabel.setText(""); // si tu veux aussi vider le message
    }

    @FXML
    void reinitialiser(ActionEvent event) {
        reinitialiserChamps();
    }

    @FXML
    void validerInscription(ActionEvent event) {
        String telephone = telephoneField.getText().trim();
        if (!telephone.matches("\\d{10}")) {
            event.consume();
            Alert erreur = new Alert(Alert.AlertType.ERROR, "Le numéro de téléphone doit contenir exactement 10 chiffres.", ButtonType.OK);
            erreur.setTitle("Téléphone : format invalide");
            erreur.showAndWait();
        }

        else if (dateNaissancePicker.getValue().isAfter(java.time.LocalDate.now())) {
            event.consume();
            Alert erreur = new Alert(Alert.AlertType.ERROR, "La date de naissance doit être antérieure à aujourd’hui.", ButtonType.OK);
            erreur.setTitle("Date de naissance : invalide");
            erreur.showAndWait();
        }

        else if (!emailField.getText().contains("@")){
            event.consume();
            Alert erreur = new Alert(Alert.AlertType.ERROR, "Le mail doit avoir un format adapté.", ButtonType.OK);
            erreur.setTitle("Email : format invalide");
            erreur.showAndWait();
        }

        // Date de naissance sélectionnée par l'utilisateur
        LocalDate dateNaissance = dateNaissancePicker.getValue();

        // Âge renseigné dans le champ
        int ageRenseigne = Integer.parseInt(ageField.getText());

        // Calcul de l'âge réel à partir de la date de naissance
        int ageReel = Period.between(dateNaissance, LocalDate.now()).getYears();

        if (ageReel != ageRenseigne) {
            event.consume();
            Alert erreur = new Alert(Alert.AlertType.ERROR, "La date de naissance et l'age doivent correspondre", ButtonType.OK);
            erreur.setTitle("DateNaissance : format invalide");
            erreur.showAndWait();
        }

        else {
            if (membre != null){
                Main.closeInscription();
                List<Cours> coursSelectionnes = coursListView.getSelectionModel().getSelectedItems();
                for (Cours c : coursSelectionnes) {
                    Donnees.ajouterIns(membre, c);
                }
                Main.rafraichirListeEmploye();
            }else{
                Main.closeInscription();
                Membre m = new Membre(0,nomField.getText(), prenomField.getText(),Integer.parseInt(ageField.getText()), dateNaissancePicker.getValue().toString(), emailField.getText(), telephoneField.getText(), saisonComboBox.getValue(),mbBureau);
                Donnees.ajouterMembre(nomField.getText(), prenomField.getText(),Integer.parseInt(ageField.getText()), dateNaissancePicker.getValue().toString(), emailField.getText(), telephoneField.getText(), saisonComboBox.getValue(),mbBureau);
                List<Cours> coursSelectionnes = coursListView.getSelectionModel().getSelectedItems();
                for (Cours c : coursSelectionnes) {
                    Donnees.ajouterIns(Donnees.dernierMembre(), c);
                }
                Main.rafraichirListeEmploye();
            }

        }
    }

    public void nouvelleInscription(Membre m){
        membre = m;
        nomField.setText(membre.getNom());
        prenomField.setText(membre.getPrenom());
        ageField.setText(Integer.toString(membre.getAge()));
        dateNaissancePicker.setValue(LocalDate.parse(membre.getDateNaissance()));
        emailField.setText(membre.getEmail());
        telephoneField.setText(membre.getTelephone());

        if (membre.isMembreBureau()){
            cbOui.setSelected(true);
        }else if (!membre.isMembreBureau()){
            cbNon.setSelected(true);
            cbOui.setSelected(false);
        }

        GestionnaireInscription toutesInscriptions = Donnees.getLesInscriptions();

        // 2. Récupère la liste des inscriptions depuis l'objet GestionnaireInscription
        List<Inscription> listeToutesInscriptions = toutesInscriptions.getInscriptions();           // ← extrait la liste
        List<Inscription> listeCoursDuMembre = new ArrayList<>();
        List<Cours> listeFinale = new ArrayList<>();
        for (Inscription i : listeToutesInscriptions){
            if (i.getMembre().equalsTo(membre)){
                listeCoursDuMembre.add(i);
            }
        }
        for (Inscription i : listeCoursDuMembre){
            listeToutesInscriptions.remove(i);
        }
        for (Inscription i : listeToutesInscriptions){
            listeFinale.add(i.getVraiCours());
        }
        coursListView.setItems(FXCollections.observableArrayList(listeFinale));
    }

    public void cbOuiValider(ActionEvent event) {
        mbBureau = true;
    }

    public void cbNonValider(ActionEvent event) {
        mbBureau = false;
    }
}
