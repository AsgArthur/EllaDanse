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
    private Membre membre = null;

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
                List<Cours> coursDisponibles = Donnees.getLesCours();
                if (membre != null) {
                    List<Inscription> inscriptionsMembre = Donnees.getLesInscriptions().getInscriptions().stream()
                            .filter(ins -> ins.getMembre().equalsTo(membre))
                            .collect(Collectors.toList());

                    List<Cours> coursDejaInscrits = inscriptionsMembre.stream()
                            .map(Inscription::getVraiCours)
                            .collect(Collectors.toList());

                    coursDisponibles = coursDisponibles.stream()
                            .filter(c -> !coursDejaInscrits.contains(c))
                            .collect(Collectors.toList());
                }

                coursListView.setItems(FXCollections.observableArrayList(coursDisponibles));
            } else {
                coursListView.setItems(FXCollections.observableArrayList(getCoursDisponiblesPourSaison(newVal)));
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

        LocalDate dateNaissance = dateNaissancePicker.getValue();

        int ageRenseigne = Integer.parseInt(ageField.getText());

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
                reinitialiserChamps();
            }else{
                Main.closeInscription();
                Membre m = new Membre(0,nomField.getText(), prenomField.getText(),Integer.parseInt(ageField.getText()), dateNaissancePicker.getValue().toString(), emailField.getText(), telephoneField.getText(), saisonComboBox.getValue(),mbBureau);
                Donnees.ajouterMembre(nomField.getText(), prenomField.getText(),Integer.parseInt(ageField.getText()), dateNaissancePicker.getValue().toString(), emailField.getText(), telephoneField.getText(), saisonComboBox.getValue(),mbBureau);
                List<Cours> coursSelectionnes = coursListView.getSelectionModel().getSelectedItems();
                for (Cours c : coursSelectionnes) {
                    Donnees.ajouterIns(Donnees.dernierMembre(), c);
                }
                Main.rafraichirListeEmploye();
                reinitialiserChamps();
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
        } else {
            cbNon.setSelected(true);
            cbOui.setSelected(false);
        }

        GestionnaireInscription toutesInscriptions = Donnees.getLesInscriptions();

        List<Cours> coursInscrits = toutesInscriptions.getInscriptions().stream()
                .filter(i -> i.getMembre().equalsTo(membre))
                .map(Inscription::getVraiCours)
                .collect(Collectors.toList());

        List<Cours> coursDisponibles = Donnees.getLesCours().stream()
                .filter(c -> !coursInscrits.contains(c))
                .collect(Collectors.toList());

        coursListView.setItems(FXCollections.observableArrayList(coursDisponibles));
    }

    public void cbOuiValider(ActionEvent event) {
        mbBureau = true;
    }

    private List<Cours> getCoursDisponiblesPourSaison(String saison) {
        List<Cours> tousCours = Donnees.getLesCours().stream()
                .filter(c -> c.getSaison().equals(saison))
                .collect(Collectors.toList());

        if (membre == null) return tousCours;

        List<Inscription> inscriptionsMembre = Donnees.getLesInscriptions().getInscriptions().stream()
                .filter(ins -> ins.getMembre().equalsTo(membre))
                .collect(Collectors.toList());

        List<Cours> coursDejaInscrits = inscriptionsMembre.stream()
                .map(Inscription::getVraiCours)
                .collect(Collectors.toList());

        return tousCours.stream()
                .filter(c -> !coursDejaInscrits.contains(c))
                .collect(Collectors.toList());
    }

    public void cbNonValider(ActionEvent event) {
        mbBureau = false;
    }
}
