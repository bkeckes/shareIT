package edu.hm.hafner.shareit.db;

import java.util.Collection;

import edu.hm.hafner.shareit.model.Benutzer;

/**
 * Erzeugt, findet und �ndert Registrierungen.
 *
 * @author Ulli Hafner
 */
public interface BenutzerController {
    /**
     * Liefert alle Benutzer mit der �bergebenen EMail zur�ck.
     *
     * @param email
     *            die EMail Addresse
     * @return die Benutzer
     */
    Collection<Benutzer> findByEmail(String email);
}
