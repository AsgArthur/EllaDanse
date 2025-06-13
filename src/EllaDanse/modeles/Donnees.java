package EllaDanse.modeles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Donnees {

    private static ObservableList<Membre> lesMembres = FXCollections.observableArrayList();

    private static ObservableList<Cours> lesCours = FXCollections.observableArrayList();

    private static GestionnaireInscription inscriptions = new GestionnaireInscription();

    private static int prochainIdMembre = 1;

    static {
        initialiserDonneesTest();
    }

    //méthodes membres

    public static ObservableList<Membre> getLesMembres() {
        return lesMembres;
    }

    public static GestionnaireInscription getLesInscriptions(){return inscriptions;}

    public static void suppInscription(Membre membre, Cours cours){
        inscriptions.supprimerInscription(membre, cours);
    }

    public static Membre getMembreById(int id) {
        return lesMembres.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static void ajouterMembre(String nom, String prenom, int age, String dateNaissance, String email,
                                     String telephone, String saison, boolean membreBureau) {
        Membre nouveau = new Membre(prochainIdMembre++, nom, prenom, age, dateNaissance, email, telephone, saison, membreBureau);
        lesMembres.add(nouveau);
    }

    public static void ajouterIns(Membre m, Cours c){
        inscriptions.ajouterInscription(m, c);
    }

    public static void modifierMembre(int id,String nom, String prenom, int age, String dateNaissance, String email,
                                      String telephone, String saison, boolean membreBureau) {
        Membre membre = getMembreById(id);
        if (membre != null) {
            membre.setNom(nom);
            membre.setPrenom(prenom);
            membre.setAge(age);
            membre.setDateNaissance(dateNaissance);
            membre.setEmail(email);
            membre.setTelephone(telephone);
            membre.setSaison(saison);
            membre.setMembreBureau(membreBureau);
        }
    }

    public static Membre dernierMembre(){
        return lesMembres.getLast();
    }

    public static boolean supprimerMembre(Membre membre) {
        return lesMembres.remove(membre);
    }



    //méthodes cours

    public static ObservableList<Cours> getLesCours() {
        return lesCours;
    }




    //méthodes test

    public static List<String> getLesSaisons() {
        return lesMembres.stream()
                .map(Membre::getSaison)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    //tests

    private static void initialiserDonneesTest() {
        // Ajout des cours
        lesCours.add(new Cours("Jazz", "Débutant", "Marie Leclerc", "Lundi 18h-19h30", "2023-2024"));
        lesCours.add(new Cours("Jazz", "Intermédiaire", "Marie Leclerc", "Mardi 19h-20h30", "2023-2024"));
        lesCours.add(new Cours("Jazz", "Avancé", "Marie Leclerc", "Jeudi 20h-21h30", "2023-2024"));

        lesCours.add(new Cours("Classique", "Débutant", "Sophie Martin", "Lundi 17h-18h30", "2023-2024"));
        lesCours.add(new Cours("Classique", "Intermédiaire", "Sophie Martin", "Mercredi 18h-19h30", "2023-2024"));
        lesCours.add(new Cours("Classique", "Avancé", "Sophie Martin", "Vendredi 19h-20h30", "2023-2024"));

        lesCours.add(new Cours("Contemporain", "Débutant", "Lucas Dubois", "Mardi 18h-19h30", "2023-2024"));
        lesCours.add(new Cours("Contemporain", "Intermédiaire", "Lucas Dubois", "Jeudi 18h-19h30", "2023-2024"));

        lesCours.add(new Cours("Hip-Hop", "Débutant", "Thomas Richard", "Mercredi 17h-18h30", "2024-2025"));
        lesCours.add(new Cours("Hip-Hop", "Intermédiaire", "Thomas Richard", "Vendredi 17h-18h30", "2023-2024"));

        lesCours.add(new Cours("Salsa", "Tous niveaux", "Carmen Rodriguez", "Samedi 14h-15h30", "2024-2025"));

        // Ajout des membres
        ajouterMembre("Dupont", "Marie", 26, "1999-06-12", "marie.dupont@email.com", "0601020304", "2024-2025", false);
        ajouterMembre("Martin", "Pierre", 31, "1994-01-22", "pierre.martin@email.com", "0602030405", "2024-2025", true);
        ajouterMembre("Bernard", "Sophie", 23, "2002-03-18", "sophie.bernard@email.com", "0603040506", "2023-2024", false);
        ajouterMembre("Durand", "Jean", 28, "1996-07-05", "jean.durand@email.com", "0604050607", "2024-2025", true);
        ajouterMembre("Moreau", "Emma", 19, "2005-11-09", "emma.moreau@email.com", "0605060708", "2023-2024", false);
        ajouterMembre("Petit", "Lucas", 36, "1989-02-25", "lucas.petit@email.com", "0606070809", "2023-2024", false);
        ajouterMembre("Roux", "Chloé", 27, "1997-08-14", "chloe.roux@email.com", "0607080910", "2024-2025", true);
        ajouterMembre("Lefevre", "Thomas", 25, "2000-04-03", "thomas.lefevre@email.com", "0608091011", "2024-2025", false);
        ajouterMembre("Garcia", "Ana", 31, "1993-12-17", "ana.garcia@email.com", "0609101112", "2023-2024", false);
        ajouterMembre("Dubois", "Julie", 27, "1998-05-30", "julie.dubois@email.com", "0610111213", "2024-2025", false);
        ajouterMembre("Fournier", "Alexandre", 29, "1995-09-21", "alex.fournier@email.com", "0611121314", "2024-2025", true);
        ajouterMembre("Lambert", "Camille", 23, "2001-10-02", "camille.lambert@email.com", "0612131415", "2024-2025", false);

        //création inscriptions
        inscriptions.ajouterInscription(lesMembres.get(0), lesCours.get(1)); // Marie Dupont → Jazz Intermédiaire
        inscriptions.ajouterInscription(lesMembres.get(0), lesCours.get(5)); // Pierre Martin → Classique Avancé
        inscriptions.ajouterInscription(lesMembres.get(1), lesCours.get(10)); // Pierre Martin → Salsa
        inscriptions.ajouterInscription(lesMembres.get(3), lesCours.get(9)); // Jean Durand → Hip-Hop Intermédiaire
        inscriptions.ajouterInscription(lesMembres.get(4), lesCours.get(0)); // Emma Moreau → Jazz Débutant
        inscriptions.ajouterInscription(lesMembres.get(5), lesCours.get(10)); // Lucas Petit → Salsa
        inscriptions.ajouterInscription(lesMembres.get(6), lesCours.get(4)); // Chloé Roux → Classique Intermédiaire
        inscriptions.ajouterInscription(lesMembres.get(6), lesCours.get(10)); // Chloé Roux → Salsa
        inscriptions.ajouterInscription(lesMembres.get(7), lesCours.get(8)); // Thomas Lefevre → Hip-Hop Débutant
        inscriptions.ajouterInscription(lesMembres.get(8), lesCours.get(10)); // Ana Garcia → Salsa
        inscriptions.ajouterInscription(lesMembres.get(9), lesCours.get(7)); // Julie Dubois → Contemporain Intermédiaire
        inscriptions.ajouterInscription(lesMembres.get(10), lesCours.get(2)); // Alexandre Fournier → Jazz Avancé
        inscriptions.ajouterInscription(lesMembres.get(11), lesCours.get(3)); // Camille Lambert → Classique Débutant
    }

}