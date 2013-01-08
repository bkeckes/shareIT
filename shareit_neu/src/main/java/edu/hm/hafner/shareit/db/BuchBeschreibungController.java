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
     * Erstellt eine neue Beschreibung
     * 
     * @param isbn
     * @param title
     * @param author
     * @return beschreibung
     */
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
    Collection<String> findISBNByBuchBeschreibung(BuchBeschreibung beschreibung);
}

