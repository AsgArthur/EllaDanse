package EllaDanse.vue;


import EllaDanse.controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CtrlInscription {

    @FXML
    private Button annulerBtn;

    @FXML
    private ListView<?> coursListView;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private TextField emailField;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private ComboBox<?> saisonComboBox;

    @FXML
    private TextField telephoneField;

    @FXML
    private Button validerBtn;

    public void initialize(){
        validerBtn.disableProperty().bind(
                nomField.textProperty().isEmpty()
                        .or(prenomField.textProperty().isEmpty())
                        .or(telephoneField.textProperty().isEmpty())
                        .or(dateNaissancePicker.valueProperty().isNull())
                        .or(saisonComboBox.valueProperty().isNull())
        );
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

        else {Main.closeInscription();} 
    }

}
