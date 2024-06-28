package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Représente une représentation prévue.
 * L'ID est n'est jamais spécifié manuellement, il est généré automatiquement de manière incrémentale.
 * Aucun élément n'est modifiable une fois créé.
 */
public class Representation implements Serializable {

    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation

    public static final AtomicInteger count = new AtomicInteger(0); // Integer spécial dans le but est de s'auto update

    private int idRepres;
    private LocalDate date;
    private boolean regieL;
    private boolean regieS;
    private boolean secu;
    private boolean pub;
    private Piece piece;
    private Organisateur organisateur;
    private Salle salle;

    // Constructeur

    public Representation(Organisateur org, Salle salle, LocalDate date, boolean rL, boolean rS, boolean secu, boolean pub, Piece piece) {
        this.idRepres = count.incrementAndGet();
        this.organisateur = org;
        this.salle = salle;
        this.date = date;
        this.regieL = rL;
        this.regieS = rS;
        this.secu = secu;
        this.pub = pub;
        this.piece = piece;

    }


    // Getters

    public int getIdRepres() {
        return idRepres;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isRegieL() {
        return regieL;
    }

    public boolean isRegieS() {
        return regieS;
    }

    public boolean isSecu() {
        return secu;
    }

    public boolean isPub() {
        return pub;
    }

    public Piece getPiece() {
        return piece;
    }

    public Organisateur getOrg() {
        return organisateur;
    }

    public Salle getSalle() {
        return salle;
    }

    // Setters

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    // Autres méthodes et procédures

    public void ajouterOrganisateur(Organisateur organisateur) {
        if (this.organisateur == null) {
            this.organisateur = organisateur;
        }
    }

}

