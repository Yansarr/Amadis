package fr.uga.iut2.genevent;

import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.modele.GenEvent;
import fr.uga.iut2.genevent.util.Persisteur;
import fr.uga.iut2.genevent.vue.JavaFXGUI;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class Main {

    private static Logger LOGGER = Logger.getLogger(JavaFXGUI.class.getPackageName());

    // Récupérarion du gestionnaire de logs.
    private static final LogManager logManager = LogManager.getLogManager();
    // Configuration du logger
    // EditConfiguration > Modify options > add VM options :
    // -Djava.util.logging.config.file=conf/debug-logging.properties
    static{
        try {
            logManager.readConfiguration( new FileInputStream("conf/debug-logging.properties") );
        } catch ( IOException exception ) {
            LOGGER.log( Level.SEVERE, "Cannot read configuration file", exception );
        }
    }

    public static final int EXIT_ERR_LOAD = 2;
    public static final int EXIT_ERR_SAVE = 3;

    public static void main(String[] args) {
        GenEvent genevent = null;

        try {
            genevent = Persisteur.lireEtat();
        }
        catch (ClassNotFoundException | IOException ignored) {
            System.err.println("Erreur irrécupérable pendant le chargement de l'état : fin d'exécution !");
            System.err.flush();
            System.exit(Main.EXIT_ERR_LOAD);
        }

        Controleur controleur = new Controleur(genevent);
        // `Controleur.demarrer` garde le contrôle de l'exécution tant que
        // l'utilisa·teur/trice n'a pas saisi la commande QUITTER.
        controleur.demarrer();

        try {
            Persisteur.sauverEtat(genevent);
        }
        catch (IOException ignored) {
            System.err.println("Erreur irrécupérable pendant la sauvegarde de l'état : fin d'exécution !");
            System.err.flush();
            System.exit(Main.EXIT_ERR_SAVE);
        }
    }
}
