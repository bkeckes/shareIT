package edu.hm.hafner.shareit;

import org.junit.Test;

import static org.junit.Assert.*;
import edu.hm.hafner.shareit.model.Registrierung;
import edu.hm.hafner.shareit.util.AbstractDatabaseTest;

/**
 * Testet die Klasse {@link BenutzerRegistrierungUsecaseControllerImpl}.
 *
 * @author Ulli Hafner
 */
public class BenutzerRegistrierungUsecaseControllerImplTest extends AbstractDatabaseTest {
    private static final String PASSWORT = "geheim";
    private static final String EMAIL = "hafner@hm.edu";
    private static final String NACHNAME = "Hafner";
    private static final String VORNAME = "Ullrich";
    
    /**
     * Zeigt, dass die Neuanlage einer Registrierung funktioniert.
     */
    @Test
    public void testeAnlegenEinerRegistrierung() {
        BenutzerRegistrierungUsecaseController verwaltung = new BenutzerRegistrierungUsecaseControllerImpl();
        verifyRegistrierungen(verwaltung, 0);
        
        Registrierung benutzer = verwaltung.registriereBenutzer(VORNAME, NACHNAME, EMAIL, PASSWORT);
        verifyRegistrierungen(verwaltung, 1);
        
        assertEquals("Falscher Vorname", VORNAME, benutzer.getVorname());
        assertEquals("Falscher Nachname", NACHNAME, benutzer.getNachname());
        assertEquals("Falsche EMail", EMAIL, benutzer.getEmail());
        assertEquals("Falsches Passwort", PASSWORT, benutzer.getPasswort());
    }
    
    private void verifyRegistrierungen(final BenutzerRegistrierungUsecaseController verwaltung, final int expectedNumber) {
        assertEquals("Falsche Anzahl registierte Benutzer", expectedNumber, verwaltung.getRegistrierungen().size());
    }
    
    /**
     * Zeigt, dass die Neuanlage einer Registrierung fehlschlägt, wenn der Benutzername bereits existiert.
     */
    @Test(expected = IllegalStateException.class)
    public void testeDoppeltenBenutzernamen() {
        BenutzerRegistrierungUsecaseController verwaltung = new BenutzerRegistrierungUsecaseControllerImpl();
        
        verwaltung.registriereBenutzer(VORNAME, NACHNAME, EMAIL, PASSWORT);
        verwaltung.registriereBenutzer("a", "b", EMAIL, "c");
    }
}

