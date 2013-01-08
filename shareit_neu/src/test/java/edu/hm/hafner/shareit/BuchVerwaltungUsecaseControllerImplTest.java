package edu.hm.hafner.shareit;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.Test;

import static org.junit.Assert.*;
import edu.hm.hafner.shareit.model.BuchBeschreibung;
import edu.hm.hafner.shareit.model.BuchExemplar;
import edu.hm.hafner.shareit.util.AbstractDatabaseTest;

/**
 * BuchVerwaltungUsecaseControllerImplTest.
 * Testet verschiedene UseCases die mit der Buchverwaltung vor kommen.
 * 
 * @author Benjamin Keckes
 */
public class BuchVerwaltungUsecaseControllerImplTest extends AbstractDatabaseTest{
    
    /**
     * legt ein paar Bücher an.
     * 
     */
    @Test
    public void testeBuchZurVerfuegungStellen(){
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        
        BuchExemplar exemplar = verwaltung.buchZurVerfuegungStellen("123", "Dan Brown", "Sakrileg", "keckes@hm.edu");
        verifyBooks(verwaltung, 1);
        verwaltung.buchZurVerfuegungStellen("3213", "Dan Brown", "Illuminati", "keckes@hm.edu");
        verifyBooks(verwaltung, 2);
        verwaltung.buchZurVerfuegungStellen("123", "Dan Brown", "Sakrileg", "keckes@hm.edu");
        verifyBooks(verwaltung, 3);
        verwaltung.buchZurVerfuegungStellen("123", "Dan Brown", "Sakrileg", "franz@hm.edu");
        verifyBooks(verwaltung, 4);
        verwaltung.buchZurVerfuegungStellen("5567", "John", "Sakrileg", "franz@hm.edu");
        verifyBooks(verwaltung, 5);
        
        assertEquals("Falsche isbn", "123", verwaltung.getBuchBeschreibung(exemplar.getIsbn()).getIsbn());
        assertEquals("Falscher Titel", "Sakrileg", verwaltung.getBuchBeschreibung(exemplar.getIsbn()).getTitle());
        assertEquals("Falscher Autor", "Dan Brown", verwaltung.getBuchBeschreibung(exemplar.getIsbn()).getAuthor());
        assertEquals("Fehler beim Ausleiher", null, exemplar.getLeiherEmail());
        
    }
    
    /**
     * 
     * leiht ein Buch aus.
     */
    @Test
    public void testeBuchAusleihen(){
        testeBuchZurVerfuegungStellen();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        BuchExemplar exemplar;
        
        //exemplar = verwaltung.buchAusleihen("4421", "asd@we");
        exemplar = verwaltung.buchAusleihen("123", "test@web.de");
        assertEquals("Fehler beim Ausleiher", "test@web.de", exemplar.getLeiherEmail());
        assertEquals("Fehler beim Leiher", "keckes@hm.edu", exemplar.getBesitzerEmail());
    }
    
    /**
     * 
     * leiht in anderes Buch aus.
     */
    public void testeBuchAusleihen2(){
        testeBuchZurVerfuegungStellen();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        verwaltung.buchAusleihen("5567", "test@web.de");
    }
    
    /**
     * 
     * gibt ein Buch zurück.
     */
    @Test
    public void testeBuchZurueckGeben(){
        
        testeBuchAusleihen();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        BuchExemplar exemplar;
        
        
        exemplar = verwaltung.buchZurueckGeben("123", "test@web.de");
        assertEquals("Fehler beim Ausleiher", null, exemplar.getLeiherEmail());
        assertEquals("Fehler beim Leiher", "keckes@hm.edu", exemplar.getBesitzerEmail());
        
        //zeigeBuecher();
    }
    
    /**
     * 
     * gibt ein Buch zurück das es nicht gibt. Erwartet Fehler.
     */
    @Test (expected = NoSuchElementException.class)
    public void testeBuchZurueckGebenMitFehler(){
        testeBuchAusleihen();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        verwaltung.buchZurueckGeben("773", "test@web.de");
    }
    
    /**
     * 
     * Fordert ein ausgeliehenes Buch zurück.
     */
    @Test
    public void testeBuchZurueckFordern(){
        
        testeBuchAusleihen();
        
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        verwaltung.buchZurueckfordern("123", "keckes@hm.edu", "test@web.de");
        
    }
    
    /**
     * 
     * löscht ein Buch
     */
    @Test
    public void testeBuchLoeschen(){
        testeBuchAusleihen();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        verwaltung.buchEntfernen("123", "keckes@hm.edu");
        
        verifyBooks(verwaltung, 4);
        //zeigeBuecher();
    }
    
