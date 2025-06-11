package EllaDanse.modeles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Donnees {

    // Liste principale des membres
    private static ObservableList<Membre> lesMembres = FXCollections.observableArrayList();

    // Liste des cours disponibles
    private static ObservableList<Cours> lesCours = FXCollections.observableArrayList();

    //Liste des inscriptions de membres
    private static GestionnaireInscription inscriptions = new GestionnaireInscription();

    // Compteur pour les IDs
    private static int prochainIdMembre = 1;

    // Initialisation statique avec des données de test
    static {
        initialiserDonneesTest();
    }

    // ========== MÉTHODES POUR LES MEMBRES ==========

    /**
     * Retourne la liste complète des membres
     */
    public static ObservableList<Membre> getLesMembres() {
        return lesMembres;
    }

    public static GestionnaireInscription getLesInscriptions(){return inscriptions;}

    public static void suppInscription(Membre membre, Cours cours){
        inscriptions.supprimerInscription(membre, cours);
    }

    /**
     * Retourne uniquement les membres du bureau
     */
    public static List<Membre> getLesMembresBureau() {
        return lesMembres.stream()
                .filter(Membre::isMembreBureau)
                .collect(Collectors.toList());
    }

    /**
     * Retourne uniquement les membres normaux (non bureau)
     */
    public static List<Membre> getLesMembresNormaux() {
        return lesMembres.stream()
                .filter(m -> !m.isMembreBureau())
                .collect(Collectors.toList());
    }

    /**
     * Retourne les membres d'une saison spécifique
     */
    public static List<Membre> getMembresBySaison(String saison) {
        return lesMembres.stream()
                .filter(m -> m.getSaison().equals(saison))
                .collect(Collectors.toList());
    }

    /**
     * Retourne les membres d'un cours spécifique
     */
    public static List<Membre> getMembresByCours(String cours) {
        return lesMembres.stream()
                .filter(m -> m.getCours().equals(cours))
                .collect(Collectors.toList());
    }

    /**
     * Trouve un membre par son ID
     */
    public static Membre getMembreById(int id) {
        return lesMembres.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Ajoute un nouveau membre
     */
    public static void ajouterMembre(String nom, String prenom, int age, String dateNaissance, String email,
                                     String telephone, String saison, String cours, boolean membreBureau) {
        Membre nouveau = new Membre(prochainIdMembre++, nom, prenom, age, dateNaissance, email, telephone, saison, cours, membreBureau);
        lesMembres.add(nouveau);
    }

    /**
     * Ajoute un membre existant
     */
    public static void ajouterMembre(Membre membre) {
        if (membre.getId() == 0) {
            membre.setId(prochainIdMembre++);
        }
        lesMembres.add(membre);
    }

    /**
     * Modifie un membre existant
     */
    public static void modifierMembre(int id, String nom, String prenom, int age, String dateNaissance, String email,
                                      String telephone, String saison, String cours, boolean membreBureau) {
        Membre membre = getMembreById(id);
        if (membre != null) {
            membre.setNom(nom);
            membre.setPrenom(prenom);
            membre.setAge(age);
            membre.setDateNaissance(dateNaissance);
            membre.setEmail(email);
            membre.setTelephone(telephone);
            membre.setSaison(saison);
            membre.setCours(cours);
            membre.setMembreBureau(membreBureau);
        }
    }


    /**
     * Supprime un membre
     */
    public static boolean supprimerMembre(Membre membre) {
        return lesMembres.remove(membre);
    }

    // ========== MÉTHODES POUR LES COURS ==========

    /**
     * Retourne la liste des cours
     */
    public static ObservableList<Cours> getLesCours() {
        return lesCours;
    }

    /**
     * Retourne les noms des cours pour les ComboBox
     */
    public static List<String> getLesNomsCours() {
        return lesCours.stream()
                .map(c -> c.getNom() + " - " + c.getNiveau())
                .collect(Collectors.toList());
    }

    /**
     * Ajoute un cours
     */
    public static void ajouterCours(Cours cours) {
        lesCours.add(cours);
    }

    // ========== MÉTHODES UTILITAIRES ==========

    /**
     * Retourne toutes les saisons disponibles
     */
    public static List<String> getLesSaisons() {
        return lesMembres.stream()
                .map(Membre::getSaison)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Compte le nombre total de membres
     */
    public static int getNombreTotalMembres() {
        return lesMembres.size();
    }

    /**
     * Compte le nombre de membres du bureau
     */
    public static int getNombreMembresBureau() {
        return (int) lesMembres.stream()
                .filter(Membre::isMembreBureau)
                .count();
    }

    /**
     * Vérifie si un email est déjà utilisé
     */
    public static boolean emailExiste(String email) {
        return lesMembres.stream()
                .anyMatch(m -> m.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Vérifie si un email est déjà utilisé (en excluant un membre spécifique)
     */
    public static boolean emailExiste(String email, int idMembre) {
        return lesMembres.stream()
                .filter(m -> m.getId() != idMembre)
                .anyMatch(m -> m.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Réinitialise toutes les données
     */
    public static void reinitialiserDonnees() {
        lesMembres.clear();
        lesCours.clear();
        prochainIdMembre = 1;
        initialiserDonneesTest();
    }

    // ========== DONNÉES DE TEST ==========

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
        ajouterMembre("Dupont", "Marie", 25, "1999-06-12", "marie.dupont@email.com", "0601020304", "2024-2025", "Jazz - Intermédiaire", false);
        ajouterMembre("Martin", "Pierre", 30, "1994-01-22", "pierre.martin@email.com", "0602030405", "2024-2025", "Classique - Avancé", true);
        ajouterMembre("Bernard", "Sophie", 22, "2002-03-18", "sophie.bernard@email.com", "0603040506", "2023-2024", "", false);
        ajouterMembre("Durand", "Jean", 28, "1996-07-05", "jean.durand@email.com", "0604050607", "2024-2025", "Hip-Hop - Intermédiaire", true);
        ajouterMembre("Moreau", "Emma", 19, "2005-11-09", "emma.moreau@email.com", "0605060708", "2023-2024", "Jazz - Débutant", false);
        ajouterMembre("Petit", "Lucas", 35, "1989-02-25", "lucas.petit@email.com", "0606070809", "2023-2024", "Salsa - Tous niveaux", false);
        ajouterMembre("Roux", "Chloé", 27, "1997-08-14", "chloe.roux@email.com", "0607080910", "2024-2025", "Classique - Intermédiaire", true);
        ajouterMembre("Lefevre", "Thomas", 24, "2000-04-03", "thomas.lefevre@email.com", "0608091011", "2024-2025", "Hip-Hop - Débutant", false);
        ajouterMembre("Garcia", "Ana", 31, "1993-12-17", "ana.garcia@email.com", "0609101112", "2023-2024", "Salsa - Tous niveaux", false);
        ajouterMembre("Dubois", "Julie", 26, "1998-05-30", "julie.dubois@email.com", "0610111213", "2024-2025", "Contemporain - Intermédiaire", false);
        ajouterMembre("Fournier", "Alexandre", 29, "1995-09-21", "alex.fournier@email.com", "0611121314", "2024-2025", "Jazz - Avancé", true);
        ajouterMembre("Lambert", "Camille", 23, "2001-10-02", "camille.lambert@email.com", "0612131415", "2024-2025", "Classique - Débutant", false);

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