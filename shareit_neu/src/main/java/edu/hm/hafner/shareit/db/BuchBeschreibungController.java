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
     * Liefert alle Buchbeschreibungen mit der �bergebenen ISBN zur�ck.
     * 
     * @param isbn die zu findende ISBN
     * @return die gefundenen Buchbeschreibungen
     */
    Collection<BuchBeschreibung> findByISBN(String isbn);
    
    /**
     * Hier wird anhand der Buchbeschreibung eine Liste der ISBN zur�ck gegeben.
     * Da Titel und Autor in verschiedenen B�chern gleich sein k�nnen muss es eine Liste sein.
     * 
     * Die Beschreibung wird so �berpr�ft:
     * -wurde eine ISBN angegeben?:
     *      der Fall ist klar. Einfach ISBN zur�ck geben
     * -wurde ein Titel angegeben?:
     *      Alle ISBN zur�ck geben die zu diesem Titel geh�ren
     * -wurde ein Autor angegeben?:
     *      Alle ISBN zur�ck geben die zu diesem Autor geh�ren
     * @param beschreibung
     * @return isbnCollection
     */
    Collection<String> findISBNByBuchBeschreibung(BuchBeschreibung beschreibung);
    
    /**
     * 
     * l�scht eine Buchbeschreibung
     * @param isbn
     * @return buchbeschreibung
     */
    void deleteBeschreibung(String isbn);
}

