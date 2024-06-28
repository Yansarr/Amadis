package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.util.Utilitaire;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Organisateur implements Serializable {

    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation

    // --- Attributs ---
    private String mail; // {unique}
    private String nomTroupe;
    private String nomComplet;
    private String numTel; // {unique}
    private String mdp;
    private HashMap<String, Representation> representations= new HashMap<>();
    private HashMap<String, Piece> pieces = new HashMap<>();
    // TODO: 22/06/2022 Pour les {unique}, à gérer dans notre classe Application (GenEvent ou autre)
    //                  En vérifiant lors de la création d'un utilisateur qu'aucun autre ne possède
    //                  la même adresse mail/numéro de téléphone.
    // Dans l'implémentation d'Etienne pour la création d'un utilisateur, l'adresse mail unique est vérifiée.
    // Il n'y a pas d'implémentations pour le numéro de téléphone.

    // --- Constructeurs ---
    // créer un organisateur par défaut (tant que l'étape de création de compte/connexion n'est pas implémentée)

    /**
     * Constructeur d'un organisateur par défaut.
     * Ayant comme attributs les valeurs suivantes :
     * - mail : "default@gmail.com"
     * - nomComplet : "defaultName"
     * - mdp : "defaultMdp"
     */
    public Organisateur() {
        this.mail = "default@email.com";
        this.nomComplet = "defaultName";
        this.mdp = "defaultMdp";
    }

    public Organisateur(String mail, String nomComplet, String mdp) {
        this.mail = mail;
        this.nomComplet = nomComplet;
        this.mdp = mdp;
    }

    // --- Getters ---

    public String getMdp() {
        return mdp;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public HashMap<String, Piece> getPieces() {
        return pieces;
    }

    // --- Setters ---



    // --- Autre méthodes et procédures ---

    public boolean aPiece(String nomPiece) {
        return this.pieces.containsKey(nomPiece);
    }

    public boolean aRepresentation(LocalDate date, Salle salle) {
        return this.representations.containsKey(date.toString() + ":" + salle.getNom());
    }

    public boolean ajouterPiece(String nom, int duree, String description) {
        nom = Utilitaire.capitalize(nom);
        if (this.aPiece(nom)) { // l'organisateur possède déjà cette pièce
            return false;
        } else {
            Piece piece = new Piece(nom, duree, description);
            this.pieces.put(nom, piece);
            piece.ajouterOrganisateur(this);
            return true;
        }
    }
    public boolean supprimerPiece(String nom) {
        if (this.aPiece(nom)) {
            this.pieces.remove(nom);
            System.out.println("supprime dans organisateur");
            return true;
        }else{
            System.out.println("L'utilisateur ne possède pas cette pièce");
            return false;
        }
    }

    public boolean ajouterRepresentation (LocalDate date,boolean regieL, boolean regieS, boolean secu, boolean pub, Piece piece, Salle salle){
        if (this.aRepresentation(date, salle)) { // l'organisateur possède déjà cette représentation
            return false;
        } else {
            Representation representation = new Representation(this, salle, date, regieL, regieS, secu, pub, piece);
            this.representations.put(date.toString() + " : " + salle.getNom(), representation);
            representation.ajouterOrganisateur(this);
            return true;
        }
    }

    public Piece getPiece (String nom){
        return this.pieces.get(nom);
    }

    public HashMap<String, Representation> getRepresentations() {
        return representations;
    }

    public ArrayList<LocalDate> getDatesPrises() {

        ArrayList<LocalDate> datesPrises = new ArrayList<>();

        for (Representation rep : this.representations.values()) {
            datesPrises.add(rep.getDate());
        }

        return datesPrises;
    }
}
