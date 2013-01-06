package edu.hm.hafner.shareit.util;

import java.io.IOException;

import com.mongodb.DB;
import com.mongodb.Mongo;

import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;

/**
 * Stellt eine NoSQL Datenbank (Mongo DB) zur Verfügung, die nach dem Beenden
 * der Anwendung wieder zerstört wird. Daher wird diese Datenbank am besten nur
 * in Unit-und Integrationstests genutzt.
 *
 * @see <a href="http://www.mongodb.org">Mongo DB</a>
 * @author Ulli Hafner
 */
public final class DatabaseFactory {
    /** Singleton zum Zugriff auf die Datenbank. */
    public static final DatabaseFactory INSTANCE = new DatabaseFactory();
    
    private DB db;
    
    private DatabaseFactory() {
        try {
            MongodForTestsFactory factory = new MongodForTestsFactory();
            Mongo mongo = factory.newMongo();
            db = factory.newDB(mongo);
        }
        catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }
    
    /**
     * Liefert die Datenbank zurück.
     *
     * @return die Datenbank
     * @see #reset() um die Datenbank zu leeren
     */
    public DB getDatabase() {
        return db;
    }
    
    /**
     * Setzt die Datenbank wieder in den Ausgangszustand zurück.
     */
    public void reset() {
        db.dropDatabase();
    }
}

