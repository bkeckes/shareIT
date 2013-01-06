package edu.hm.hafner.shareit.db;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import edu.hm.hafner.shareit.model.Registrierung;
import edu.hm.hafner.shareit.util.DatabaseFactory;

/**
 * Erzeugt, findet und ändert Registrierungen.
 *
 * @author Ulli Hafner
 */
public class RegistrierungsControllerImpl implements RegistrierungsController {
    private static final String NACHNAME = "nachname";
    private static final String VORNAME = "vorname";
    private static final String EMAIL = "email";
    private static final String PASSWORT = "passwort";
    
    private DBCollection getRegistrierungenCollection() {
        return DatabaseFactory.INSTANCE.getDatabase().getCollection("registrierungen");
    }
    
    @Override
    public Registrierung create(final String vorname, final String nachname, final String email, final String passwort) {
        Collection<Registrierung> existing = findByEmail(email);
        if (!existing.isEmpty()) {
            throw new IllegalStateException("Bereits eine Registrierung vorhanden mit der Email " + email);
        }
        BasicDBObject registrierung = new BasicDBObject();
        registrierung.append(VORNAME, vorname);
        registrierung.append(NACHNAME, nachname);
        registrierung.append(EMAIL, email);
        registrierung.append(PASSWORT, passwort);
        
        getRegistrierungenCollection().insert(registrierung);
        return new Registrierung(vorname, nachname, email, passwort);
    }
    
    @Override
    public Collection<Registrierung> findRegistrierungen() {
        return asCollection(getRegistrierungenCollection().find());
    }
    
    @Override
    public Collection<Registrierung> findByEmail(final String email) {
        return asCollection(queryForEmail(email));
    }
    
    private DBCursor queryForEmail(final String email) {
        BasicDBObject query = new BasicDBObject();
        query.append(EMAIL, email);
        
        return getRegistrierungenCollection().find(query);
    }
    
    private Collection<Registrierung> asCollection(final DBCursor result) {
        try {
            List<Registrierung> buecher = Lists.newArrayList();
            for (DBObject dbObject : result) {
                String vorname = (String)dbObject.get(VORNAME);
                String nachname = (String)dbObject.get(NACHNAME);
                String email = (String)dbObject.get(EMAIL);
                String passwort = (String)dbObject.get(PASSWORT);
                buecher.add(new Registrierung(vorname, nachname, email, passwort));
            }
            return buecher;
        }
        finally {
            result.close();
        }
    }
    
    @Override
    public void delete(final String email) {
        DBCursor cursor = queryForEmail(email);
        try {
            if (!cursor.hasNext()) {
                throw new NoSuchElementException("Keiner Registrierung gefunden zur EMail " + email);
            }
            getRegistrierungenCollection().remove(cursor.next());
        }
        finally {
            cursor.close();
        }
    }
    
    @Override
    public void setVorname(final String email, final String geaenderterVorname) {
        List<DBObject> geaenderteRegistrierungen = Lists.newArrayList();
        DBCursor registrierungen = queryForEmail(email);
        try {
            for (DBObject registrierung : registrierungen) {
                registrierung.put(VORNAME, geaenderterVorname);
                geaenderteRegistrierungen.add(registrierung);
            }
        }
        finally {
            registrierungen.close();
        }
        
        DBCollection tabelle = getRegistrierungenCollection();
        for (DBObject registrierung : geaenderteRegistrierungen) {
            tabelle.save(registrierung);
        }
    }
}

