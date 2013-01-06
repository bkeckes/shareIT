package edu.hm.hafner.shareit.model;



/**
 * Repräsentiert ein Buchexemplar einer Buchbeschreibung.
 * 
 * @author Cookie
 */
public class BuchExemplar {
    
    // Attribute der Buchbeschreibung
    
    /** Repräsentiert die Buchbeschreibung des Buchexemplars */
    private final BuchBeschreibung beschreibung;
    
    /** Repräsentiert den Besitzer des Buchexemplars */
    private final Benutzer besitzer;
    
    /** Repräsentiert den aktuelle Leiher des Buchexemplars */
    private final Benutzer leiher;
    
    /** Repräsentiert die ID des Buchexemplars */
    private final int id;
    
    /** Repräsentiert die Zurückforderungs-Markierung des Buchexemplar-Besitzers */
    private boolean isZurueckgefordert = false;
    
    private String emailBesitzer;
    private String emailLeiher;
    private String beschreibungIsbn;
    
    /**
     * Konstruktor, erzeugt ein neues Buchexemplar.
     * 
     * @param beschreibung Buchbeschreibung des Buchexemplars
     * @param besitzer Besitzer des Buchexemplars
     * @param leiher aktueller Leiher des Buchexemplars
     * @param id ID des Buchexemplars
     */
    public BuchExemplar(final BuchBeschreibung beschreibung, final Benutzer besitzer, final Benutzer leiher, final int id) {
        this.beschreibung = beschreibung;
        this.besitzer = besitzer;
        this.leiher = leiher;
        this.id = id;
    }
    
    public BuchExemplar(final String beschreibung, final String emailBesitzer, final String emailLeiher){
        beschreibungIsbn = beschreibung;
        this.emailBesitzer = emailBesitzer;
        this.emailLeiher = emailLeiher;
        besitzer=null;
        id = 0;
        leiher = null;
        this.beschreibung=null;
    }
    
    public String getIsbn(){
        return beschreibungIsbn;
    }
    
    public String getBesitzerEmail(){
        return emailBesitzer;
    }
    public String getLeiherEmail(){
        return emailLeiher;
    }
    /**
     * Returns the isZurueckgefordert.
     * 
     * @return the isZurueckgefordert
     */
    public boolean isZurueckgefordert() {
        return isZurueckgefordert;
    }
    
    /**
     * Sets the isZurueckgefordert to the specified value.
     * 
     * @param isZurueckgefordert the value to set
     */
    public void setZurueckgefordert(final boolean isZurueckgefordert) {
        this.isZurueckgefordert = isZurueckgefordert;
    }
    
    /**
     * Returns the beschreibung.
     * 
     * @return the beschreibung
     */
    public BuchBeschreibung getBeschreibung() {
        return beschreibung;
    }
    
    /**
     * Returns the besitzer.
     * 
     * @return the besitzer
     */
    public Benutzer getBesitzer() {
        return besitzer;
    }
    
    /**
     * Returns the leiher.
     * 
     * @return the leiher
     */
    public Benutzer getLeiher() {
        return leiher;
    }
    
    /**
     * Returns the id.
     * 
     * @return the id
     */
    public int getId() {
        return id;
    }
    
    
    
}

