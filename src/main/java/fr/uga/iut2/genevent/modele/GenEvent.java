package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.util.Utilitaire;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class GenEvent implements Serializable {

    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation

    // Attributs
    private final HashMap<String, Organisateur> organisateurs;
    private Organisateur organisateurConnecte;
    private HashMap<String, Salle> salles;

    // Constructeur
    public GenEvent() {
        this.organisateurs = new HashMap<>();
        this.salles = new HashMap<String, Salle>();
        this.initSalles();
        // ligne a dé-commenter si la première page ouverte n'est pas celle de connexion.
//        this.organisateurConnecte = new Organisateur();
    }


    public Organisateur getOrganisateurConnecte() {
        return organisateurConnecte;
    }

    public boolean ajouteOrganisateur(String email, String nomComplet, String mdp) {
        if (this.organisateurs.containsKey(this.organisateurs.containsKey(email))) {
            return false;
        } else {
            this.organisateurs.put(email, new Organisateur(email, nomComplet, mdp));
            organisateurConnecte = organisateurs.get(email);
            return true;
        }
    }


    public boolean ajoutePiece(String nom, int duree, String description) {
        return this.organisateurConnecte.ajouterPiece(nom, duree, description);
    }

    // CONNEXION
    public boolean connexion(String email, String mdp){
        if (organisateurs.containsKey(email)){
            try {
                if (organisateurs.get(email).getMdp().equals(Utilitaire.hash(mdp))){
                    organisateurConnecte = organisateurs.get(email);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void deconnexion() {
        this.organisateurConnecte = null; // attention, possibilité de NullPointerException
    }

    // SALLES
    /**
     * Initialise les salles du GenEvent.
     */
    public void initSalles(){
        salles = new HashMap<String, Salle>();
        salles.put("Théatre Molière", new Salle("Théatre Molière", "Grenoble", "25 Rue de la Libération", "38000", 300, true, true, 600));
        salles.put("La Crique", new Salle("La Crique", "Meylan", "12 Route Bleue", "38240", 250, false, true, 400));
        salles.put("Salle Polyvalente de la Poupe", new Salle("Salle Polyvalente de la Poupe", "Saint-Egrève", "45 Avenue des Lilas", "38120", 50, false, false, 150));
        salles.put("Les Balivernes", new Salle("Les Balivernes", "Echirolles", "50 rue Louis Pasteur", "38130", 100, true, false, 100));
    }

    // récupération des éléments de l'application.
    // - pour tous les éléments
    public HashMap<String, Salle> getAllSalles() {
        return salles;
    }

    public HashMap<String, Piece> getAllPiecesOrganisateurConnecte() {
        return this.organisateurConnecte.getPieces();
    }

    public HashMap<String, Representation> getAllRepresentationsOrganisateurConnecte() {
        return this.organisateurConnecte.getRepresentations();
    }

    // - pour un élément spécifique
    public Salle getSalleNom(String nom) {
        return this.salles.get(nom);
    }

    public Piece getPieceNomOrganisateurConnecte(String nom) {
        return this.getAllPiecesOrganisateurConnecte().get(nom);
    }

    public Representation getRepresentationNomOrganisateurConnecte(String nom) {
        return this.getAllRepresentationsOrganisateurConnecte().get(nom);
    }

    public ArrayList<Organisateur> getOrganisateurs() {
        return new ArrayList<>(this.organisateurs.values());
    }
}
