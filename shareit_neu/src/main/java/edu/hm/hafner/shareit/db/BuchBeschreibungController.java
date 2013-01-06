package edu.hm.hafner.shareit.db;

import java.util.Collection;

import edu.hm.hafner.shareit.model.BuchBeschreibung;

/**
 * Erzeugt, findet und l�scht Buchbeschreibungen.
 * 
 * @author Cookie
 */
public interface BuchBeschreibungController {
    
    /**
     * Stellt ein neues Buch zur Verf�gung.
     * 
     * @param isbn ISBN des Buches
     * @param title Buchtitel
     * @param author Autor des Buches
     * @param emailBesitzer EMail des Buchexemplarbesitzers
     * @return die Buchbeschreibung
     */
    BuchBeschreibung create(String isbn, String title, String author, String emailBesitzer);
    
    BuchBeschreibung createBeschreibung(String isbn, String title, String author);
    
    /**
     * Liefert alle Buchbeschreibungen zur�ck.
     * 
     * @return die gefundenen Buchbeschreibunbgen
     */
    Collection<BuchBeschreibung> findBuchBeschreibung();
    
    /**
     * Liefert alle Buchbeschreibungen mit der �bergebenen ISBN zur�ck.
     * 
     * @param isbn die zu findende ISBN
     * @return die gefundenen Buchbeschreibungen
     */
    Collection<BuchBeschreibung> findByISBN(String isbn);
    
    /**
     * L�scht die Buchbeschreibung zur gegebenen ISBN aus der Datenbank.
     * 
     * @param isbn ISBN der zu l�schenden Buchbeschreibung
     */
    void delete(String isbn);
}

