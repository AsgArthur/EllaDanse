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
        if (nomField.getText().isEmpty()) {
            event.consume();
            Alert erreur = new Alert(Alert.AlertType.ERROR, "Veuillez remplir le champ Nom.", ButtonType.OK);
            erreur.setTitle("Nom : champ requis");
            erreur.showAndWait();
        }
        else if (prenomField.getText().isEmpty()) {
            event.consume();
            Alert erreur = new Alert(Alert.AlertType.ERROR, "Veuillez remplir le champ Prénom.", ButtonType.OK);
            erreur.setTitle("Prénom : champ requis");
            erreur.showAndWait();
        }

        else if (telephoneField.getText().isEmpty()) {
            event.consume();
            Alert erreur = new Alert(Alert.AlertType.ERROR, "Veuillez remplir le champ Téléphone.", ButtonType.OK);
            erreur.setTitle("Téléphone : champ requis");
            erreur.showAndWait();
        }

        else if (dateNaissancePicker.getValue() == null) {
            event.consume();
            Alert erreur = new Alert(Alert.AlertType.ERROR, "Veuillez sélectionner une date de naissance.", ButtonType.OK);
            erreur.setTitle("Date de naissance : champ requis");
            erreur.showAndWait();
        }

        else if (saisonComboBox.getValue() == null) {
            event.consume();
            Alert erreur = new Alert(Alert.AlertType.ERROR, "Veuillez sélectionner une saison.", ButtonType.OK);
            erreur.setTitle("Saison : champ requis");
            erreur.showAndWait();
            return;
        }
        else{
            Main.closeInscription();
        }
    }

}
