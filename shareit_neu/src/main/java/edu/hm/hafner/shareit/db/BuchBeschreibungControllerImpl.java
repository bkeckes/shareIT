package edu.hm.hafner.shareit.db;

import java.util.Collection;
import java.util.Collections;
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
 * Erzeugt, findet und löscht Buchbeschreibungen.
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
        
        // Alle Buchbeschreibungen mit der übergebenen ISBN
        Collection<BuchBeschreibung> existing = findByISBN(isbn);
        
        // Wenn es schon eine Buchbeschreibung mit der ISBN gibt
        if(!existing.isEmpty()) {
            
            // Dann wird die Anzahl der Exemplare von der Buchbeschreibung erhöt
            // und die Besitzer-Email in die Besitzer-Map geschrieben
            Iterator<BuchBeschreibung> iter = existing.iterator();
            iter.next().setExemplars(iter.next().getExemplars() + 1);
            iter.next().getExemplarOwner().put(iter.next().getExemplars(), emailBesitzer);
            
            // und die Buchbeschreibung zurückgegeben
            return iter.next();
        }
        
        // Neue Buchbeschreibung erzeugen und für das erste Buchexemplar
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
        
        // Rückgabe der neuen Buchbeschreibung
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
     * Gibt die gefundenen Buchbeschreibungen als Collection zurück.
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
     * welche zur übergebenen ISBN passen, zurück.
     * 
     * @param isbn ISBN der gesuchten Buchbeschreibung
     * @return Datenbank-Collection
     */
    private DBCursor queryForIsbn(final String isbn) {
        
        BasicDBObject query = new BasicDBObject();
        query.append(ISBN, isbn);
        
        return getBuchBeschreibungenCollection().find(query);
    }
    
    
    private DBCursor queryForTitle(final String title){
        BasicDBObject query = new BasicDBObject();
        query.append(TITLE, title);
        
        return getBuchBeschreibungenCollection().find(query);
    }
    
    private DBCursor queryForAuthor(final String author){
        BasicDBObject query = new BasicDBObject();
        query.append(AUTHOR, author);
        
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
    
    /**
     * Hier wird anhand der Buchbeschreibung eine Liste der ISBN zurück gegeben.
     * Da Titel und Autor in verschiedenen Büchern gleich sein können muss es eine Liste sein.
     * 
     * Die Beschreibung wird so überprüft:
     * -wurde eine ISBN angegeben?:
     *      der Fall ist klar. Einfach ISBN zurück geben
     * -wurde ein Titel angegeben?:
     *      Alle ISBN zurück geben die zu diesem Titel gehören
     * -wurde ein Autor angegeben?:
     *      Alle ISBN zurück geben die zu diesem Autor gehören
     * 
     * @return isbnCollection
     */
    @Override
    public Collection<String> findISBNByBuchBeschreibung(final BuchBeschreibung beschreibung) {
        
        Collection<String> isbn = Lists.newArrayList();
        Collection<BuchBeschreibung> beschList;
        Iterator iter;
        
        //Wenn ISBN angegeben ist (kommt wohl nie vor)
        if(beschreibung.getIsbn()!=null) {
            isbn.add(beschreibung.getIsbn());
            return isbn;
        }
        
        if(beschreibung.getTitle()!=null){
            beschList = asCollection(queryForTitle(beschreibung.getTitle()));
            iter = beschList.iterator();
            if(!iter.hasNext()){
                throw new IllegalArgumentException("zu diesem Titel wurde keine ISBN gefunden");
            }
            while(iter.hasNext()){
                isbn.add(((BuchBeschreibung)iter.next()).getIsbn());
            }
            return isbn;
        }
        else if(beschreibung.getAuthor()!=null){
            beschList = asCollection(queryForAuthor(beschreibung.getAuthor()));
            
            iter = beschList.iterator();
            if(!iter.hasNext()){
                throw new IllegalArgumentException("zu diesem Autor wurde keine ISBN gefunden");
            }
            while(iter.hasNext()){
                isbn.add(((BuchBeschreibung)iter.next()).getIsbn());
            }
            return isbn;
        }
        
        return Collections.emptyList();
    }
}

