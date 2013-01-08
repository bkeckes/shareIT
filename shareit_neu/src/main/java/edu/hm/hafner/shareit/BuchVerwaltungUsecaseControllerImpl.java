package edu.hm.hafner.shareit;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.common.collect.Lists;

import edu.hm.hafner.shareit.db.BuchBeschreibungController;
import edu.hm.hafner.shareit.db.BuchBeschreibungControllerImpl;
import edu.hm.hafner.shareit.db.BuchExemplarController;
import edu.hm.hafner.shareit.db.BuchExemplarControllerImpl;
import edu.hm.hafner.shareit.model.BuchBeschreibung;
import edu.hm.hafner.shareit.model.BuchExemplar;

/**
 * Usecase Controller f�r die Anwendungsf�lle, welche die Buchbeschreibungen
 * und die Buchexemplare betreffen
 * 
 * @author Keckes
 * @author Kuchenbecker
 */
public class BuchVerwaltungUsecaseControllerImpl implements BuchVerwaltungUsecaseController {
    
    /** Neuer Controller f�r die Buchbeschreibung */
    private final BuchBeschreibungController beschreibungController = new BuchBeschreibungControllerImpl();
    
    /** Neuer Controller f�r das Buchexemplar */
    private final BuchExemplarController buchexemplarController = new BuchExemplarControllerImpl();
    
    /**
     * Buch wird zur Verf�gung gestellt.
     * Eine Buchbeschreibung wird nur neu angelegt wenn sie noch nicht existiert.
     *
     * @param isbn
     * @param author
     * @param title
     * @param emailBesitzer
     * @return buchExemplar
     */
    @Override
    public BuchExemplar buchZurVerfuegungStellen(final String isbn, final String author, final String title, final String emailBesitzer) {
        
        BuchBeschreibung besch;
        
        //Wenn Beschreibung noch nicht vorhanden...
        if(beschreibungController.findByISBN(isbn).isEmpty()){
            
            //Beschreibung erstellen
            besch = beschreibungController.createBeschreibung(isbn, title, author);
        }
        //Wenn Beschreibung vorhanden
        else{
            
            //bestehende Beschreibung verwenden
            besch = beschreibungController.findByISBN(isbn).iterator().next();
        }
        
        return buchexemplarController.createExemplar(besch.getIsbn(), emailBesitzer);//beschreibungController.create(isbn, title, author, emailBesitzer);
    }
    
    /**
     * Buch wird ausgeliehen wenn es gefunden wird, ansonsten wird ein leeres BuchExemplar zur�ck gegeben
     * 
     * @param isbn
     * @param leiher
     * @return
     */
    @Override
    public BuchExemplar buchAusleihen(final String isbn, final String leiher) {
        
        BuchExemplar buch = null;
        try{
            
            Iterator iter = buchexemplarController.findByIsbn(isbn).iterator();
            while(iter.hasNext()){
                buch = (BuchExemplar)iter.next();
                
                //Wenn das Buch noch nicht entliehen
                if(buch.getLeiherEmail()==null){
                    break;
                }
                else {
                    buch=null;
                }
                
            }
        }
        catch(NoSuchElementException e){
            System.out.println("Buch kann nicht ausgeliehen werden.");
            e.printStackTrace();
            return new BuchExemplar(null, null, null);
        }
        if(buch==null) {
            throw new IllegalStateException("Alle B�cher dieses Types sind im Moment entliehen");
        }
        
        
        return buchexemplarController.rentExemplar(isbn, buch.getBesitzerEmail(), leiher);
    }
    
    /**
     * Buch wird zur�ck gegeben
     * 
     * @param isbn
     * @param emailLeiher
     * @return
     */
    @Override
    public BuchExemplar buchZurueckGeben(final String isbn, final String emailLeiher) {
        
        return buchexemplarController.returnExemplar(isbn, emailLeiher);
        
    }
    
    /**
     * Buch wird als zur�ckgefordert markiert.
     * 
     * @param isbn
     * @param besitzer
     * @param leiher
     * @return exemplar
     */
    @Override
    public BuchExemplar buchZurueckfordern(final String isbn, final String besitzer, final String leiher) {
        
        return buchexemplarController.reclaimExemplar(isbn, besitzer, leiher);
    }
    
    /**
     * Buch wird aus Liste entfernt wenn es nicht ausgeliehen ist
     * 
     * @param isbn
     * @param besitzer
     */
    @Override
    public void buchEntfernen(final String isbn, final String besitzer) {
        
        //Wenn das Buch nicht verliehen ist wird es gel�scht.
        buchexemplarController.delete(isbn, besitzer);
    }
    
