package EllaDanse.vue;


import EllaDanse.controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
        if (nomField.getText().isEmpty()){

        }

    }

}
