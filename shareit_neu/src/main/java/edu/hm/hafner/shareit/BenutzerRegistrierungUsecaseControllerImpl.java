package edu.hm.hafner.shareit;

import java.util.Collection;

import edu.hm.hafner.shareit.db.BenutzerController;
import edu.hm.hafner.shareit.db.BenutzerControllerImpl;
import edu.hm.hafner.shareit.db.RegistrierungsController;
import edu.hm.hafner.shareit.db.RegistrierungsControllerImpl;
import edu.hm.hafner.shareit.model.Registrierung;

/**
 * Usecase Controller für den Anwendungsfall "Benutzer registrieren".
 *
 * @author Ulli Hafner
 */
public class BenutzerRegistrierungUsecaseControllerImpl implements BenutzerRegistrierungUsecaseController {
    private final RegistrierungsController controller = new RegistrierungsControllerImpl();
    private final BenutzerController benutzerController = new BenutzerControllerImpl();
    
    /**
     * Liefert alle bestehenden Registrierungen zurück.
     *
     * @return die Registrierungen
     */
    @Override
    public Collection<Registrierung> getRegistrierungen() {
        return controller.findRegistrierungen();
    }
    
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
    @Override
    public Registrierung registriereBenutzer(final String vorname, final String nachname, final String email, final String passwort) {
        if (!controller.findByEmail(email).isEmpty()
                || !benutzerController.findByEmail(email).isEmpty()) {
            throw new IllegalStateException("Die Email ist schon verwendet " + email);
        }
        return controller.create(vorname, nachname, email, passwort);
    }
}

