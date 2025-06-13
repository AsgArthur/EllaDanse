package EllaDanse.vue;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class FenListeMembres extends Stage {

    private CtrlListeMembres ctrl;

    public FenListeMembres() throws IOException {
        this.setTitle("Liste des membre EllaDanse");
        this.setResizable(false);
        Scene laScene = new Scene(creerSceneGraph());
        this.setScene(laScene);
    }

    private Pane creerSceneGraph() throws IOException {
        File f = new File("src/EllaDanse/listeMembres.fxml");
        FXMLLoader loader;
        loader = new FXMLLoader(f.toURI().toURL());
        Pane racine = loader.load();
        ctrl = loader.getController();
        return racine;
    }


    public void rafraichir(){
        ctrl.rafraichirVue();
    }
}
