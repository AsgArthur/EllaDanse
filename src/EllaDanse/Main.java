package EllaDanse;

import EllaDanse.controllers.FenAccueil;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

    static private FenAccueil fAccueil;

    public void start(Stage f) throws Exception {
        fAccueil = new FenAccueil();
        fAccueil.show();
    }

    static public void main(String args[]) {
        Application.launch(args);
    }

    static public void fermerAppli() {
        System.exit(0);
    }
}