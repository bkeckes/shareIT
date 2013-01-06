package edu.hm.hafner.shareit.db;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import edu.hm.hafner.shareit.model.BuchBeschreibung;
import edu.hm.hafner.shareit.util.DatabaseFactory;

/**
 * Erzeugt, findet und l�scht Buchbeschreibungen.
 * 
 * @author Cookie
 */
public class BuchBeschreibungControllerImpl implements BuchBeschreibungController {
    
    // Pflichtangaben einer Buchbeschreibung.
    
    /** ISBN der Buchbeschreibung */
    private static final String ISBN = "isbn";
    
    /** Buchtitel */
    private static final String TITLE = "title";
    
    /** Autor der Buchbeschreibung */
    private static final String AUTHOR = "author";
    
    /** Besitzer der jeweiligen Buchexemplare */
    private static final String EXEMPLAROWNER = "exemplarOwner";
    
    /**
     * Liefert alle Buchbeschreibungen.
     * 
     * @return Datenbank-Collection
     */
    private DBCollection getBuchBeschreibungenCollection() {
        return DatabaseFactory.INSTANCE.getDatabase().getCollection("buchbeschreibungen");
    }
    
    @Override
    public BuchBeschreibung create(final String isbn, final String title, final String author, final String emailBesitzer) {
        
        // Alle Buchbeschreibungen mit der �bergebenen ISBN
        Collection<BuchBeschreibung> existing = findByISBN(isbn);
        
        // Wenn es schon eine Buchbeschreibung mit der ISBN gibt
        if(!existing.isEmpty()) {
            
            // Dann wird die Anzahl der Exemplare von der Buchbeschreibung erh�t
            // und die Besitzer-Email in die Besitzer-Map geschrieben
            Iterator<BuchBeschreibung> iter = existing.iterator();
            iter.next().setExemplars(iter.next().getExemplars() + 1);
            iter.next().getExemplarOwner().put(iter.next().getExemplars(), emailBesitzer);
            
            // und die Buchbeschreibung zur�ckgegeben
            return iter.next();
        }
        
        // Neue Buchbeschreibung erzeugen und f�r das erste Buchexemplar
        // die Besitzer-Email speicher
        BuchBeschreibung neueBS = new BuchBeschreibung(isbn, title, author);
        neueBS.getExemplarOwner().put(neueBS.getExemplars(), emailBesitzer);
        
        // Neue Buchebschreibung in der Datenbank speichern
        BasicDBObject buchbeschreibung = new BasicDBObject();
        buchbeschreibung.append(ISBN, isbn);
        buchbeschreibung.append(TITLE, title);
        buchbeschreibung.append(AUTHOR, author);
        buchbeschreibung.append(EXEMPLAROWNER, neueBS.exemplarOwner);
        
        // Die neue Buchbeschreibung in der Buchbeschreibungen-Collection speichern
        getBuchBeschreibungenCollection().insert(buchbeschreibung);
        
        // R�ckgabe der neuen Buchbeschreibung
        return neueBS;
        
    }
    
    @Override
    public Collection<BuchBeschreibung> findBuchBeschreibung() {
        return asCollection(getBuchBeschreibungenCollection().find());
    }
    
    @Override
    public Collection<BuchBeschreibung> findByISBN(final String isbn) {
        return asCollection(queryForIsbn(isbn));
    }
    
    @Override
    public void delete(final String isbn) {
        
        DBCursor cursor = queryForIsbn(isbn);
        
        try {
            
            if(!cursor.hasNext()) {
                throw new NoSuchElementException("Keine Buchbeschreibung zu der ISBN: " +isbn+ " gefunden.");
            }
            
            getBuchBeschreibungenCollection().remove(cursor.next());
        }
        finally {
            cursor.close();
        }
        
    }
    
    /**
     * Findet alle Buchbeschreibungen in der Datenbank.
     * 
     * Gibt die gefundenen Buchbeschreibungen als Collection zur�ck.
     * 
     * @param find alle Buchebschreibungen in der DB
     * @return Collection der Buchbeschreibungen
     */
    private Collection<BuchBeschreibung> asCollection(final DBCursor find) {
        
        try {
            
            List<BuchBeschreibung> buchbeschreibungen = Lists.newArrayList();
            
            for(DBObject dbObject : find) {
                String isbn = (String)dbObject.get(ISBN);
                String title = (String)dbObject.get(TITLE);
                String author = (String)dbObject.get(AUTHOR);
                buchbeschreibungen.add(new BuchBeschreibung(isbn, title, author));
            }
            return buchbeschreibungen;
        }
        finally {
            find.close();
        }
    }
    
    /**
     * Gibt eine Datenbank-Collection mit den Buchbeschreibungen,
     * welche zur �bergebenen ISBN passen, zur�ck.
     * 
     * @param isbn ISBN der gesuchten Buchbeschreibung
     * @return Datenbank-Collection
     */
    private DBCursor queryForIsbn(final String isbn) {
        
        BasicDBObject query = new BasicDBObject();
        query.append(ISBN, isbn);
        
        return getBuchBeschreibungenCollection().find(query);
    }
    
    @Override
    public BuchBeschreibung createBeschreibung(final String isbn, final String title, final String author) {
        BasicDBObject buchbeschreibung = new BasicDBObject();
        buchbeschreibung.append(ISBN, isbn);
        buchbeschreibung.append(TITLE, title);
        buchbeschreibung.append(AUTHOR, author);
        
        // Die neue Buchbeschreibung in der Buchbeschreibungen-Collection speichern
        getBuchBeschreibungenCollection().insert(buchbeschreibung);
        return new BuchBeschreibung(isbn, title, author);
    }
}

