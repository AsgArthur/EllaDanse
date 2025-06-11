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
     * Ajoute un nouveau membre AVEC TÉLÉPHONE OBLIGATOIRE
     */
    public static void ajouterMembre(String nom, String prenom, int age, String email, String telephone,
                                     String saison, String cours, boolean membreBureau) {
        // Validation du téléphone obligatoire
        if (telephone == null || telephone.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone est obligatoire");
        }

        // Validation des autres champs obligatoires
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }
        if (saison == null || saison.trim().isEmpty()) {
            throw new IllegalArgumentException("La saison est obligatoire");
        }
        if (cours == null || cours.trim().isEmpty()) {
            throw new IllegalArgumentException("Le cours est obligatoire");
        }

        // Validation du format du téléphone (optionnel)
        if (!isValidTelephone(telephone.trim())) {
            throw new IllegalArgumentException("Le format du numéro de téléphone n'est pas valide");
        }

        // Vérifier que l'email n'existe pas déjà
        if (emailExiste(email.trim())) {
            throw new IllegalArgumentException("Cette adresse email est déjà utilisée");
        }

        Membre nouveau = new Membre(prochainIdMembre++, nom.trim(), prenom.trim(), age,
                email.trim(), telephone.trim(), saison.trim(), cours.trim(), membreBureau);
        lesMembres.add(nouveau);
    }

    /**
     * Ajoute un membre existant (avec validation)
     */
    public static void ajouterMembre(Membre membre) {
        if (membre == null) {
            throw new IllegalArgumentException("Le membre ne peut pas être null");
        }

        // Validation du téléphone obligatoire
        if (membre.getTelephone() == null || membre.getTelephone().trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone est obligatoire");
        }

        // Validation du format du téléphone
        if (!isValidTelephone(membre.getTelephone().trim())) {
            throw new IllegalArgumentException("Le format du numéro de téléphone n'est pas valide");
        }

        // Vérifier que l'email n'existe pas déjà
        if (emailExiste(membre.getEmail(), membre.getId())) {
            throw new IllegalArgumentException("Cette adresse email est déjà utilisée");
        }

        if (membre.getId() == 0) {
            membre.setId(prochainIdMembre++);
        }
        lesMembres.add(membre);
    }

    /**
     * Modifie un membre existant AVEC TÉLÉPHONE OBLIGATOIRE
     */
    public static void modifierMembre(int id, String nom, String prenom, int age, String email, String telephone,
                                      String saison, String cours, boolean membreBureau) {
        Membre membre = getMembreById(id);
        if (membre != null) {
            // Validation du téléphone obligatoire
            if (telephone == null || telephone.trim().isEmpty()) {
                throw new IllegalArgumentException("Le numéro de téléphone est obligatoire");
            }

            // Validation du format du téléphone
            if (!isValidTelephone(telephone.trim())) {
                throw new IllegalArgumentException("Le format du numéro de téléphone n'est pas valide");
            }

            // Vérifier que l'email n'existe pas déjà (en excluant ce membre)
            if (emailExiste(email.trim(), id)) {
                throw new IllegalArgumentException("Cette adresse email est déjà utilisée");
            }

            membre.setNom(nom.trim());
            membre.setPrenom(prenom.trim());
            membre.setAge(age);
            membre.setEmail(email.trim());
            membre.setTelephone(telephone.trim());
            membre.setSaison(saison.trim());
            membre.setCours(cours.trim());
            membre.setMembreBureau(membreBureau);
        } else {
            throw new IllegalArgumentException("Membre avec l'ID " + id + " introuvable");
        }
    }

    /**
     * Supprime un membre
     */
    public static boolean supprimerMembre(int id) {
        Membre membre = getMembreById(id);
        if (membre != null) {
            return lesMembres.remove(membre);
        }
        return false;
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
     * Vérifie si un numéro de téléphone est déjà utilisé
     */
    public static boolean telephoneExiste(String telephone) {
        return lesMembres.stream()
                .anyMatch(m -> m.getTelephone().equals(telephone));
    }

    /**
     * Vérifie si un numéro de téléphone est déjà utilisé (en excluant un membre spécifique)
     */
    public static boolean telephoneExiste(String telephone, int idMembre) {
        return lesMembres.stream()
                .filter(m -> m.getId() != idMembre)
                .anyMatch(m -> m.getTelephone().equals(telephone));
    }

    /**
     * Valide le format d'un numéro de téléphone
     * Accepte : 0123456789, 01.23.45.67.89, 01 23 45 67 89, +33123456789, etc.
     */
    public static boolean isValidTelephone(String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            return false;
        }

        // Nettoyer le numéro (enlever espaces, points, tirets)
        String telephoneNettoye = telephone.replaceAll("[\\s\\.-]", "");

        // Vérifier les formats français courants
        return telephoneNettoye.matches("^(?:(?:\\+33|0)[1-9](?:[0-9]{8}))$") ||  // Format français
                telephoneNettoye.matches("^[0-9]{10}$") ||                          // 10 chiffres
                telephoneNettoye.matches("^\\+[0-9]{10,15}$");                      // Format international
    }

    /**
     * Formate un numéro de téléphone en format français standard
     */
    public static String formaterTelephone(String telephone) {
        if (telephone == null) return "";

        String telephoneNettoye = telephone.replaceAll("[\\s\\.-]", "");

        // Si c'est un numéro français à 10 chiffres commençant par 0
        if (telephoneNettoye.matches("^0[1-9][0-9]{8}$")) {
            return telephoneNettoye.substring(0, 2) + "." +
                    telephoneNettoye.substring(2, 4) + "." +
                    telephoneNettoye.substring(4, 6) + "." +
                    telephoneNettoye.substring(6, 8) + "." +
                    telephoneNettoye.substring(8, 10);
        }

        return telephone; // Retourner tel quel si format non reconnu
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

    // ========== DONNÉES DE TEST AVEC TÉLÉPHONES ==========

    private static void initialiserDonneesTest() {
        // Ajout des cours
        lesCours.add(new Cours("Jazz", "Débutant", "Marie Leclerc", "Lundi 18h-19h30"));
        lesCours.add(new Cours("Jazz", "Intermédiaire", "Marie Leclerc", "Mardi 19h-20h30"));
        lesCours.add(new Cours("Jazz", "Avancé", "Marie Leclerc", "Jeudi 20h-21h30"));

        lesCours.add(new Cours("Classique", "Débutant", "Sophie Martin", "Lundi 17h-18h30"));
        lesCours.add(new Cours("Classique", "Intermédiaire", "Sophie Martin", "Mercredi 18h-19h30"));
        lesCours.add(new Cours("Classique", "Avancé", "Sophie Martin", "Vendredi 19h-20h30"));

        lesCours.add(new Cours("Contemporain", "Débutant", "Lucas Dubois", "Mardi 18h-19h30"));
        lesCours.add(new Cours("Contemporain", "Intermédiaire", "Lucas Dubois", "Jeudi 18h-19h30"));

        lesCours.add(new Cours("Hip-Hop", "Débutant", "Thomas Richard", "Mercredi 17h-18h30"));
        lesCours.add(new Cours("Hip-Hop", "Intermédiaire", "Thomas Richard", "Vendredi 17h-18h30"));

        lesCours.add(new Cours("Salsa", "Tous niveaux", "Carmen Rodriguez", "Samedi 14h-15h30"));

        // Ajout des membres AVEC TÉLÉPHONES
        try {
            ajouterMembre("Dupont", "Marie", 25, "marie.dupont@email.com", "01.23.45.67.89", "2024-2025", "Jazz - Intermédiaire", false);
            ajouterMembre("Martin", "Pierre", 30, "pierre.martin@email.com", "01.34.56.78.90", "2024-2025", "Classique - Avancé", true);
            ajouterMembre("Bernard", "Sophie", 22, "sophie.bernard@email.com", "01.45.67.89.01", "2023-2024", "Contemporain - Débutant", false);
            ajouterMembre("Durand", "Jean", 28, "jean.durand@email.com", "01.56.78.90.12", "2024-2025", "Hip-Hop - Intermédiaire", true);
            ajouterMembre("Moreau", "Emma", 19, "emma.moreau@email.com", "01.67.89.01.23", "2024-2025", "Jazz - Débutant", false);
            ajouterMembre("Petit", "Lucas", 35, "lucas.petit@email.com", "01.78.90.12.34", "2023-2024", "Salsa - Tous niveaux", false);
            ajouterMembre("Roux", "Chloé", 27, "chloe.roux@email.com", "01.89.01.23.45", "2024-2025", "Classique - Intermédiaire", true);
            ajouterMembre("Lefevre", "Thomas", 24, "thomas.lefevre@email.com", "01.90.12.34.56", "2024-2025", "Hip-Hop - Débutant", false);
            ajouterMembre("Garcia", "Ana", 31, "ana.garcia@email.com", "01.01.23.45.67", "2023-2024", "Salsa - Tous niveaux", false);
            ajouterMembre("Dubois", "Julie", 26, "julie.dubois@email.com", "01.12.34.56.78", "2024-2025", "Contemporain - Intermédiaire", false);
            ajouterMembre("Fournier", "Alexandre", 29, "alex.fournier@email.com", "01.23.45.67.90", "2024-2025", "Jazz - Avancé", true);
            ajouterMembre("Lambert", "Camille", 23, "camille.lambert@email.com", "01.34.56.78.01", "2024-2025", "Classique - Débutant", false);
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur lors de l'initialisation des données de test : " + e.getMessage());
        }
    }
}