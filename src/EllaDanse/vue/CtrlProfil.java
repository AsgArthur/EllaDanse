package EllaDanse.vue;

import EllaDanse.modeles.Membre;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;

import static java.lang.Integer.parseInt;

public class CtrlProfil {

    @FXML
    private CheckBox afficherInactivesCheckBox;

    @FXML
    private Label ageLabel;

    @FXML
    private Button ajouterInscriptionBtn;

    @FXML
    private TableColumn<?, ?> coursCol;

    @FXML
    private TableColumn<?, ?> dateInscriptionCol;

    @FXML
    private Label dateNaissanceLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Button fermerBtn;

    @FXML
    private ComboBox<?> filtreSaisonComboBox;

    @FXML
    private TableColumn<?, ?> horaireCol;

    @FXML
    private TableView<?> inscriptionsTable;

    @FXML
    private Label membreDepuisLabel;

    @FXML
    private Button modifierBtn;

    @FXML
    private Label nomLabel;

    @FXML
    private Label prenomLabel;

    @FXML
    private TableColumn<?, ?> saisonCol;

    @FXML
    private TableColumn<?, ?> statutCol;

    @FXML
    private Button supprimerInscriptionBtn;

    @FXML
    private Label telephoneLabel;

    @FXML
    void ajouterInscription(ActionEvent event) {

    }

    @FXML
    void fermer(ActionEvent event) {

    }

    @FXML
    void modifierMembre(ActionEvent event) {

    }

    @FXML
    void supprimerInscription(ActionEvent event) {

    }

    public void afficherMembre(Membre m) {
        nomLabel.setText(m.getNom());
        prenomLabel.setText(m.getPrenom());
        ageLabel.setText(Integer.toString(m.getAge()));
        dateNaissanceLabel.setText(m.getDateNaissance());
        emailLabel.setText(m.getEmail());
        telephoneLabel.setText(m.getTelephone());
    }
}

