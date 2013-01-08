package edu.hm.hafner.shareit.db;

import java.util.Collection;

import edu.hm.hafner.shareit.model.BuchExemplar;

/**
 * Erzeugt, findet und löscht Buchexemplare.
 * 
 * @author Cookie
 */
public interface BuchExemplarController {
    
    /**
     * Erstellt ein BuchExemplar
     * 
     * @param isbn
     * @param besitzerEmail
     * @return BuchExemplar
     */
    BuchExemplar createExemplar(String isbn, String besitzerEmail);
    
    /**
     * Ein Exemplar wird geliehen
     * 
     * @param isbn
     * @param besitzerEmail
     * @param leiherEmail
     * @return BuchExemplar
     */
    BuchExemplar rentExemplar(String isbn, String besitzerEmail, String leiherEmail);
    
    /**
     * Ein Exemplar wird zurück gegeben
     * 
     * @param isbn
     * @param leiherEmail
     * @return BuchExemplar
     */
    BuchExemplar returnExemplar(String isbn, String leiherEmail);
    
    /**
     * Ein Exemplar wird zurück gefordert
     * 
     * @param isbn
     * @param besitzer
     * @param leiher
     * @return BuchExemplar
     */
    BuchExemplar reclaimExemplar(String isbn, String besitzer, String leiher);
    
    /**
     * Gibt Bücher über die ISBN zurück
     * @param isbn
     * @return collection
     */
    Collection<BuchExemplar> findByIsbn(String isbn);
    
    /**
     * Gibt Bücher eines Besitzer zurück
     * @param besitzer
     * @return collection
     */
    Collection<BuchExemplar> findByBesitzer(String besitzer);
    
    /**
     * Gibt Bücher zurück die von "leiher" ausgeliehen sind
     * @param leiher
     * @return collection
     */
    Collection<BuchExemplar> findByLeiher(String leiher);
    
    /**
     * Löscht ein Buch
     * 
     * @param isbn
     * @param besitzer
     */
    void delete(String isbn, String besitzer);
    /**
     * Gibt alle Bücher zurück
     * 
     * @return collection
     */
    Collection<BuchExemplar> getAllBooks();
}

