package EllaDanse.vue;

import java.io.File;
import java.io.IOException;

import EllaDanse.modeles.Membre;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

public class FenInscription extends Stage{
    private CtrlInscription ctrl;

    public FenInscription() throws IOException {
        this.setTitle("Inscription EllaDanse");
        this.setResizable(false);
        Scene laScene = new Scene(creerSceneGraph());
        this.setScene(laScene);
    }

    private Pane creerSceneGraph() throws IOException {
        File f = new File("src/EllaDanse/inscription.fxml");
        FXMLLoader loader;
        loader = new FXMLLoader(f.toURI().toURL());
        Pane racine = loader.load();
        ctrl = loader.getController();
        return racine;
    }

    public void nouvelleInscription(Membre membre){
        ctrl.nouvelleInscription(membre);
    }

}



