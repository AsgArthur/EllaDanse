package EllaDanse.modeles;

import javafx.beans.property.*;

public class Membre {
    private IntegerProperty id;
    private static StringProperty nom;
    private StringProperty prenom;
    private IntegerProperty age;
    private StringProperty email;
    private StringProperty saison;
    private StringProperty cours;
    private BooleanProperty membreBureau;

    public Membre(int id, String nom, String prenom, int age, String email, String saison, String cours, boolean membreBureau) {
        this.id = new SimpleIntegerProperty(id);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.age = new SimpleIntegerProperty(age);
        this.email = new SimpleStringProperty(email);
        this.saison = new SimpleStringProperty(saison);
        this.cours = new SimpleStringProperty(cours);
        this.membreBureau = new SimpleBooleanProperty(membreBureau);
    }

    // Getters
    public int getId() { return id.get(); }
    public String getNom() { return nom.get(); }
    public String getPrenom() { return prenom.get(); }
    public int getAge() { return age.get(); }
    public String getEmail() { return email.get(); }
    public String getSaison() { return saison.get(); }
    public String getCours() { return cours.get(); }
    public boolean isMembreBureau() { return membreBureau.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setNom(String nom) { this.nom.set(nom); }
    public void setPrenom(String prenom) { this.prenom.set(prenom); }
    public void setAge(int age) { this.age.set(age); }
    public void setEmail(String email) { this.email.set(email); }
    public void setSaison(String saison) { this.saison.set(saison); }
    public void setCours(String cours) { this.cours.set(cours); }
    public void setMembreBureau(boolean membreBureau) { this.membreBureau.set(membreBureau); }

    // Properties (pour les bindings JavaFX)
    public IntegerProperty idProperty() { return id; }
    public StringProperty nomProperty() { return nom; }
    public StringProperty prenomProperty() { return prenom; }
    public IntegerProperty ageProperty() { return age; }
    public StringProperty emailProperty() { return email; }
    public StringProperty saisonProperty() { return saison; }
    public StringProperty coursProperty() { return cours; }
    public BooleanProperty membreBureauProperty() { return membreBureau; }
}
