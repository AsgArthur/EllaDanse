package EllaDanse.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CtrlListeMembres {

    @FXML private TableColumn<?, ?> ageCol;
    @FXML private TableColumn<?, ?> coursCol;
    @FXML private TableColumn<?, ?> emailCol;
    @FXML private TableColumn<?, ?> idCol;
    @FXML private TableView<?> membresTable;
    @FXML private Button modifierBtn;
    @FXML private TableColumn<?, ?> nomCol;
    @FXML private TableColumn<?, ?> prenomCol;
    @FXML private Button profilBtn;
    @FXML private TextField rechercheField;
    @FXML private TableColumn<?, ?> saisonCol;
    @FXML private ComboBox<?> saisonComboBox;
    @FXML private Button supprimerBtn;
    @FXML private Label totalMembresLabel;
    @FXML private ComboBox<?> triComboBox;


    @FXML void modifierMembre(ActionEvent event) {

    }

    @FXML void ouvrirProfilMembre(ActionEvent event) {

    }

    @FXML void supprimerMembre(ActionEvent event) {

    }

}
