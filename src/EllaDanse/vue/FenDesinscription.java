package EllaDanse.vue;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;


public class FenDesinscription extends Stage {


    private CtrlDesinscription ctrl;


    public FenDesinscription() throws IOException {
        this.setTitle("Détail d'un employé");
        this.setResizable(false);
        Scene laScene = new Scene(creerSceneGraph());
        this.setScene(laScene);
    }


    private Pane creerSceneGraph() throws IOException {
        // Dans l'instruction suivante, indiquer le chemin complet du fichier fxml
        File f = new File("src/EllaDanse/desinscription.fxml");
        FXMLLoader loader;
        loader = new FXMLLoader(f.toURI().toURL());
        Pane racine = loader.load();
        ctrl = loader.getController();
        return racine;
    }
}

