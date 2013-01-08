package edu.hm.hafner.shareit;

import java.util.Collection;

import edu.hm.hafner.shareit.model.BuchBeschreibung;
import edu.hm.hafner.shareit.model.BuchExemplar;

/**
 * Usecase Controller f�r die Anwendungsf�lle, welche die
 * Buchbeschreibungen und Buchexemplare betreffen.
 * 
 * @author Cookie
 */
public interface BuchVerwaltungUsecaseController {
    
    /**
     * Stellt eine neue Buchbeschreibung zur Verf�gung.
     * 
     * Wenn schon eine Buchbeschreibung mit den �bergebenen Daten
     * existiert, wird dieser ein weiters Buchexemplar dazugez�hlt.
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
     * @param buchbeschreibung Beschreibung f�r das ausgeliehene Buchexemplar
     * @param id ID des Buchexemplars
     * @param ausleiher Leiher des Buchexemplars
     * @return das ausgeliehene Buchexemplar
     */
    //BuchExemplar buchAusleihen(BuchBeschreibung buchbeschreibung, int id, Benutzer ausleiher);
    BuchExemplar buchAusleihen(String isbn, String leiher);
    int buchZaehler();
    /**
     * Realisiert das Zur�ckgeben eines Buchexemplars.
     * 
     * @param id ID des Buchexemplars
     * @return true: wenn erfolgreich zur�ckgegeben
     *         false: wenn nicht erfolgreich zur�ckgegeben
     */
    //boolean buchZurueckgeben(int id);
    BuchExemplar buchZurueckGeben(String isbn, String emailLeiher);
    
    /**
     * Realisiert das Zur�ckfordern eines verliehen Buchexemplars.
     * 
     * @param id ID des Buchexemplars
     * @return Buchexemplar, welches zur�ckgefordert wird
     */
    //BuchExemplar buchZurueckfordern(int id);
    BuchExemplar buchZurueckfordern(String isbn, String besitzer, String leiher);
    
    /**
     * Realisiert das Entfernen eines Buches aus dem System.
     * 
     * @param isbn ISBN des Buches
     * @param besitzer Besitzer des Buches
     * @return true: wenn das Buch erfolgreich entfernt wurde
     *         false: wenn das Buch nicht erfolgreich entfernt wurde
     */
    void buchEntfernen(String isbn, String besitzer);
    Collection<BuchExemplar> buchSuchen(BuchBeschreibung beschreibung, boolean nurVerfuegbar);
    
    Collection<BuchExemplar> eigeneBuecher(String besitzer);
    Collection<BuchExemplar> eigeneLeihe(String leiher);
    
}

