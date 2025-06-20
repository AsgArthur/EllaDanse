package EllaDanse.controller;

import EllaDanse.modeles.Membre;
import EllaDanse.vue.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

    static private FenAccueil fAccueil;
    static private FenInscription fInscription;
    static private FenListeMembres fListeMembres;
    static private FenProfil fProfil;

    public void start(Stage f) throws Exception {
        fAccueil = new FenAccueil();
        fInscription = new FenInscription();
        fListeMembres = new FenListeMembres();
        fProfil = new FenProfil();
        fAccueil.show();
    }

    static public void openInscription(){
        fAccueil.close();
        fInscription.show();
        fProfil.close();
    }

    static public void closeInscription(){
        fInscription.close();
        fAccueil.show();
    }

    static public void openListeMembre(){
        fAccueil.close();
        fListeMembres.show();
    }

    static public void closeListeMembre(){
        fListeMembres.close();
        fAccueil.show();
    }

    static public void openProfil(Membre m){
        fProfil.afficherMembre(m);
        fProfil.show();
        fListeMembres.close();
    }

    static public void closeProfil(){
        fProfil.close();
        fListeMembres.show();
    }

    static public void fermerAppli() {
        System.exit(0);
    }

    static public void rafraichirListeEmploye(){
        fListeMembres.rafraichir();
    }
    static public void nouvelleInscription(Membre m){
        fInscription.nouvelleInscription(m);
    }

    static public void main(String[] args) {
        Application.launch(args);
    }
}