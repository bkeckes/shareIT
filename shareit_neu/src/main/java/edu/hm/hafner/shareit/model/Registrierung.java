package edu.hm.hafner.shareit.model;

/**
 * Enth�lt alle relevanten Daten f�r einen neuen Benutzer von ShareIt. Ein
 * Benutzer kann im System eindeutig �ber seine EMail identifiziert werden.
 *
 * @author Ulli Hafner
 */
public class Registrierung {
    private final String nachname;
    private final String vorname;
    private final String email;
    private final String passwort;
    
    /**
     * Erzeugt eine neue {@link Registrierung}.
     *
     * @param vorname
     *            Vorname des Benutzers
     * @param nachname
     *            Nachname des Benutzers
     * @param email
     *            Email Adresse des Benutzers
     * @param passwort
     *            Passwort des Benutzers
     */
    public Registrierung(final String vorname, final String nachname, final String email, final String passwort) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
        this.passwort = passwort;
    }
    
    /**
     * Liefert den Nachnamen zur�ck.
     *
     * @return der Nachname
     */
    public String getNachname() {
        return nachname;
    }
    
    /**
     * Liefert den Vornamen zur�ck.
     *
     * @return der Vorname
     */
    public String getVorname() {
        return vorname;
    }
    
    /**
     * Liefert die EMail zur�ck.
     *
     * @return die EMail
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Liefert das Passwort zur�ck.
     *
     * @return das Passwort
     */
    public String getPasswort() {
        return passwort;
    }
}

