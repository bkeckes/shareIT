package edu.hm.hafner.shareit.db;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import edu.hm.hafner.shareit.model.Benutzer;
import edu.hm.hafner.shareit.model.BuchBeschreibung;
import edu.hm.hafner.shareit.model.BuchExemplar;
import edu.hm.hafner.shareit.util.DatabaseFactory;

/**
 * Erzeugt, findet und löscht Buchexemplare.
 * 
 * @author Cookie
 */
public class BuchExemplarControllerImpl implements BuchExemplarController {
    
    // Pflichtangaben eines Buchexemplars bei der Erstellung
    
    /** Beschreibung des Buches */
    private static final String BESCHREIBUNG = "beschreibung";
    
    /** Besitzer des Buches */
    private static final String BESITZER = "besitzer";
    
    /** Leiher des Buches */
    private static final String LEIHER = "leiher";
    
    /** ID des Buches */
    private static final String ID = "id";
    
    private static final String ENTLIEHEN = "entliehen";
    
    /**
     * Liefert alle Buchexemplare
     * 
     * @return Datenbank-Collection
     */
    private DBCollection getBuchExemplareCollection() {
        return DatabaseFactory.INSTANCE.getDatabase().getCollection("buchexemplare");
    }
    
    @Override
    public BuchExemplar create(final BuchBeschreibung beschreibung, final Benutzer leiher, final int id) {
        
        // Benutzer-Objekt für den Besitzer des Buchexemplars
        BenutzerController ownerContrl = new BenutzerControllerImpl();
        Benutzer owner = (Benutzer)ownerContrl.findByEmail(beschreibung.getExemplarOwner().get(id));
        
        // Alle Buchexemplare mit der übergebenen ID
        Collection<BuchExemplar> existing = findById(id);
        
        // Wenn ein Buchexemplar mit der übergebenen ID existiert
        if(!existing.isEmpty()) {
            throw new IllegalStateException("Es ist bereits ein Buch mit dieser ID vorhanden.");
        }
        
        // Wenn es in der Besitzer-Map keinen Besitzer mit der übergebenen ID gibt
        if(beschreibung.getExemplarOwner().get(id) == null) {
            throw new IllegalStateException("Es ist kein Buch mit dieser ID vorhanden.");
        }
        
        // Neues Buchexemplar in der Datenbank speichern
        BasicDBObject buchExemplar = new BasicDBObject();
        buchExemplar.append(BESCHREIBUNG, beschreibung);
        buchExemplar.append(BESITZER, beschreibung.getExemplarOwner().get(id));
        buchExemplar.append(LEIHER, leiher.getEmail());
        buchExemplar.append(ID, id);
        
        // Das Buchexemplar aus der Besitzer-Map löschen,
        // da es ausgeliehen wurde und nicht zweimal verliehen werden kann
        beschreibung.getExemplarOwner().remove(id);
        beschreibung.setExemplars(beschreibung.getExemplars() - 1);
        
        // Wenn es nur ein Buchexemplar gibt, wird die Buchbeschreibung aus der Datenbank gelöscht,
        // da diese nicht mehr in der Auswahl-Liste erscheinen soll
        if(beschreibung.getExemplars() < 1) {
            BuchBeschreibungController buchBeschreibungen = new BuchBeschreibungControllerImpl();
            buchBeschreibungen.delete(beschreibung.getIsbn());
        }
        
        // Das Buchexemplar wird in die Buchexemplar-Collection aufgenommen
        getBuchExemplareCollection().insert(buchExemplar);
        
        // Rückgabe des Buchexemplars, welches ausgeliehen wird
        return new BuchExemplar(beschreibung, owner, leiher, id);
    }
    
    @Override
    public Collection<BuchExemplar> findBuchExemplare() {
        return asCollection(getBuchExemplareCollection().find());
    }
    
    @Override
    public Collection<BuchExemplar> findById(final int id) {
        return asCollection(queryForId(id));
    }
    
    @Override
    public void delete(final int id) {
        
        DBCursor cursor = queryForId(id);
        
        try {
            if(!cursor.hasNext()) {
                throw new NoSuchElementException("Kein Buch mit dieser ID: "+id+" gefunden");
            }
            getBuchExemplareCollection().remove(cursor.next());
        }
        finally {
            cursor.close();
        }
    }
    
    /**
     * Findet alle Buchexemplare in der Datenbank.
     * 
     * Gibt die gefundenen Buchexemplare als Collection zurück.
     * 
     * @param find alle Buchexemplare in der DB
     * @return Collection der Buchexemplare
     */
    private Collection<BuchExemplar> asCollection(final DBCursor find) {
        
        try {
            List<BuchExemplar> buchExemplare = Lists.newArrayList();
            
            for(DBObject dbObject : find) {
                String beschreibung = (String)dbObject.get(BESCHREIBUNG);
                String besitzer = (String)dbObject.get(BESITZER);
                String leiher = (String)dbObject.get(LEIHER);
                
                //int id = (Integer)dbObject.get(ID);
                
                
                buchExemplare.add(new BuchExemplar(beschreibung, besitzer, leiher));
            }
            return buchExemplare;
        }
        finally {
            find.close();
        }
    }
    
