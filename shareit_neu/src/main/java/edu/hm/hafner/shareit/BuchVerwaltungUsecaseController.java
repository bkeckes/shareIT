package edu.hm.hafner.shareit;

import edu.hm.hafner.shareit.model.Benutzer;
import edu.hm.hafner.shareit.model.BuchBeschreibung;
import edu.hm.hafner.shareit.model.BuchExemplar;

/**
 * Usecase Controller für die Anwendungsfälle, welche die
 * Buchbeschreibungen und Buchexemplare betreffen.
 * 
 * @author Cookie
 */
public interface BuchVerwaltungUsecaseController {
    
    /**
     * Stellt eine neue Buchbeschreibung zur Verfügung.
     * 
     * Wenn schon eine Buchbeschreibung mit den übergebenen Daten
     * existiert, wird dieser ein weiters Buchexemplar dazugezählt.
     * 
     * @param isbn ISBN der Buchbeschreibung
     * @param author Autor der Buchbeschreibung
     * @param title Buchtitel
     * @param emailBesitzer Emailadresse des Besitzers der Buchbeschreibung/-exemplar
     * @return die ersetellte Buchbeschreibung
     */
    BuchExemplar buchZurVerfuegungStellen(String isbn, String author, String title, String emailBesitzer);
    void zeigeAlleBuecher();
    BuchBeschreibung getBuchBeschreibung(String isbn);
    /**
     * Realisiert das Ausleihen eines Buchexemplares.
     * 
     * @param buchbeschreibung Beschreibung für das ausgeliehene Buchexemplar
     * @param id ID des Buchexemplars
     * @param ausleiher Leiher des Buchexemplars
     * @return das ausgeliehene Buchexemplar
     */
    //BuchExemplar buchAusleihen(BuchBeschreibung buchbeschreibung, int id, Benutzer ausleiher);
    BuchExemplar buchAusleihen(String isbn, String leiher);
    int buchZaehler();
    /**
     * Realisiert das Zurückgeben eines Buchexemplars.
     * 
     * @param id ID des Buchexemplars
     * @return true: wenn erfolgreich zurückgegeben
     *         false: wenn nicht erfolgreich zurückgegeben
     */
    boolean buchZurueckgeben(int id);
    BuchExemplar buchZurueckGeben(String isbn, String emailLeiher);
    
    /**
     * Realisiert das Zurückfordern eines verliehen Buchexemplars.
     * 
     * @param id ID des Buchexemplars
     * @return Buchexemplar, welches zurückgefordert wird
     */
    BuchExemplar buchZurueckfordern(int id);
    BuchExemplar buchZurueckfordern(String isbn, String besitzer);
    
    /**
     * Realisiert das Entfernen eines Buches aus dem System.
     * 
     * @param isbn ISBN des Buches
     * @param besitzer Besitzer des Buches
     * @return true: wenn das Buch erfolgreich entfernt wurde
     *         false: wenn das Buch nicht erfolgreich entfernt wurde
     */
    boolean buchEntfernen(String isbn, Benutzer besitzer);
}

