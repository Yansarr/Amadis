package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Salle implements Serializable {

    // Ensemble des attrbuts de Salle :
    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation

    private int idSalle;
    private String nom;
    private String ville;
    private String adresse;
    private String codePostal;
    private int capacite;
    private boolean bar;
    private boolean projecteur;
    private double tarifJournalier;
    private ArrayList<Representation> representations = new ArrayList<>();
    public static final AtomicInteger count = new AtomicInteger(0);
    public ArrayList<LocalDate> historiqueDatesLocations = new ArrayList<>();

    // Constructeur de Salle :

    public Salle (String nom, String ville, String adresse, String codePostal, int capacite, boolean bar, boolean projecteur, double tarifJournalier) {
        this.nom=nom;
        this.ville=ville;
        this.capacite=capacite;
        this.adresse=adresse;
        this.codePostal=codePostal;
        this.bar=bar;
        this.projecteur=projecteur;
        this.tarifJournalier=tarifJournalier;
        this.idSalle=count.incrementAndGet();

    }

    // Getters des attributs :

    public int getIdSalle() {
        return idSalle;
    }

    public String getVille() {
        return ville;
    }

    public double getPrix() {
        return tarifJournalier;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public ArrayList<Representation> getRepresentations() {
        return representations;
    }

    public double getTarifJournalier() {
        return tarifJournalier;
    }


    public boolean isBar() {
        return bar;
    }

    public boolean isProjecteur() {
        return projecteur;
    }

    public ArrayList<LocalDate> getHistoriqueDatesLocations() {

        return historiqueDatesLocations; // Faire attention au cas où l'ArrayList est vide
    }


    // Setters que pour nom et tarifJournalier (Les deux seuls qui peuvent être modifiés) :

    public void setNom(String nom) {
        this.nom = nom;
    }

     public void setTarifJournalier(double tarifJournalier) {
        this.tarifJournalier = tarifJournalier;
    }

    // Méthodes  :

    public void louer(LocalDate date) {
        this.historiqueDatesLocations.add(date);
    }

    public void annulerLocation(LocalDate date) {
        this.historiqueDatesLocations.remove(date);
    }

    public String toString() {
        return (nom);
    }

}
