package fr.uga.iut2.genevent.modele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    @Test
    void setNom() {

        Piece p1 = new Piece("nom", 1, "description");

        assertEquals("Nom", p1.getNom(), "Les pièces doivent être enregistrées avec la première lettre de leur nom en majuscule.");
    }
}