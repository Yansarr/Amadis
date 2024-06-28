package fr.uga.iut2.genevent.controleur;

import fr.uga.iut2.genevent.modele.*;
import fr.uga.iut2.genevent.util.Utilitaire;
import fr.uga.iut2.genevent.vue.IHM;
import fr.uga.iut2.genevent.vue.JavaFXGUI;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controleur {

    private static final Logger LOGGER = Logger.getLogger(JavaFXGUI.class.getPackageName());

    private final GenEvent genevent;
    private final IHM ihm;

    public Controleur(GenEvent genevent) {
        this.genevent = genevent;

        // choisir la classe CLI ou JavaFXGUI
//        this.ihm = new CLI(this);
        this.ihm = new JavaFXGUI(this);
    }

    public void demarrer() {
        this.ihm.demarrerInteraction();
    }

    // dialogue avec l'IHM
    public void afficherSalles() {
        this.ihm.afficherSalles();
    }

    public void afficherRepresentations() {
        this.ihm.afficherRepresentations();
    }

    public void afficherPieces() {
        ihm.afficherPieces();
    }

    public void saisirNouvellePiece() {
        this.ihm.saisirNouvellePiece();
    }

    /**
     * Demande à l'IHM l'affichage de la page de modification pour la pièces ayant comme informations
     * {@link fr.uga.iut2.genevent.vue.IHM.InfosPiece}.
     * @param piece
     */
    public void saisirModifPiece(IHM.InfosPiece piece) {
        this.ihm.saisirModifPiece(piece);
    }

    // récupération des données de la base de données de l'application pour les traduires en format IHM
    /**
     * Récupère toutes les {@link Salle}s de l'application et les traduit en format IHM.
     *
     * @return une liste de {@link IHM.InfosSalle}s correspondant aux {@link Salle}s de l'application.
     */
    public ArrayList<IHM.InfosSalle> recupererInfosAllSAlles() {
        HashMap<String, Salle> salles = this.genevent.getAllSalles();

        ArrayList<IHM.InfosSalle> infosSalles = new ArrayList<>();
        for(Salle salle : salles.values()) {
            infosSalles.add(new IHM.InfosSalle(
                    salle.getNom(),
                    salle.getAdresse(),
                    salle.getVille(),
                    salle.getCapacite(),
                    salle.getPrix()
            ));
        }
        return infosSalles;
    }

    /**
     * Récupère toutes les {@link Piece}s de l'application pour l'{@link Organisateur} connecté et les traduit en format IHM.
     *
     * @return une liste de {@link IHM.InfosPiece}s correspondant aux {@link Piece}s de l'application pour l'{@link Organisateur} connecté.
     */
    public ArrayList<IHM.InfosPiece> recupererInfosAllPieces() {
        HashMap<String, Piece> pieces = this.genevent.getAllPiecesOrganisateurConnecte();

        ArrayList<IHM.InfosPiece> infosPieces = new ArrayList<>();
        for(Piece piece : pieces.values()) {
            infosPieces.add(new IHM.InfosPiece(
                    piece.getNom(),
                    piece.getDuree(),
                    piece.getDescription()
            ));
        }
        return infosPieces;
    }

    /**
     * Récupère toutes les {@link Representation}s de l'application pour l'{@link Organisateur} connecté et les traduit en format IHM.
     *
     * @return une liste de {@link IHM.InfosRepresentation}s correspondant aux {@link Representation}s de l'application, pour l'{@link Organisateur} connecté.
     */
    public ArrayList<IHM.InfosRepresentation> recupererInfosAllRepresentation() {
        HashMap<String, Representation> representations = this.genevent.getAllRepresentationsOrganisateurConnecte();

        ArrayList<IHM.InfosRepresentation> infosRepresentations = new ArrayList<>();
        for (Representation representation : representations.values()) {
            infosRepresentations.add(new IHM.InfosRepresentation(
                    representation.getDate(),
                    representation.isRegieL(),
                    representation.isRegieS(),
                    representation.isSecu(),
                    representation.isPub(),
                    new IHM.InfosPiece(
                            representation.getPiece().getNom(),
                            representation.getPiece().getDuree(),
                            representation.getPiece().getDescription()
                    ),
                    new IHM.InfosSalle(
                            representation.getSalle().getNom(),
                            representation.getSalle().getVille(),
                            representation.getSalle().getAdresse(),
                            representation.getSalle().getCapacite(),
                            representation.getSalle().getPrix()
                    ))
            );
        }
        return infosRepresentations;
    }

    /**
     * Récupère la {@link Salle} ayant pour nom l'identifiant passé en paramètre et la traduit en format IHM.
     *
     * @param nom le nom de la {@link Salle} à récupérer.
     * @return une {@link IHM.InfosSalle} correspondant à la {@link Salle} ayant pour nom l'identifiant passé en paramètre.
     *
     */
    public IHM.InfosSalle recupererInfosSalle(String nom) {
        Salle salle = genevent.getSalleNom(nom);

        if (salle == null) {
            LOGGER.log(Level.SEVERE, "Salle " + nom + " non trouvée");
            return null;
        } else {
            return new IHM.InfosSalle(
                    salle.getNom(),
                    salle.getAdresse(),
                    salle.getVille(),
                    salle.getCapacite(),
                    salle.getPrix()
            );
        }
    }

    /**
     * Récupère la {@link Piece} ayant pour nom l'identifiant passé en paramètre et la traduit en format IHM.
     *
     * @param nom le nom de la {@link Piece} à récupérer.
     * @return une {@link IHM.InfosPiece} correspondant à la {@link Piece} ayant pour nom l'identifiant passé en paramètre.
     */
    public IHM.InfosPiece recupererInfosPiece(String nom) {
        Piece piece = genevent.getPieceNomOrganisateurConnecte(nom);

        return new IHM.InfosPiece(
                piece.getNom(),
                piece.getDuree(),
                piece.getDescription()
        );
    }

    // enregistrement des données entrées en IHM dans l'application

    /**
     * Enregistre et traduit les informations d'une {@link fr.uga.iut2.genevent.vue.IHM.InfosInscription}
     * en un nouvel {@link Organisateur} pour l'application, si il n'existe pas déjà.
     *
     * @param data les {@link fr.uga.iut2.genevent.vue.IHM.InfosInscription} à enregistrer.
     */
    public void enregistrerOrganisateur(IHM.InfosInscription data) {
        boolean nouvelOrganisateur = this.genevent.ajouteOrganisateur(
                data.email,
                data.nomComplet,
                data.mdp
        );
        if (nouvelOrganisateur) {
            this.ihm.informerUtilisateur(
                    "Nouvel·le utilisa·teur/trice : " + data.nomComplet +  " <" + data.email + ">",
                    true
            );
        } else {
            this.ihm.informerUtilisateur(
                    "L'utilisa·teur/trice " + data.email + " est déjà connu·e de GenEvent.",
                    false
            );
        }
    }

    /**
     * Enregistre et traduit une {@link IHM.InfosRepresentation} de l'IHM en
     * {@link Representation} pour l'{@link Organisateur} connecté.
     *
     * @param data la {@link IHM.InfosRepresentation}s à enregistrer.
     */
    public void enregistrerRepresentation(IHM.InfosRepresentation data) {
        boolean nouvelleRepresentation = this.genevent.getOrganisateurConnecte().ajouterRepresentation(
                data.date,
                data.regieL,
                data.regieS,
                data.secu,
                data.pub,
                getOrganisateurConnecte().getPiece(data.piece.nom),
                this.genevent.getSalleNom(data.salle.nom)
        );

        if (nouvelleRepresentation) {
            this.genevent.getSalleNom(data.salle.nom).louer(data.date);
            this.ihm.informerUtilisateur(
                    "Nouvel·le representation : " + data.date + " à " + data.salle,
                    true
            );
        } else {
            this.ihm.informerUtilisateur(
                    "La representation " + data.date + " à " + data.salle + " est déjà connu·e de GenEvent pour cette utilisateur.",
                    false
            );
        }
    }

    /**
     * Enregistre et traduit une {@link IHM.InfosSalle} de l'IHM en {@link Salle} pour l'{@link Organisateur} connecté.
     *
     * @param data les {@link IHM.InfosPiece}s à enregistrer.
     */
    public void enregistrerNouvellePiece(IHM.InfosPiece data) {
        boolean nouvellePiece = this.genevent.ajoutePiece(
                data.nom,
                data.duree,
                data.description
        );
        if (nouvellePiece) {
            this.ihm.informerUtilisateur(
                    "Nouvel·le piece : " + data.nom + " (" + data.duree + " minutes)",
                    true
            );
        } else {
            this.ihm.informerUtilisateur(
                    "La piece " + data.nom + " existe déjà pour cette utilisateur.",
                    false
            );
        }
    }

    // à ranger proprement

    public Organisateur getOrganisateurConnecte() {
        LOGGER.log(Level.INFO, "Utilisateur connecté : " + genevent.getOrganisateurConnecte().getNomComplet());
        return this.genevent.getOrganisateurConnecte();
    }

    public boolean verifierConnexion(IHM.InfosConnexion infos){
        boolean verifConnexion = this.genevent.connexion(infos.email, infos.mdp);
        if (verifConnexion){
            this.ihm.afficherMenu();
            return true;
        }else{
            return false;
        }

    }

    public void supprimerPiece(IHM.InfosPiece infos){

        boolean supprimerPiece = this.genevent.getOrganisateurConnecte().supprimerPiece(infos.nom);

        if (supprimerPiece) {
            this.ihm.informerUtilisateur(
                    "Pièce supprimée: " + infos.nom,
                    true
            );
        } else {
            this.ihm.informerUtilisateur(
                    "Pièce non-supprimée " + infos.nom,
                    false
            );
        }
    }
    // METHODES POUR LES REPRESENTATIONS

    public void deconnexion() {
        this.genevent.deconnexion();
        this.ihm.saisirConnexion();
    }


    /**
     * Permet de vérifier l'indisponibilité de l'utilisateur connecté pour la date donnée.
     *
     * @param date la date à vérifier.
     * @return vrai si l'organisateur connecté a une représentation à cette date, faux sinon.
     */
    public boolean checkOrganisateurIndisponible(LocalDate date) {

        if (this.getOrganisateurConnecte().getDatesPrises().contains(date)) {
            LOGGER.log(Level.SEVERE, "Organisateur " + this.getOrganisateurConnecte().getNomComplet() + " est déjà pris pour cette date");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Permet de vérifier l'indisponibilité d'une salle pour une date donnée.
     *
     * @param date la date à vérifier.
     * @param nomSalle  le nom de la {@link Salle} à vérifier.
     * @return vrai si la salle n'est pas disponible à @date et faux sinon.
     */
    public boolean checkSalleIndisponible(LocalDate date, String nomSalle) {

        boolean salleIndisponible = false;

        if (this.genevent.getSalleNom(nomSalle).getHistoriqueDatesLocations() != null) {

            for (LocalDate dateStockee : genevent.getSalleNom(nomSalle).getHistoriqueDatesLocations()) {
                System.out.println(dateStockee + "//" + date + "ET" + nomSalle + "//" + genevent.getSalleNom(nomSalle).getNom());
                if (dateStockee.isEqual(date)) {
                    LOGGER.log(Level.SEVERE, "Salle " + nomSalle + " est déjà prise pour cette date");
                    salleIndisponible = true;
                }
            }
        }

        return salleIndisponible;
    }

    /**
     * Permet de vérifier que la date choisie est valide.
     *
     * @param date la date à vérifier.
     * @return true si la date est antérieure à la date actuelle + 7 jours, faux sinon.
     */
    public boolean checkDateSeptJours(LocalDate date) {

        if (date.isBefore(LocalDate.now().plusDays(7))) {
            LOGGER.log(Level.SEVERE, "Tentative de réservation dans moins d'une semaine / d'annulation d'une réservation dans moins d'une semaine");
            return true;
        }
        return false;
    }

    public void modifierPiece(String nom, IHM.InfosPiece data) {
        if(!this.genevent.getAllPiecesOrganisateurConnecte().containsKey(data.nom) | (nom.equals(Utilitaire.capitalize(data.nom)))){
            this.genevent.getAllPiecesOrganisateurConnecte().get(nom).setNom(data.nom);
            this.genevent.getAllPiecesOrganisateurConnecte().get(nom).setDescription(data.description);
            this.genevent.getAllPiecesOrganisateurConnecte().get(nom).setDuree(data.duree);
            Piece piece = this.genevent.getAllPiecesOrganisateurConnecte().get(nom);
            this.genevent.getAllPiecesOrganisateurConnecte().remove(nom);
            this.genevent.getAllPiecesOrganisateurConnecte().put(Utilitaire.capitalize(data.nom), piece);
        } else {
            this.ihm.informerUtilisateur("Une pièce a déjà ce nom dans la base de données.", false);
        }

    }

    public void annulerRepresentation(IHM.InfosRepresentation reps) {
        String key = reps.date.toString() + " : " + reps.salle.nom;
        Representation representation = this.genevent.getAllRepresentationsOrganisateurConnecte().get(key);
        representation.getSalle().getRepresentations().remove(representation);
        getOrganisateurConnecte().getRepresentations().remove(key);
        this.genevent.getSalleNom(reps.salle.nom).annulerLocation(reps.date);
        this.ihm.informerUtilisateur("La représentation a bien été annulée.", true);
    }

    public boolean verifierPieceUtilisee(IHM.InfosPiece piece) {
        for(Representation representation : genevent.getAllRepresentationsOrganisateurConnecte().values()){
            if (representation.getPiece().getNom().equals(piece.nom)){
                return true;
            }
        } return false;
    }

    public boolean verifierOrganisateurConnecteAPiece() {
        return !(genevent.getAllPiecesOrganisateurConnecte()==null || genevent.getAllPiecesOrganisateurConnecte().isEmpty());
    }
}
