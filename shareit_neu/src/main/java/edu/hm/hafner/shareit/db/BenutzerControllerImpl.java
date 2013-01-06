package edu.hm.hafner.shareit.db;

import java.util.Collection;
import java.util.Collections;

import edu.hm.hafner.shareit.model.Benutzer;

/**
 * Erzeugt, findet und ändert Registrierungen.
 *
 * @author Ulli Hafner
 */
public class BenutzerControllerImpl implements BenutzerController {
    @Override
    public Collection<Benutzer> findByEmail(final String email) {
        return Collections.emptyList(); // Mehr wird für die Benutzerregistrierung nicht benötigt
        
        // Lösungsvorschlag @author Cookie
        
        
        //        Collection<Registrierung> regGefunden =  registrierung.findByEmail(email);
        //        Collection<Benutzer> benutzer = Lists.newArrayList();
        //        Iterator<Registrierung> iter = regGefunden.iterator();
        //
        //        while(iter.hasNext()) {
        //
        //            Benutzer b = (Benutzer)iter.next();
        //            benutzer.add(b);
        //        }
        //
        //        return benutzer;
    }
}

