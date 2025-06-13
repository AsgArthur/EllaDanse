package EllaDanse.vue;

import EllaDanse.controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.ButtonType;

public class CtrlAccueil {

    @FXML
    private Label accueilLabel;

    @FXML
    private Button aproposBtn;

    @FXML
    private Button fermerBtn;

    @FXML
    private Button inscriptionBtn;

    @FXML
    private Button listeMembresBtn;

    @FXML
    void fermerAccueil(ActionEvent event) {
        Main.fermerAppli();
    }

    @FXML
    void handleAPropos(ActionEvent event) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("À propos");
        dialog.setHeaderText(null);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setPrefWidth(450);

        Label titre = new Label("EllaDanse");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titre.setTextFill(Color.DARKBLUE);

        Label sousTitre = new Label("Système de gestion d'école de danse");
        sousTitre.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        sousTitre.setTextFill(Color.GRAY);

        Separator separator = new Separator();

        Label version = new Label("Version 1.0 - 2025");
        version.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        Label description = new Label(
                "EllaDanse est une solution complète pour gérer efficacement\n" +
                        "votre école de danse. De l'inscription des élèves à la gestion\n" +
                        "des cours, tout est pensé pour vous simplifier la vie."
        );
        description.setWrapText(true);

        GridPane features = new GridPane();
        features.setHgap(10);
        features.setVgap(5);
        features.setPadding(new Insets(10, 0, 10, 0));

        Label featuresTitle = new Label("Fonctionnalités :");
        featuresTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        String[][] fonctionnalites = {
                {"✓", "Gestion complète des membres"},
                {"✓", "Inscription aux cours multiples"},
                {"✓", "Suivi par saison"},
                {"✓", "Gestion du bureau"},
                {"✓", "5 types de danse supportés"},
                {"✓", "Recherche et filtrage avancés"}
        };

        for (int i = 0; i < fonctionnalites.length; i++) {
            Label check = new Label(fonctionnalites[i][0]);
            check.setTextFill(Color.GREEN);
            check.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            Label feature = new Label(fonctionnalites[i][1]);

            features.add(check, 0, i);
            features.add(feature, 1, i);
        }

        Label copyright = new Label("© 2025 EllaDanse - Tous droits réservés");
        copyright.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        copyright.setTextFill(Color.GRAY);


        content.getChildren().addAll(
                titre,
                sousTitre,
                separator,
                version,
                new Label(""),
                description,
                new Label(""),
                featuresTitle,
                features,
                new Label(""),
                copyright
        );

        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("Fermer");
        okButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        dialog.showAndWait();
    }

    @FXML
    void ouvrirListeMembres(ActionEvent event) {
        Main.openListeMembre();
    }

    @FXML
    void ouvrirInscription(ActionEvent event) {
        Main.openInscription();
    }

}