    /**
     * Gibt eine Datenbank-Collection mit den Buchexemplaren,
     * welche zur übergebenen ID passen, zurück.
     * 
     * @param id ID des gesuchten Buchexemplares
     * @return Datenbank-Collection
     */
    private DBCursor queryForId(final int id) {
        
        BasicDBObject query = new BasicDBObject();
        query.append(ID, id);
        
        return getBuchExemplareCollection().find(query);
    }
    
    private DBCursor queryForIsbn(final String isbn) {
        
        BasicDBObject query = new BasicDBObject();
        query.append(BESCHREIBUNG, isbn);
        
        return getBuchExemplareCollection().find(query);
    }
    
    private DBCursor queryForExemplarByBesitzer(final String isbn, final String besitzer){
        BasicDBObject query = new BasicDBObject();
        query.append(BESCHREIBUNG, isbn);
        query.append(BESITZER, besitzer);
        
        return getBuchExemplareCollection().find(query);
    }
    private DBCursor queryForExemplarByLeiher(final String isbn, final String leiher){
        BasicDBObject query = new BasicDBObject();
        query.append(BESCHREIBUNG, isbn);
        query.append(LEIHER, leiher);
        
        return getBuchExemplareCollection().find(query);
    }
    
    @Override
    public BuchExemplar createExemplar(final String isbn, final String besitzerEmail) {
        BasicDBObject buchExemplar = new BasicDBObject();
        buchExemplar.append(BESCHREIBUNG, isbn);
        buchExemplar.append(BESITZER, besitzerEmail);
        buchExemplar.append(LEIHER, null);
        //buchExemplar.append(ENTLIEHEN, false);
        
        getBuchExemplareCollection().insert(buchExemplar);
        return new BuchExemplar(isbn, besitzerEmail, null);
    }
    
    @Override
    public Collection<BuchExemplar> getAllBooks() {
        return asCollection(getBuchExemplareCollection().find());
    }
    
    @Override
    public Collection<BuchExemplar> findByIsbn(final String isbn) {
        
        return asCollection(queryForIsbn(isbn));
    }
    
    @Override
    public BuchExemplar rentExemplar(final String isbn, final String besitzerEmail, final String leiherEmail) {
        
        //neues Objekt
        BasicDBObject buchExemplar = new BasicDBObject();
        buchExemplar.append(BESCHREIBUNG, isbn);
        buchExemplar.append(BESITZER, besitzerEmail);
        buchExemplar.append(LEIHER, leiherEmail);
        
        //zu ersetzendes
        BasicDBObject query = new BasicDBObject();
        query.append(BESCHREIBUNG, isbn);
        query.append(BESITZER, besitzerEmail);
        
        
        getBuchExemplareCollection().update(query, buchExemplar);
        return new BuchExemplar(isbn, besitzerEmail, leiherEmail);
    }
    
    @Override
    public boolean delete(final String isbn, final String besitzer) {
        DBCursor cursor = queryForExemplarByBesitzer(isbn, besitzer);
        
        try {
            if(!cursor.hasNext()) {
                throw new NoSuchElementException("Kein Buch mit dieser ISBN: "+isbn+" von "+besitzer+" gefunden");
            }
            getBuchExemplareCollection().remove(cursor.next());
        }
        finally {
            cursor.close();
        }
        return true;
    }
    
    @Override
    public BuchExemplar returnExemplar(final String isbn, final String leiherEmail) {
        
        //Hole die email des Besitzers
        DBCursor cursor = queryForExemplarByLeiher(isbn, leiherEmail);
        if(!cursor.hasNext()){
            throw new NoSuchElementException("Fehler beim zurueck geben des Buches");
        }
        String besitzer = asCollection(cursor).iterator().next().getBesitzerEmail();
        
        //neues Objekt
        BasicDBObject buchExemplar = new BasicDBObject();
        buchExemplar.append(BESCHREIBUNG, isbn);
        buchExemplar.append(LEIHER, null);
        buchExemplar.append(BESITZER, besitzer);
        
        //zu ersetzendes
        BasicDBObject query = new BasicDBObject();
        query.append(BESCHREIBUNG, isbn);
        query.append(LEIHER, leiherEmail);
        
        
        getBuchExemplareCollection().update(query, buchExemplar);
        return new BuchExemplar(isbn, besitzer, null);
    }
    
}

