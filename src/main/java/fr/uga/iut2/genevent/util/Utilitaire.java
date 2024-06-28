package fr.uga.iut2.genevent.util;

import fr.uga.iut2.genevent.modele.Salle;
import javafx.collections.ObservableList;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * Classe utilitaire pour tout le projet, n'a pas vocation à être instanciée.
 * Cette classe contient des méthodes statiques qui sont utiles pour tout le projet.
 */
public class Utilitaire {

    /**
     * Méthode statique qui permet de mettre en majuscule la première lettre d'un String.
     * @param chaine
     * @return
     */
    public static String capitalize(String chaine) {
        return chaine.substring(0, 1).toUpperCase() + chaine.substring(1);
    }

    /**
     * Méthode de hachage de String, utilisée pour les mots de passe.
     * @param str
     * @return
     * @throws Exception
     */
    public static String hash(String str) throws Exception {

        MessageDigest msg = MessageDigest.getInstance("SHA-256");
        byte[] hash = msg.digest(str.getBytes(StandardCharsets.UTF_8));

        StringBuilder s = new StringBuilder();
        for (byte b : hash) {
            s.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return s.toString();
    }

    public static Salle rechIdSalle(ObservableList<Salle> listSalle, int idSalle) {
        for (Salle s : listSalle){
            if(s.getIdSalle()==idSalle){
                return s;
            }
        }
        return null;
    }
}
