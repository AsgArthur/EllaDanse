package controller;

import java.io.File;

import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


public class FenAccueil extends Stage {

    private CtrlAccueil ctrl;

    public FenAccueil() throws IOException {
        this.setTitle("Accueil EllaDanse");
        this.setResizable(false);
        Scene laScene = new Scene(creerSceneGraph());
        this.setScene(laScene);
    }

    private Pane creerSceneGraph() throws IOException {
        File f = new File("src/EllaDanse/acceuil.fxml");
        FXMLLoader loader;
        loader = new FXMLLoader(f.toURI().toURL());
        Pane racine = loader.load();
        ctrl = loader.getController();
        return racine;
    }
}