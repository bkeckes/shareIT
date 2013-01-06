package edu.hm.hafner.shareit.model;


/**
 * Kann Bücher ausleihen und verleihen.
 *
 * @author Ulli Hafner
 */
public class Benutzer extends Registrierung {
    /**
     * Erzeugt einen neuen {@link Benutzer}.
     *
     * @param nachname
     *            Nachname des Benutzers
     * @param vorname
     *            Vorname des Benutzers
     * @param email
     *            Email Adresse des Benutzers
     * @param passwort
     *            Passwort des Benutzers
     */
    public Benutzer(final String nachname, final String vorname, final String email, final String passwort) {
        super(nachname, vorname, email, passwort);
    }
    
    /**
     * Liefert zurück, ob dieser Nutzer erweiterte Rechte besitzt.
     *
     * @return <code>true</code> falls er erweiterte Rechte besitzt,
     *         <code>false</code> otherwise
     */
    public boolean isAdminstrator() {
        return false;
    }
}

