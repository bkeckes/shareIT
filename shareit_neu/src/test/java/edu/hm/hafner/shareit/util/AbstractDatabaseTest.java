package edu.hm.hafner.shareit.util;

import org.junit.Before;

/**
 * Basisklasse f�r Tests die die Datenbank zur�cksetzen m�ssen.
 */
public abstract class AbstractDatabaseTest {
    /**
     * Setzt die Datenbank zur�ck.
     */
    @Before
    public void clearDatabase() {
        DatabaseFactory.INSTANCE.reset();
    }
}
