package se.danielkonsult.www.kvadratab;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;

import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;

/**
 * Test database, instantiating itself with a different name than the
 * production database.
 */
public class KvadratTestDb extends KvadratDb {

    // Constants

    public static String DATABASE_NAME = "KvadratTest.db";

    // Constructor

    public KvadratTestDb(Context context) {
        super(context, DATABASE_NAME);
    }

    // Public methods
}
