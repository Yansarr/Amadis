package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.util.Utilitaire;

import java.io.Serializable;

/**
 * Représente une Piece avec son nom, sa durée et sadescription.
 */
public class Piece implements Serializable {

    // --- Attributs -------------------------------------------------------
    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation

    private String nom;
    private int duree;
    private String description;
    private Organisateur organisateur;

    // --- Constructeurs ----------------------------------------------------
    public Piece(String nom, int duree, String description) {
        this.setNom(nom);
        this.duree = duree;
        this.description = description;
    }

    // --- Getters ----------------------------------------------------------
    public String getNom() {
        return this.nom;
    }

    public int getDuree() {
        return this.duree;
    }

    public String getDescription() {
        return this.description;
    }

    // --- Setters ----------------------------------------------------------
    public void setNom(String nom) {
        this.nom = Utilitaire.capitalize(nom);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }
    // --- Autres méthodes et procédures ---

    public void ajouterOrganisateur(Organisateur organisateur) {
        if (this.organisateur == null) {
            this.organisateur = organisateur;
        }
    }


}
