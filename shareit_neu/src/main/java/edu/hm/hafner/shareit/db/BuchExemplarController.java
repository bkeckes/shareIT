package edu.hm.hafner.shareit.db;

import java.util.Collection;

import edu.hm.hafner.shareit.model.BuchExemplar;

/**
 * Erzeugt, findet und l�scht Buchexemplare.
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
     * Ein Exemplar wird zur�ck gegeben
     * 
     * @param isbn
     * @param leiherEmail
     * @return BuchExemplar
     */
    BuchExemplar returnExemplar(String isbn, String leiherEmail);
    
    /**
     * Ein Exemplar wird zur�ck gefordert
     * 
     * @param isbn
     * @param besitzer
     * @param leiher
     * @return BuchExemplar
     */
    BuchExemplar reclaimExemplar(String isbn, String besitzer, String leiher);
    
    /**
     * Gibt B�cher �ber die ISBN zur�ck
     * @param isbn
     * @return collection
     */
    Collection<BuchExemplar> findByIsbn(String isbn);
    
    /**
     * Gibt B�cher eines Besitzer zur�ck
     * @param besitzer
     * @return collection
     */
    Collection<BuchExemplar> findByBesitzer(String besitzer);
    
    /**
     * Gibt B�cher zur�ck die von "leiher" ausgeliehen sind
     * @param leiher
     * @return collection
     */
    Collection<BuchExemplar> findByLeiher(String leiher);
    
    /**
     * L�scht ein Buch
     * 
     * @param isbn
     * @param besitzer
     */
    void delete(String isbn, String besitzer);
    /**
     * Gibt alle B�cher zur�ck
     * 
     * @return collection
     */
    Collection<BuchExemplar> getAllBooks();
}

