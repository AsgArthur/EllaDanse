package EllaDanse.vue;

import EllaDanse.controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CtrlAccueil {

    @FXML
    private Label accueilLabel;

    @FXML
    private Button aproposBtn;

    @FXML
    private Button fermerBtn;

    @FXML
    private Button inscriptionBtn;

    @FXML
    private Button listeMembresBtn;

    @FXML
    void fermerAccueil(ActionEvent event) {
        Main.fermerAppli();
    }

    @FXML
    void handleAPropos(ActionEvent event) {
    }

    @FXML
    void ouvrirListeMembres(ActionEvent event) {
        Main.openListeMembre();
    }

    @FXML
    void ouvrirInscription(ActionEvent event) {
        Main.openInscription();
    }

}
