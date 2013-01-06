package edu.hm.hafner.shareit.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Repräsentiert die Beschreibung eines Buches.
 * 
 * @author Cookie
 */
public class BuchBeschreibung {
    
    // Attribute der Buchbeschreibung
    
    /** Repräsentiert die IDBN des Buches */
    private final String isbn;
    
    /** Repräsentiert den Buch-Titel */
    private final String title;
    
    /** Repräsentiert den Autor des Buches */
    private final String author;
    
    /** Repräsentier die Anzahl der verfügbaren Buch-Exemplare */
    private int exemplars;
    
    /** Repräsentiert die Besitzer der jeweiligen Exemplare */
    public Map<Integer, String> exemplarOwner = new HashMap<Integer, String>();
    
    /**
     * Konstruktor, erzeugt eine Buchbeschreibung.
     * 
     * Setzt die Anzahl der Buchexemplare auf den Standardwert (1).
     * 
     * @param isbn ISBN des Buches
     * @param title Buchtitel
     * @param author Autor des Buches
     */
    public BuchBeschreibung(final String isbn, final String title, final String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        
        // Wenn eine neue Buchbeschreibung erzeugt wird, gibt es mindestens
        // einen Buchexemplar-Besitzer
        exemplars = 1;
    }
    
    /**
     * Returns the exemplarOwner.
     * 
     * @return the exemplarOwner
     */
    public Map<Integer, String> getExemplarOwner() {
        return exemplarOwner;
    }
    
    /**
     * Returns the isbn.
     * 
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }
    
    /**
     * Returns the title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Returns the author.
     * 
     * @return the author
     */
    public String getAuthor() {
        return author;
    }
    
    /**
     * Returns the exemplars.
     * 
     * @return the exemplars
     */
    public int getExemplars() {
        return exemplars;
    }
    
    /**
     * Sets the exemplars to the specified value.
     * 
     * @param exemplars the value to set
     */
    public void setExemplars(final int exemplars) {
        this.exemplars = exemplars;
    }
}

