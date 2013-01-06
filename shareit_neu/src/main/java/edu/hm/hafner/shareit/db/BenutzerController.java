package edu.hm.hafner.shareit.db;

import java.util.Collection;

import edu.hm.hafner.shareit.model.Benutzer;

/**
 * Erzeugt, findet und ändert Registrierungen.
 *
 * @author Ulli Hafner
 */
public interface BenutzerController {
    /**
     * Liefert alle Benutzer mit der übergebenen EMail zurück.
     *
     * @param email
     *            die EMail Addresse
     * @return die Benutzer
     */
    Collection<Benutzer> findByEmail(String email);
}
