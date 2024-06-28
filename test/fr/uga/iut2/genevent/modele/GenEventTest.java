package fr.uga.iut2.genevent.modele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenEventTest {

    @Test
    void ajoutePiece() {

        GenEvent genevent = new GenEvent();

        boolean nouvellePiece = genevent.ajoutePiece("nom",1, "description");

        assertTrue(nouvellePiece, "La pièce doit être ajoutée.");
    }
}