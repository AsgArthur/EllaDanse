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
    private Button desinscriptionBtn;

    @FXML
    private Button fermerBtn;

    @FXML
    private Button inscriptionBtn;

    @FXML
    private Button listeMembresBtn;

    @FXML
    private Button profilBtn;

    @FXML
    void fermerAccueil(ActionEvent event) {
        Main.fermerAppli();
    }

    @FXML
    void handleAPropos(ActionEvent event) {

    }

    @FXML
    void handleAfficherMembres(ActionEvent event) {

    }

    @FXML
    void handleDesinscription(ActionEvent event) {

    }

    @FXML
    void handleInscription(ActionEvent event) {

    }

    @FXML
    void handleProfil(ActionEvent event) {

    }

}
