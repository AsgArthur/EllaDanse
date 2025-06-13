package EllaDanse.modeles;

import javafx.beans.property.*;

public class Membre {
    private IntegerProperty id;
    private StringProperty nom;
    private StringProperty prenom;
    private IntegerProperty age;
    private StringProperty dateNaissance;
    private StringProperty email;
    private StringProperty telephone;
    private StringProperty saison;
    private BooleanProperty membreBureau;

    public Membre(int id, String nom, String prenom, int age, String dateNaissance, String email, String telephone, String saison, boolean membreBureau) {
        this.id = new SimpleIntegerProperty(id);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.age = new SimpleIntegerProperty(age);
        this.dateNaissance = new SimpleStringProperty(dateNaissance);
        this.email = new SimpleStringProperty(email);
        this.telephone = new SimpleStringProperty(telephone);
        this.saison = new SimpleStringProperty(saison);
        this.membreBureau = new SimpleBooleanProperty(membreBureau);
    }

    // Getters
    public int getId() { return id.get(); }
    public String getNom() { return nom.get(); }
    public String getPrenom() { return prenom.get(); }
    public int getAge() { return age.get(); }
    public String getDateNaissance() {return dateNaissance.get();}
    public String getEmail() { return email.get(); }
    public String getTelephone() { return telephone.get();}
    public String getSaison() { return saison.get(); }
    public boolean isMembreBureau() { return membreBureau.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setNom(String nom) { this.nom.set(nom); }
    public void setPrenom(String prenom) { this.prenom.set(prenom); }
    public void setAge(int age) { this.age.set(age); }
    public void setDateNaissance(String dateNaissance) {this.dateNaissance.set(dateNaissance); }
    public void setEmail(String email) { this.email.set(email); }
    public void setTelephone(String telephone) { this.telephone.set(telephone); }
    public void setSaison(String saison) { this.saison.set(saison); }
    public void setMembreBureau(boolean membreBureau) { this.membreBureau.set(membreBureau); }


    public boolean equalsTo(Membre m){
        return this.id == m.id;
    }
}
