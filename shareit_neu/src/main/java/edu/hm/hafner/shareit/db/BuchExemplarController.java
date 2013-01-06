package edu.hm.hafner.shareit.db;

import java.util.Collection;

import edu.hm.hafner.shareit.model.Benutzer;
import edu.hm.hafner.shareit.model.BuchBeschreibung;
import edu.hm.hafner.shareit.model.BuchExemplar;

/**
 * Erzeugt, findet und löscht Buchexemplare.
 * 
 * @author Cookie
 */
public interface BuchExemplarController {
    
    /**
     * Erzeugt ein neues Buchexemplar.
     * 
     * @param beschreibung Beschreibung des Buches
     * @param leiher Leiher des Buchexemplares
     * @param id ID des Buchexemplares
     * @return das neue Buchexemplar
     */
    BuchExemplar create(BuchBeschreibung beschreibung, Benutzer leiher, int id);
    
    BuchExemplar createExemplar(String isbn, String besitzerEmail);
    BuchExemplar rentExemplar(String isbn, String besitzerEmail, String leiherEmail);
    BuchExemplar returnExemplar(String isbn, String leiherEmail);
    /**
     * Liefert alle Buchexemplare zurück.
     * 
     * @return die gefundenen Buchexemplare
     */
    Collection<BuchExemplar> findBuchExemplare();
    
    /**
     * Liefert alle Buchexemplare mit der übergebenen ID zurück.
     * 
     * @param id ID des gesuchten Buchexemplars
     * @return die gefunden Buchexemplare
     */
    Collection<BuchExemplar> findById(int id);
    Collection<BuchExemplar> findByIsbn(String isbn);
    /**
     * Löscht alle Buchexemplare mit der gegebenen ID.
     * 
     * @param id ID des zu löschenden Buchexemplars
     */
    void delete(int id);
    boolean delete(String isbn, String besitzer);
    
    Collection<BuchExemplar> getAllBooks();
}

