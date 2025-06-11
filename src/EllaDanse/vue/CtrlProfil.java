package EllaDanse.vue;

import EllaDanse.modeles.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

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
    private Label nomLabel;

    @FXML
    private Label prenomLabel;

    private int id;

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
        Inscription inscription = inscriptionsTable.getSelectionModel().getSelectedItem();

        Donnees.suppInscription(inscription.getMembre(), inscription.getVraiCours());
    }

    public void afficherMembre(Membre m) {
        nomLabel.setText(m.getNom());
        prenomLabel.setText(m.getPrenom());
        ageLabel.setText(Integer.toString(m.getAge()));
        dateNaissanceLabel.setText(m.getDateNaissance());
        emailLabel.setText(m.getEmail());
        telephoneLabel.setText(m.getTelephone());

        GestionnaireInscription toutesInscriptions = Donnees.getLesInscriptions();

        // 2. Récupère la liste des inscriptions depuis l'objet GestionnaireInscription
        List<Inscription> liste = toutesInscriptions.getInscriptions();           // ← extrait la liste
        System.out.println("caca");
        List<Inscription> listeCoursMembre = new ArrayList<>();
        for (Inscription i : liste){
            if (i.getMembre().equalsTo(m)){
                listeCoursMembre.add(i);
                System.out.println("caca");
            }
        }

        // 3. Affiche dans la TableView
        inscriptionsTable.setItems(FXCollections.observableArrayList(listeCoursMembre));

        supprimerInscriptionBtn.disableProperty().bind(Bindings.isNull(inscriptionsTable.getSelectionModel().selectedItemProperty()));
    }

    public void initialize() {
        saisonCol.setCellValueFactory(new PropertyValueFactory<>("saison"));
        coursCol.setCellValueFactory(new PropertyValueFactory<>("cours"));
        horaireCol.setCellValueFactory(new PropertyValueFactory<>("horaire"));
        professeurCol.setCellValueFactory(new PropertyValueFactory<>("professeur"));
    }
}

