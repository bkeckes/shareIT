package edu.hm.hafner.shareit;

import java.util.Collection;
import java.util.Iterator;

import edu.hm.hafner.shareit.db.BuchBeschreibungController;
import edu.hm.hafner.shareit.db.BuchBeschreibungControllerImpl;
import edu.hm.hafner.shareit.db.BuchExemplarController;
import edu.hm.hafner.shareit.db.BuchExemplarControllerImpl;
import edu.hm.hafner.shareit.model.Benutzer;
import edu.hm.hafner.shareit.model.BuchBeschreibung;
import edu.hm.hafner.shareit.model.BuchExemplar;

/**
 * Usecase Controller für die Anwendungsfälle, welche die Buchbeschreibungen
 * und die Buchexemplare betreffen
 * 
 * @author Cookie
 */
public class BuchVerwaltungUsecaseControllerImpl implements BuchVerwaltungUsecaseController {
    
    /** Neuer Controller für die Buchbeschreibung */
    private final BuchBeschreibungController beschreibungController = new BuchBeschreibungControllerImpl();
    
    /** Neuer Controller für das Buchexemplar */
    private final BuchExemplarController buchexemplarController = new BuchExemplarControllerImpl();
    
    @Override
    /**
     * Wenn die Buchbeschreibung schon existiert soll nur ein neues Buchexemplar angelegt werden.
     * Andernfalls soll erst eine Buchbeschreibung angelegt werden und dann das Exemplar
     */
    public BuchExemplar buchZurVerfuegungStellen(final String isbn, final String author, final String title, final String emailBesitzer) {
        
        BuchBeschreibung besch;
        if(beschreibungController.findByISBN(isbn).isEmpty()){
            besch = beschreibungController.createBeschreibung(isbn, title, author);
        }
        else{
            besch = beschreibungController.findByISBN(isbn).iterator().next();
        }
        
        return buchexemplarController.createExemplar(besch.getIsbn(), emailBesitzer);//beschreibungController.create(isbn, title, author, emailBesitzer);
    }
    @Override
    public void zeigeAlleBuecher(){
        Iterator iter = buchexemplarController.getAllBooks().iterator();
        
        while(iter.hasNext()){
            BuchExemplar b = (BuchExemplar)iter.next();
            BuchBeschreibung besch = beschreibungController.findByISBN(b.getIsbn()).iterator().next();
            System.out.println("Buch: "+besch.getTitle()+", "+besch.getAuthor()+" gehört zu: "+b.getBesitzerEmail()+" ausgeliehen von: "+b.getLeiherEmail());
        }
        
    }
    @Override
    public BuchExemplar buchAusleihen(final BuchBeschreibung buchbeschreibung, final int id, final Benutzer ausleiher) {
        return buchexemplarController.create(buchbeschreibung, ausleiher, id);
    }
    
    @Override
    public boolean buchZurueckgeben(final int id) {
        
        BuchExemplar buchZurueck = (BuchExemplar)buchexemplarController.findById(id);
        
        if(!buchZurueck.isZurueckgefordert()) {
            
            BuchBeschreibung beschreibung = (BuchBeschreibung)beschreibungController.findByISBN(buchZurueck.getBeschreibung().getIsbn());
            
            if(beschreibung == null) {
                beschreibungController.create(
                        buchZurueck.getBeschreibung().getIsbn(),
                        buchZurueck.getBeschreibung().getTitle(),
                        buchZurueck.getBeschreibung().getAuthor(),
                        buchZurueck.getBesitzer().getEmail());
            }
            else {
                beschreibung.getExemplarOwner().put(id, buchZurueck.getBesitzer().getEmail());
            }
            
            buchexemplarController.delete(id);
        }
        
        return true;
    }
    
    @Override
    public BuchExemplar buchZurueckfordern(final int id) {
        
        BuchExemplar buchExemplar = (BuchExemplar)buchexemplarController.findById(id);
        buchExemplar.setZurueckgefordert(true);
        
        return buchExemplar;
    }
    
    @Override
    public boolean buchEntfernen(final String isbn, final Benutzer besitzer) {
        
        BuchBeschreibung beschreibung = (BuchBeschreibung)beschreibungController.findByISBN(isbn);
        
        Collection<BuchExemplar> exemplare = buchexemplarController.findBuchExemplare();
        Iterator<BuchExemplar> iter = exemplare.iterator();
        
        while(iter.hasNext()) {
            if(iter.next().getBeschreibung().equals(beschreibung)) {
                throw new IllegalStateException("Das Buch kann nicht entfernt werden, da es momentan entliehen ist.");
            }
        }
        
        if(beschreibung.getExemplars() < 1) {
            beschreibungController.delete(isbn);
        }
        else {
            for(int i=0; i<beschreibung.getExemplars(); i++) {
                if(beschreibung.getExemplarOwner().get(i).equals(besitzer.getEmail())) {
                    beschreibung.getExemplarOwner().remove(i);
                    beschreibung.setExemplars(beschreibung.getExemplars() - 1);
                    break;
                }
            }
        }
        return true;
    }
    @Override
    public BuchBeschreibung getBuchBeschreibung(final String isbn) {
        
        return beschreibungController.findByISBN(isbn).iterator().next();
    }
    @Override
    public BuchExemplar buchAusleihen(final String isbn, final String leiher) {
        if(buchexemplarController.findByIsbn(isbn).isEmpty()) {
            throw new IllegalStateException("Das Buch kann nicht ausgeliehen werden da es nicht in der DB zu finden ist");
        }
        BuchExemplar buch = null;
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
        if(buch==null) {
            throw new IllegalStateException("Alle Bücher dieses Types sind im Moment entliehen");
        }
        
        
        return buchexemplarController.rentExemplar(isbn, buch.getBesitzerEmail(), leiher);
    }
    @Override
    public BuchExemplar buchZurueckGeben(final String isbn, final String emailLeiher) {
        
        return buchexemplarController.returnExemplar(isbn, emailLeiher);
    }
    
}

