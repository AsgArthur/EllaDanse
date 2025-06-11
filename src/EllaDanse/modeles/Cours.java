package EllaDanse.modeles;

public class Cours {
    private String nom;
    private String niveau;
    private String professeur;
    private String horaire;

    public Cours(String nom, String niveau, String professeur, String horaire) {
        this.nom = nom;
        this.niveau = niveau;
        this.professeur = professeur;
        this.horaire = horaire;
    }

    public String getNom() { return nom + " " + niveau; }
    public String getNiveau() { return niveau; }
    public String getProfesseur() { return professeur; }
    public String getHoraire() { return horaire; }

    public void setNom(String nom) { this.nom = nom; }
    public void setNiveau(String niveau) { this.niveau = niveau; }
    public void setProfesseur(String professeur) { this.professeur = professeur; }
    public void setHoraire(String horaire) { this.horaire = horaire; }

    @Override
    public String toString() {
        return nom + " - " + niveau;
    }
}
