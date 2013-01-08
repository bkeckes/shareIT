package edu.hm.hafner.shareit.model;


/**
 * Repräsentiert die Beschreibung eines Buches.
 * 
 * @author Keckes, Kuchenbecker
 */
public class BuchBeschreibung {
    
    // Attribute der Buchbeschreibung
    
    /** Repräsentiert die IDBN des Buches */
    private final String isbn;
    
    /** Repräsentiert den Buch-Titel */
    private final String title;
    
    /** Repräsentiert den Autor des Buches */
    private final String author;
    
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
}

