package EllaDanse;

import javafx.application.Application;
import javafx.stage.Stage;
import modele.*;
import vue.*;

public class Main extends Application{

    public void start(Stage f) throws Exception {

        Donnees.chargementDonnees();
        //initialisation des fenêtres
        fNouvEmp = new FenNouvelEmploye();
        fModifierEmp = new FenModifierEmploye();
        fListeEmp = new FenListeEmployes();
        fListeEmp.show();
    }

    static public void main(String args[]) {
        Application.launch(args);
    }

    static public void fermerAppli() {

    }