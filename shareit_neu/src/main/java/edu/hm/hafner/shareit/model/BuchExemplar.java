package edu.hm.hafner.shareit.model;



/**
 * Repräsentiert ein Buchexemplar einer Buchbeschreibung.
 * 
 * @author Cookie
 */
public class BuchExemplar {
    
    // Attribute der Buchbeschreibung
    
    /** email Adresse des Besitzers */
    private final String emailBesitzer;
    
    /** email Adresse des Leihers */
    private final String emailLeiher;
    /** isbn */
    private final String beschreibungIsbn;
    /** zeigt an ob das Buch vom Besitzer zurück gefordert ist */
    private final boolean zurueckGefordert;
    
    /**
     * 
     * Creates a new instance of {@link BuchExemplar}.
     * @param beschreibung
     * @param emailBesitzer
     * @param emailLeiher
     * @param zurueck
     */
    public BuchExemplar(final String beschreibung, final String emailBesitzer, final String emailLeiher, final boolean zurueck){
        beschreibungIsbn = beschreibung;
        this.emailBesitzer = emailBesitzer;
        this.emailLeiher = emailLeiher;
        zurueckGefordert = zurueck;
    }
    
    /**
     * 
     * getZurueckGefordert
     * @return zurueckGefordert
     */
    public boolean getZurueckGefordert(){
        return zurueckGefordert;
    }
    
    /**
     * 
     * getIsbn
     * @return isbn
     */
    public String getIsbn(){
        return beschreibungIsbn;
    }
    
    /**
     * 
     * getBesitzerEmail
     * @return email
     */
    public String getBesitzerEmail(){
        return emailBesitzer;
    }
    
    /**
     * 
     * getLeiherEmail
     * @return email
     */
    public String getLeiherEmail(){
        return emailLeiher;
    }
    
    
    
    
}

