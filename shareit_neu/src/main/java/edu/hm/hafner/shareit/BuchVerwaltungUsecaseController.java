package edu.hm.hafner.shareit;

import java.util.Collection;

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
     * Buch wird zur Verfügung gestellt.
     * Eine Buchbeschreibung wird nur neu angelegt wenn sie noch nicht existiert.
     *
     * @param isbn
     * @param author
     * @param title
     * @param emailBesitzer
     * @return buchExemplar
     */
    BuchExemplar buchZurVerfuegungStellen(String isbn, String author, String title, String emailBesitzer);
    
    /**
     * Buch wird ausgeliehen wenn es gefunden wird, ansonsten wird ein leeres BuchExemplar zurück gegeben
     * 
     * @param isbn
     * @param leiher
     * @return exemplar
     */
    BuchExemplar buchAusleihen(String isbn, String leiher);
    
    /**
     * Buch wird zurück gegeben
     * 
     * @param isbn
     * @param emailLeiher
     * @return exemplar
     */
    BuchExemplar buchZurueckGeben(String isbn, String emailLeiher);
    
    /**
     * Buch wird als zurückgefordert markiert.
     * 
     * @param isbn
     * @param besitzer
     * @param leiher
     * @return exemplar
     */
    BuchExemplar buchZurueckfordern(String isbn, String besitzer, String leiher);
    
    /**
     * Buch wird aus Liste entfernt wenn es nicht ausgeliehen ist
     * 
     * @param isbn
     * @param besitzer
     */
    void buchEntfernen(String isbn, String besitzer);
    
    /**
     * Buch nach Beschreibung suchen.
     * Es können bei Bedarf auch nur verfügbare Büchern angezeigt werden
     *
     * @param beschreibung
     * @param nurVerfuegbar
     * @return collection
     */
    Collection<BuchExemplar> buchSuchen(BuchBeschreibung beschreibung, boolean nurVerfuegbar);
    
    /**
     * Eigene Bücher werden auf der Konsole ausgegeben und als Liste zurück gegeben
     * 
     * @param besitzer
     * @return collection
     */
    Collection<BuchExemplar> eigeneBuecher(String besitzer);
    
    /**
     * Bücher werden angezeigt die man selber leiht.
     * 
     * @param leiher
     * @return collection
     */
    Collection<BuchExemplar> eigeneLeihe(String leiher);
    
    /**
     * Gibt die Buchbeschreibung zu einer ISBN aus
     * 
     * @param isbn
     * @return buchbeschreibung
     */
    BuchBeschreibung getBuchBeschreibung(String isbn);
    
    
    /**
     * Gibt die Anzahl aller Bücher im System aus
     * 
     * @return count
     */
    int buchZaehler();
    
    /**
     * zeigt alle Buecher in der Konsole an
     * 
     */
    void zeigeAlleBuecher();
}

