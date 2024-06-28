package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.modele.Salle;

import java.time.LocalDate;
import java.util.Collection;


public abstract class IHM {

    /**
     * Classe conteneur pour les informations des
     * {@link fr.uga.iut2.genevent.modele.Salle}s à afficher.
     *
     * <ul>
     * <li>Tous les attributs sont `public` par commodité d'accès.</li>
     * <li>Tous les attributs sont `final` pour ne pas être modifiables.</li>
     * </ul>
     */
    public static class InfosSalle {
        public final String nom;
        public final String ville;
        public final String adresse;
        public final int capacite;
        public final double prix;

        public InfosSalle(final String nom, final String ville, final String adresse, final int capacite, final double prix) {
            this.nom = nom;
            this.ville = ville;
            this.adresse = adresse;
            this.capacite = capacite;
            this.prix = prix;
        }


    }

    /**
     * Classe conteneur pour les informations saisies pour l'inscription d'un
     * {@link fr.uga.iut2.genevent.modele.Organisateur}.
     *
     * <ul>
     * <li>Tous les attributs sont `public` par commodité d'accès.</li>
     * <li>Tous les attributs sont `final` pour ne pas être modifiables.</li>
     * </ul>
     */
    public static class InfosInscription {
        public final String email;
        public final String nomComplet;
        public final String mdp;

        public InfosInscription(String email, String nomComplet, String mdp) {
            this.email = email;
            this.nomComplet = nomComplet;
            this.mdp = mdp;
        }
    }

    /**
     * Classe conteneur pour les informations saisies pour la connexion d'un
     * {@link fr.uga.iut2.genevent.modele.Organisateur}.
     *
     * <ul>
     * <li>Tous les attributs sont `public` par commodité d'accès.</li>
     * <li>Tous les attributs sont `final` pour ne pas être modifiables.</li>
     * </ul>
     */
    public static class InfosConnexion {
        public final String email;
        public final String mdp;

        public InfosConnexion(String email, String mdp) {
            this.email = email;
            this.mdp = mdp;
        }
    }

    /**
     * Classe conteneur pour les informations saisies pour une nouvelle
     * {@link fr.uga.iut2.genevent.modele.Piece}.
     *
     * <ul>
     * <li>Tous les attributs sont `public` par commodité d'accès.</li>
     * <li>Tous les attributs sont `final` pour ne pas être modifiables.</li>
     * </ul>
     */
    public static class InfosPiece {
        public final String nom;
        public final int duree;
        public final String description;

        public InfosPiece(final String nom, final int duree, final String description) {
            this.nom = nom;
            this.duree = duree;
            this.description = description;
        }
    }

    /**
     * Classe conteneur pour les informations d'une
     * {@link fr.uga.iut2.genevent.modele.Salle}.
     *
     * <ul>
     * <li>Tous les attributs sont `public` par commodité d'accès.</li>
     * <li>Tous les attributs sont `final` pour ne pas être modifiables.</li>
     * </ul>
     */
    public static class InfosRepresentation {

        public final LocalDate date;
        public final boolean regieL;
        public final boolean regieS;
        public final boolean secu;
        public final boolean pub;
        public InfosPiece piece;
        public InfosSalle salle;

        public InfosRepresentation(final LocalDate date, final boolean regieL, final boolean regieS, final boolean secu, final boolean pub, final InfosPiece piece, final InfosSalle salle) {
            this.date = date;
            this.regieL = regieL;
            this.regieS = regieS;
            this.secu = secu;
            this.pub = pub;
            this.piece = piece;
            this.salle = salle;
        }
    }

    /**
     * Rend actif l'interface Humain-machine.
     *
     * L'appel est bloquant : le contrôle est rendu à l'appelant une fois que
     * l'IHM est fermée.
     *
     */
    public abstract void demarrerInteraction();

    /**
     * Affiche un message d'information à l'attention de l'utilisa·teur/trice.
     *
     * @param msg Le message à afficher.
     *
     * @param succes true si le message informe d'une opération réussie, false
     *     sinon.
     */
    public abstract void informerUtilisateur(final String msg, final boolean succes);

    // fonction de saisie d'information

    /**
     * Invite l'utilisateur à saisir les informations nécessaires pour une
     * {@link fr.uga.iut2.genevent.modele.Piece}.
     *
     */
    public abstract void saisirNouvellePiece();

    /**
     * Invite l'utilisateur à saisir les informations nécessaires à modifier la {@link fr.uga.iut2.genevent.modele.Piece}.
     */
    public abstract void saisirModifPiece(InfosPiece piece);

    /**
     * Invite l'utilisateur à saisir les informations nécessaires pour se connecter.
     *
     */
    public abstract void saisirConnexion();

    /**
     * Invite l'utilisateur à saisir les informations nécessaires pour s'inscrire.
     *
     */
    public abstract void saisirInscription();

    // fonction d'affichage d'information
    /**
     * Affiche le menu de l'application.
     */
    public abstract void afficherMenu();

    /**
     * Affiche les informations des {@link fr.uga.iut2.genevent.modele.Salle}s contenues
     * dans la collection passée en paramètre.
     *
     */
    public abstract void afficherSalles();

    /**
     * Affiche les informations des {@link fr.uga.iut2.genevent.modele.Representation}s
     * contenues dans la collection passée en paramètre.
     */
    public abstract void afficherRepresentations();

    /**
     * Affiche les informations des {@link fr.uga.iut2.genevent.modele.Piece}s
     * contenues dans la collection passée en paramètre.
     */
    public abstract void afficherPieces();

    /**
     * Supprime une pièce de l'{@link fr.uga.iut2.genevent.modele.Organisateur} connecté.
     */
    public abstract void supprimerPiece(InfosPiece piece);

}
