package EllaDanse.vue;


import EllaDanse.controller.Main;
import EllaDanse.modeles.Cours;
import EllaDanse.modeles.Donnees;
import EllaDanse.modeles.Membre;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    public void initialize(){
        validerBtn.disableProperty().bind(
                nomField.textProperty().isEmpty()
                        .or(prenomField.textProperty().isEmpty())
                        .or(telephoneField.textProperty().isEmpty())
                        .or(emailField.textProperty().isEmpty())
                        .or(dateNaissancePicker.valueProperty().isNull())
                        .or(saisonComboBox.valueProperty().isNull())
                        .or(ageField.textProperty().isEmpty())
                        .or(cbOui.selectedProperty().not().and(cbNon.selectedProperty().not()))
        );
        cbOui.disableProperty().bind(cbNon.selectedProperty());
        cbNon.disableProperty().bind(cbOui.selectedProperty());
        List<String> saisons = Donnees.getLesSaisons();
        saisonComboBox.getItems().clear();
        saisonComboBox.getItems().add("Toutes");
        saisonComboBox.getItems().addAll(saisons);
        saisonComboBox.setValue("Toutes");

        coursListView.setItems(Donnees.getLesCours());
        coursListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        saisonComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.equals("Toutes")) {
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
        Main.closeInscription();
    }

    @FXML
    void reinitialiser(ActionEvent event) {
        Main.closeInscription();
        Main.openInscription();
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

        else {
            Main.closeInscription();
            Membre m = new Membre(0,nomField.getText(), prenomField.getText(),Integer.parseInt(ageField.getText()), dateNaissancePicker.getValue().toString(), emailField.getText(), telephoneField.getText(), saisonComboBox.getValue(),mbBureau);
            Donnees.ajouterMembre(nomField.getText(), prenomField.getText(),Integer.parseInt(ageField.getText()), dateNaissancePicker.getValue().toString(), emailField.getText(), telephoneField.getText(), saisonComboBox.getValue(),mbBureau);
            List<Cours> coursSelectionnes = coursListView.getSelectionModel().getSelectedItems();
            for (Cours c : coursSelectionnes) {
                Donnees.ajouterIns(Donnees.dernierMembre(), c);
            }
            Main.rafraichir();
        }
    }

    public void cbOuiValider(ActionEvent event) {
        mbBureau = true;
    }

    public void cbNonValider(ActionEvent event) {
        mbBureau = false;
    }
}