    /**
     * 
     * löscht ein Buch das es nicht gibt. Erwartet Fehler.
     */
    @Test (expected = NoSuchElementException.class)
    public void testeBuchLoeschenMitFehler1(){
        testeBuchZurVerfuegungStellen();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        verwaltung.buchEntfernen("899", "keckes@hm.edu");
    }
    
    /**
     * 
     * löscht ein Buch das ausgeliehen ist. Erwartet Fehler.
     */
    @Test (expected = IllegalStateException.class)
    public void testeBuchLoeschenMitFehler2(){
        testeBuchAusleihen2();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        verwaltung.buchEntfernen("5567", "franz@hm.edu");
    }
    
    /**
     * 
     * sucht ein Buch mit verschiedenen Kriterien.
     */
    @Test
    public void testeBuchSuchen(){
        testeBuchAusleihen();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        
        
        
        //Nicht suchen, sondern alle Bücher ausgeben
        Collection<BuchExemplar> exemplare = verwaltung.buchSuchen(null, false);
        verifyResults(exemplare, 5);
        
        //Alle Bücher ausgeben die noch verfügbar sind
        exemplare = verwaltung.buchSuchen(null, true);
        verifyResults(exemplare, 4);
        
        //alle Bücher mit ISBN 123 ausgeben
        exemplare = verwaltung.buchSuchen(new BuchBeschreibung("123", null, null), false);
        verifyResults(exemplare, 3);
        
        //verfügbare Bücher mit ISBN 123 ausgeben
        exemplare = verwaltung.buchSuchen(new BuchBeschreibung("123", null, null), true);
        verifyResults(exemplare, 2);
        
        //alle Bücher mit Titel Sakrileg ausgeben
        exemplare = verwaltung.buchSuchen(new BuchBeschreibung(null, "Sakrileg", null), false);
        verifyResults(exemplare, 4);
        
        //alle Bücher mit Autor Dan Brown ausgeben
        exemplare = verwaltung.buchSuchen(new BuchBeschreibung(null, null, "Dan Brown"), false);
        verifyResults(exemplare, 4);
        
        //alle Bücher mit Autor Dan Brown ausgeben die noch verfügbar sind
        exemplare = verwaltung.buchSuchen(new BuchBeschreibung(null, null, "Dan Brown"), true);
        verifyResults(exemplare, 3);
        
    }
    
    /**
     * 
     * eigene Bücher sollen ausgegeben werden.
     */
    @Test
    public void testeEigeneBuecherAnzeigen(){
        testeBuchAusleihen();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        Collection<BuchExemplar> exemplare = verwaltung.eigeneBuecher("keckes@hm.edu");
        assertEquals("Unerwartete Anzahl an Buechern", 3, exemplare.size());
    }
    
    /**
     * 
     * Bücher die man selber ausgeliehen hat sollen angezeigt werden.
     */
    @Test
    public void testeEigeneAusleihe(){
        testeBuchAusleihen();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        Collection<BuchExemplar> exemplare = verwaltung.eigeneLeihe("test@web.de");
        assertEquals("Unerwartete Anzahl an Buechern", 1, exemplare.size());
    }
    
    /**
     * 
     * legt Bücher an, löscht eins und zeigt danach alle Bücher an.
     */
    @Test
    public void testeAnlegenLoeschenUndAnzeigen(){
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        verwaltung.buchZurVerfuegungStellen("111", "A1", "T1", "email1@hm.edu");
        verwaltung.buchZurVerfuegungStellen("222", "A2", "T2", "email2@hm.edu");
        verifyBooks(verwaltung, 2);
        verwaltung.buchEntfernen("111", "email1@hm.edu");
        verifyBooks(verwaltung, 1);
        
        zeigeBuecher();
    }
    
    /**
     * 
     * Überprüft die Anzahl der Suchergebnisse.
     * @param ergebnisse
     * @param expectedNumber
     */
    private void verifyResults(final Collection<BuchExemplar> ergebnisse, final int expectedNumber){
        assertEquals("Unerwartetes Suchergebnis", expectedNumber, ergebnisse.size());
    }
    
    /**
     * 
     * zeigt alle Bücher aus der DB an.
     */
    private void zeigeBuecher(){
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        verwaltung.zeigeAlleBuecher();
    }
    
    /**
     * 
     * Überprüft die Anzahl der gespeicherten Bücher.
     * @param verwaltung
     * @param expectedNumber
     */
    private void verifyBooks(final BuchVerwaltungUsecaseController verwaltung, final int expectedNumber){
        assertEquals("Falsche Anzahl an Buechern", expectedNumber, verwaltung.buchZaehler());
    }
}

