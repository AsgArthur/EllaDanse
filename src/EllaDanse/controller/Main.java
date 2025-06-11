package EllaDanse.controller;

import EllaDanse.vue.FenAccueil;
import EllaDanse.vue.FenInscription;
import EllaDanse.vue.FenListeMembres;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

    static private FenAccueil fAccueil;
    static private FenInscription fInscription;
    static private FenListeMembres fListeMembres;

    public void start(Stage f) throws Exception {
        fAccueil = new FenAccueil();
        fInscription = new FenInscription();
        fListeMembres = new FenListeMembres();
        fAccueil.show();
    }

    static public void openInscription(){
        fAccueil.close();
        fInscription.show();
    }

    static public void openListeMembre(){
        fAccueil.close();
        fListeMembres.show();
    }

    static public void fermerAppli() {
        System.exit(0);
    }

    static public void main(String args[]) {
        Application.launch(args);
    }
}