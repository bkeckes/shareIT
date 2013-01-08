package edu.hm.hafner.shareit.db;

import java.util.Collection;

import edu.hm.hafner.shareit.model.BuchBeschreibung;

/**
 * Erzeugt, findet und löscht Buchbeschreibungen.
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
     * Liefert alle Buchbeschreibungen mit der übergebenen ISBN zurück.
     * 
     * @param isbn die zu findende ISBN
     * @return die gefundenen Buchbeschreibungen
     */
    Collection<BuchBeschreibung> findByISBN(String isbn);
    
    /**
     * Hier wird anhand der Buchbeschreibung eine Liste der ISBN zurück gegeben.
     * Da Titel und Autor in verschiedenen Büchern gleich sein können muss es eine Liste sein.
     * 
     * Die Beschreibung wird so überprüft:
     * -wurde eine ISBN angegeben?:
     *      der Fall ist klar. Einfach ISBN zurück geben
     * -wurde ein Titel angegeben?:
     *      Alle ISBN zurück geben die zu diesem Titel gehören
     * -wurde ein Autor angegeben?:
     *      Alle ISBN zurück geben die zu diesem Autor gehören
     * @param beschreibung
     * @return isbnCollection
     */
    Collection<String> findISBNByBuchBeschreibung(BuchBeschreibung beschreibung);
    
    /**
     * 
     * löscht eine Buchbeschreibung
     * @param isbn
     * @return buchbeschreibung
     */
    void deleteBeschreibung(String isbn);
}