    /**
     * Buch nach Beschreibung suchen.
     * Es k�nnen bei Bedarf auch nur verf�gbare B�chern angezeigt werden
     *
     * @param beschreibung
     * @param nurVerfuegbar
     * @return collection
     */
    @Override
    public Collection<BuchExemplar> buchSuchen(final BuchBeschreibung beschreibung, final boolean nurVerfuegbar) {
        
        Collection<BuchExemplar> collection = Lists.newArrayList();
        
        //Wenn eine leere Beschreibung �bergeben wird
        if(beschreibung==null){
            
            //Alle B�cher werden geladen
            collection = buchexemplarController.getAllBooks();
            
            //Alle B�cher zur�ck geben
            if(!nurVerfuegbar){
                return collection;
            }
            //Nur die B�cher die noch verf�gbar sind
            else{
                return gibNurVerfuegbareBuecher(collection);
            }
        }
        
        //Wenn eine beschreibung angegeben ist
        else{
            
            //Wenn keine isbn angegeben ist muss in der Beschreibung nach der isbn gesucht werden
            if(beschreibung.getIsbn()==null){
                Collection<String> isbn = beschreibungController.findISBNByBuchBeschreibung(beschreibung);
                
                Iterator iter = isbn.iterator();
                String temp;
                if(!iter.hasNext()){
                    throw new IllegalArgumentException("zu dieser Suche gibt es kein Ergebnis");
                }
                while(iter.hasNext()){
                    temp = (String)iter.next();
                    collection.addAll(buchexemplarController.findByIsbn(temp));
                }
            }
            else{
                collection = buchexemplarController.findByIsbn(beschreibung.getIsbn());
            }
            
            //Hier wird nach verf�gbar oder nicht gefiltert.
            if(!nurVerfuegbar) {
                return collection;
            }
            else {
                return gibNurVerfuegbareBuecher(collection);
            }
        }
    }
    
    /**
     * Eigene B�cher werden auf der Konsole ausgegeben und als Liste zur�ck gegeben
     * 
     * @param besitzer
     * @return collection
     */
    @Override
    public Collection<BuchExemplar> eigeneBuecher(final String besitzer) {
        Collection<BuchExemplar> collection = buchexemplarController.findByBesitzer(besitzer);
        Iterator iter = collection.iterator();
        
        while(iter.hasNext()){
            BuchExemplar b = (BuchExemplar)iter.next();
            BuchBeschreibung besch = beschreibungController.findByISBN(b.getIsbn()).iterator().next();
            System.out.println("Buch: "+besch.getTitle()+", "+besch.getAuthor()+" geh�rt zu: "+b.getBesitzerEmail()+" ausgeliehen von: "+b.getLeiherEmail()+", gefordert: "+b.getZurueckGefordert());
        }
        return collection;
    }
    
    /**
     * B�cher werden angezeigt die man selber leiht.
     * 
     * @param leiher
     * @return collection
     */
    @Override
    public Collection<BuchExemplar> eigeneLeihe(final String leiher) {
        Collection<BuchExemplar> collection = buchexemplarController.findByLeiher(leiher);
        Iterator iter = collection.iterator();
        
        while(iter.hasNext()){
            BuchExemplar b = (BuchExemplar)iter.next();
            BuchBeschreibung besch = beschreibungController.findByISBN(b.getIsbn()).iterator().next();
            System.out.println("Buch: "+besch.getTitle()+", "+besch.getAuthor()+" geh�rt zu: "+b.getBesitzerEmail()+" ausgeliehen von: "+b.getLeiherEmail()+", gefordert: "+b.getZurueckGefordert());
        }
        return collection;
    }
    
    /**
     * Gibt die Buchbeschreibung zu einer ISBN aus
     * 
     * @param isbn
     * @return buchbeschreibung
     */
    @Override
    public BuchBeschreibung getBuchBeschreibung(final String isbn) {
        
        return beschreibungController.findByISBN(isbn).iterator().next();
    }
    
    /**
     * Gibt die Anzahl aller B�cher im System aus
     * 
     * @return
     */
    @Override
    public int buchZaehler() {
        
        Iterator iter = buchexemplarController.getAllBooks().iterator();
        int i=0;
        while(iter.hasNext()){
            iter.next();
            i++;
        }
        return i;
    }
    
    /**
     * 
     * Gibt eine B�chercollection zur�ck mit B�chern die noch nicht ausgeliehen sind
     * @param collection
     * @return verf�gbareB�cher
     */
    private Collection<BuchExemplar> gibNurVerfuegbareBuecher(final Collection<BuchExemplar> collection){
        
        Collection<BuchExemplar> verfuegbareExemplare = Lists.newArrayList();
        
        //verfuegbareExemplare.clear();
        
        Iterator<BuchExemplar> iter;
        BuchExemplar exemplar;
        
        
        if(collection.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        iter = collection.iterator();
        while(iter.hasNext()){
            exemplar = iter.next();
            if(exemplar.getLeiherEmail()==null) {
                verfuegbareExemplare.add(exemplar);
            }
        }
        return verfuegbareExemplare;
    }
    
    /**
     * zeigt alle Buecher in der Konsole an
     * 
     */
    @Override
    public void zeigeAlleBuecher(){
        Iterator iter = buchexemplarController.getAllBooks().iterator();
        
        while(iter.hasNext()){
            BuchExemplar b = (BuchExemplar)iter.next();
            BuchBeschreibung besch = beschreibungController.findByISBN(b.getIsbn()).iterator().next();
            System.out.println("Buch: "+besch.getTitle()+", "+besch.getAuthor()+" geh�rt zu: "+b.getBesitzerEmail()+" ausgeliehen von: "+b.getLeiherEmail()+", gefordert: "+b.getZurueckGefordert());
        }
        
    }
    
}

