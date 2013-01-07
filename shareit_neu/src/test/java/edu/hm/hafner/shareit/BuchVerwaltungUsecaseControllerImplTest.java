package edu.hm.hafner.shareit;

import org.junit.Test;

import static org.junit.Assert.*;
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
        
        zeigeBuecher();
    }
    
    @Test
    public void testeBuchZurueckFordern(){
        
        testeBuchZurVerfuegungStellen();
        
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        BuchExemplar exemplar = verwaltung.buchZurueckfordern("123", "keckes@hm.edu");
        
        // zeigeBuecher();
        
    }
    private void zeigeBuecher(){
        BuchVerwaltungUsecaseController verwaltung = new BuchVerwaltungUsecaseControllerImpl();
        verwaltung.zeigeAlleBuecher();
    }
    
    private void verifyBooks(final BuchVerwaltungUsecaseController verwaltung, final int expectedNumber){
        assertEquals("Falsche Anzahl an Buechern", expectedNumber, verwaltung.buchZaehler());
    }
}

