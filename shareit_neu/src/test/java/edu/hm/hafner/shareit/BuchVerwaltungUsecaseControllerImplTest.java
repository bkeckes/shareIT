package edu.hm.hafner.shareit;

import java.util.Collection;

import org.junit.Test;

import static org.junit.Assert.*;
import edu.hm.hafner.shareit.model.BuchBeschreibung;
import edu.hm.hafner.shareit.model.BuchExemplar;
import edu.hm.hafner.shareit.util.AbstractDatabaseTest;

/**
 * TODO: Document type BuchVerwaltungUsecaseControllerImplTest.
 */
public class BuchVerwaltungUsecaseControllerImplTest extends AbstractDatabaseTest{
    
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
    
    @Test
    public void testeBuchAusleihen(){
        testeBuchZurVerfuegungStellen();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        BuchExemplar exemplar;
        
        exemplar = verwaltung.buchAusleihen("123", "test@web.de");
        assertEquals("Fehler beim Ausleiher", "test@web.de", exemplar.getLeiherEmail());
        assertEquals("Fehler beim Leiher", "keckes@hm.edu", exemplar.getBesitzerEmail());
    }
    
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
    
    @Test
    public void testeBuchZurueckFordern(){
        
        testeBuchAusleihen();
        
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        BuchExemplar exemplar = verwaltung.buchZurueckfordern("123", "keckes@hm.edu", "test@web.de");
        
        //zeigeBuecher();
        
    }
    
    @Test
    public void testeBuchLoeschen(){
        testeBuchAusleihen();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        verwaltung.buchEntfernen("123", "keckes@hm.edu");
        
        verifyBooks(verwaltung, 4);
        //zeigeBuecher();
    }
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
    
    @Test
    public void testeEigeneBuecherAnzeigen(){
        testeBuchAusleihen();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        Collection<BuchExemplar> exemplare = verwaltung.eigeneBuecher("keckes@hm.edu");
        assertEquals("Unerwartete Anzahl an Buechern", 3, exemplare.size());
    }
    
    @Test
    public void testeEigeneAusleihe(){
        testeBuchAusleihen();
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        Collection<BuchExemplar> exemplare = verwaltung.eigeneLeihe("test@web.de");
        assertEquals("Unerwartete Anzahl an Buechern", 1, exemplare.size());
    }
    
    
    
    private void verifyResults(final Collection<BuchExemplar> ergebnisse, final int expectedNumber){
        assertEquals("Unerwartetes Suchergebnis", expectedNumber, ergebnisse.size());
    }
    private void zeigeBuecher(){
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        verwaltung.zeigeAlleBuecher();
    }
    
    private void verifyBooks(final BuchVerwaltungUsecaseController verwaltung, final int expectedNumber){
        assertEquals("Falsche Anzahl an Buechern", expectedNumber, verwaltung.buchZaehler());
    }
}

