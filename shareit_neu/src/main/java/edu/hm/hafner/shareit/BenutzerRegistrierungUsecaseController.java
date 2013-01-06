package edu.hm.hafner.shareit;

import java.util.Collection;

import edu.hm.hafner.shareit.model.Registrierung;

/**
 * Usecase Controller für den Anwendungsfall "Benutzer registrieren".
 *
 * @author Ulli Hafner
 */
public interface BenutzerRegistrierungUsecaseController {
    
    /**
     * Liefert alle bestehenden Registrierungen zurück.
     *
     * @return die Registrierungen
     */
    Collection<Registrierung> getRegistrierungen();
    
    /**
     * Registriert einen neuen Benutzer.
     *
     * @param vorname
     *            Vorname des Benutzers
     * @param nachname
     *            Nachname des Benutzers
     * @param email
     *            Email Adresse des Benutzers
     * @param passwort
     *            Passwort des Benutzers
     * @return der erzeugte Benutzer
     */
    Registrierung registriereBenutzer(String vorname, String nachname, String email, String passwort);
    
}
