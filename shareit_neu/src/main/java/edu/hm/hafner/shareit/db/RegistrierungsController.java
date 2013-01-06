package edu.hm.hafner.shareit.db;

import java.util.Collection;

import edu.hm.hafner.shareit.model.Registrierung;

/**
 * Erzeugt, findet und �ndert Registrierungen.
 *
 * @author Ulli Hafner
 */
public interface RegistrierungsController {
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
     * @return die neue Registrierung
     */
    Registrierung create(String vorname, String nachname, String email, String passwort);
    
    /**
     * Liefert alle Registrierungen zur�ck.
     *
     * @return die gefundenen Registrierungen
     */
    Collection<Registrierung> findRegistrierungen();
    
    /**
     * Liefert alle Registrierungen mit der �bergebenen EMail zur�ck.
     *
     * @param email
     *            die zu pr�fende EMail
     * @return die gefundenen Registrierungen
     */
    Collection<Registrierung> findByEmail(String email);
    
    /**
     * L�scht die Registrierung zur gegebenen Email aus der Datenbank.
     *
     * @param email
     *            die Email der zu l�schenden Registrierung
     */
    void delete(String email);
    
    /**
     * �ndert den Vornamen aller Registrierungen, die die angegebene Email
     * haben.
     *
     * @param email
     *            die Email der Datensatze, der ge�ndert werden sollen
     * @param geaenderterVorname
     *            der neue Vorname
     */
    void setVorname(String email, String geaenderterVorname);
}
