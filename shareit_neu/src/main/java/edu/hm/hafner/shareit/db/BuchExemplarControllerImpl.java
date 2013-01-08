package edu.hm.hafner.shareit.db;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

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
    
    /** Status des Exemplars  */
    private static final String ZURUECKGEFORDERT = "gefordert";
    
    private static final String JA = "ja";
    private static final String NEIN = "nein";
    
    /**
     * Liefert alle Buchexemplare
     * 
     * @return Datenbank-Collection
     */
    private DBCollection getBuchExemplareCollection() {
        return DatabaseFactory.INSTANCE.getDatabase().getCollection("buchexemplare");
    }
    
    /**
     * Gibt die Ergebnisse die über das query aus der DB gefunden wurden als
     * Collection zurück
     * 
     * @param find
     * @return collection
     */
    private Collection<BuchExemplar> asCollection(final DBCursor find) {
        
        try {
            List<BuchExemplar> buchExemplare = Lists.newArrayList();
            
            for(DBObject dbObject : find) {
                String beschreibung = (String)dbObject.get(BESCHREIBUNG);
                String besitzer = (String)dbObject.get(BESITZER);
                String leiher = (String)dbObject.get(LEIHER);
                String zurueck = (String)dbObject.get(ZURUECKGEFORDERT);
                
                boolean gefordert=false;
                if(zurueck.equals(JA)){
                    gefordert = true;
                }
                
                buchExemplare.add(new BuchExemplar(beschreibung, besitzer, leiher, gefordert));
            }
            return buchExemplare;
        }
        finally {
            find.close();
        }
    }
    
    /**
     * Erstellt ein BuchExemplar
     * 
     * @param isbn
     * @param besitzerEmail
     * @return BuchExemplar
     */
    @Override
    public BuchExemplar createExemplar(final String isbn, final String besitzerEmail) {
        BasicDBObject buchExemplar = new BasicDBObject();
        buchExemplar.append(BESCHREIBUNG, isbn);
        buchExemplar.append(BESITZER, besitzerEmail);
        buchExemplar.append(LEIHER, null);
        buchExemplar.append(ZURUECKGEFORDERT, "nein");
        
        getBuchExemplareCollection().insert(buchExemplar);
        return new BuchExemplar(isbn, besitzerEmail, null);
    }
    
    /**
     * Gibt alle Bücher zurück
     * 
     * @return collection
     */
    @Override
    public Collection<BuchExemplar> getAllBooks() {
        
        return asCollection(getBuchExemplareCollection().find());
        
    }
    
    @Override
    public Collection<BuchExemplar> findByIsbn(final String isbn) {
        Collection<BuchExemplar> collection = asCollection(queryForIsbn(isbn));
        if(collection.isEmpty()) {
            throw new NoSuchElementException("Zu der ISBN: "+isbn+" wurde nichts in der DB gefunden");
        }
        return collection;
    }
    
    
    
    
    @Override
    public void delete(final String isbn, final String besitzer) {
        
        //Betreffende Bücher werden geholt
        DBCursor cursor = queryForExemplarByBesitzer(isbn, besitzer);
        if(!cursor.hasNext()) {
            System.out.println("Kein Buch mit dieser ISBN: "+isbn+" von "+besitzer+" gefunden");
            throw new NoSuchElementException("Kein Buch mit dieser ISBN: "+isbn+" von "+besitzer+" gefunden");
        }
        
        //Betreffende Bücher die keinen Leiher haben werden geholt.
        cursor = queryForExemplarByBesitzerWithEmptyLeiher(isbn, besitzer);
        if(!cursor.hasNext()) {
            System.out.println("Das Buch kann nicht geloscht werden da es im Moment ausgeliehen ist");
            throw new IllegalStateException("Das Buch kann nicht geloscht werden da es im Moment ausgeliehen ist");
        }
        
        //Wenn das Buch nicht ausgeliehen ist wird es gelöscht
        getBuchExemplareCollection().remove(cursor.next());
        
    }
    
    @Override
    public BuchExemplar returnExemplar(final String isbn, final String leiherEmail) {
        
        //Hole die email des Besitzers
        DBCursor cursor = queryForExemplarByLeiher(isbn, leiherEmail);
        if(!cursor.hasNext()){
            throw new NoSuchElementException("Fehler beim zurueck geben des Buches, Leiher und isbn werden nicht gefunden");
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
    
    @Override
    public BuchExemplar rentExemplar(final String isbn, final String besitzerEmail, final String leiherEmail) {
        
        //neues Objekt
        BasicDBObject buchExemplar = new BasicDBObject();
        buchExemplar.append(BESCHREIBUNG, isbn);
        buchExemplar.append(BESITZER, besitzerEmail);
        buchExemplar.append(LEIHER, leiherEmail);
        buchExemplar.append(ZURUECKGEFORDERT, NEIN);
        
        
        //zu ersetzendes
        BasicDBObject query = new BasicDBObject();
        query.append(BESCHREIBUNG, isbn);
        query.append(BESITZER, besitzerEmail);
        
        
        getBuchExemplareCollection().update(query, buchExemplar);
        return new BuchExemplar(isbn, besitzerEmail, leiherEmail);
    }
    
    @Override
    public BuchExemplar reclaimExemplar(final String isbn, final String besitzer, final String leiher) {
        
        //neues Objekt
        BasicDBObject buchExemplar = new BasicDBObject();
        buchExemplar.append(BESCHREIBUNG, isbn);
        buchExemplar.append(BESITZER, besitzer);
        buchExemplar.append(LEIHER, leiher);
        buchExemplar.append(ZURUECKGEFORDERT, JA);
        
        //zu ersetzendes
        BasicDBObject query = new BasicDBObject();
        query.append(BESCHREIBUNG, isbn);
        query.append(BESITZER, besitzer);
        query.append(LEIHER, leiher);
        query.append(ZURUECKGEFORDERT, NEIN);
        
        getBuchExemplareCollection().update(query, buchExemplar);
        
        return new BuchExemplar(isbn, besitzer, leiher, true);
    }
    
    
    @Override
    public Collection<BuchExemplar> findByBesitzer(final String besitzer) {
        return asCollection(queryByBesitzer(besitzer));
    }
    
    
    @Override
    public Collection<BuchExemplar> findByLeiher(final String leiher) {
        return asCollection(queryByLeiher(leiher));
    }
    
    /**
     * 
     * queryForIsbn
     * @param isbn
     * @return cursor
     */
    private DBCursor queryForIsbn(final String isbn) {
        
        BasicDBObject query = new BasicDBObject();
        query.append(BESCHREIBUNG, isbn);
        
        return getBuchExemplareCollection().find(query);
    }
    
    /**
     * 
     * queryByBesitzer
     * @param besitzer
     * @return cursor
     */
    private DBCursor queryByBesitzer(final String besitzer){
        BasicDBObject query = new BasicDBObject();
        query.append(BESITZER, besitzer);
        
        return getBuchExemplareCollection().find(query);
    }
    
    /**
     * 
     * queryForExemplarByBesitzer
     * @param isbn
     * @param besitzer
     * @return cursor
     */
    private DBCursor queryForExemplarByBesitzer(final String isbn, final String besitzer){
        BasicDBObject query = new BasicDBObject();
        query.append(BESCHREIBUNG, isbn);
        query.append(BESITZER, besitzer);
        
        return getBuchExemplareCollection().find(query);
    }
    
    /**
     * 
     * queryForExemplarByBesitzerWithEmptyLeiher
     * @param isbn
     * @param besitzer
     * @return cursor
     */
    private DBCursor queryForExemplarByBesitzerWithEmptyLeiher(final String isbn, final String besitzer){
        BasicDBObject query = new BasicDBObject();
        query.append(BESCHREIBUNG, isbn);
        query.append(BESITZER, besitzer);
        query.append(LEIHER, null);
        
        return getBuchExemplareCollection().find(query);
    }
    
    /**
     * 
     * queryByLeiher
     * @param leiher
     * @return cursor
     */
    private DBCursor queryByLeiher(final String leiher){
        BasicDBObject query = new BasicDBObject();
        query.append(LEIHER, leiher);
        
        return getBuchExemplareCollection().find(query);
    }
    
    /**
     * 
     * queryForExemplarByLeiher
     * @param isbn
     * @param leiher
     * @return cursor
     */
    private DBCursor queryForExemplarByLeiher(final String isbn, final String leiher){
        BasicDBObject query = new BasicDBObject();
        query.append(BESCHREIBUNG, isbn);
        query.append(LEIHER, leiher);
        
        return getBuchExemplareCollection().find(query);
    }
    
    
}

