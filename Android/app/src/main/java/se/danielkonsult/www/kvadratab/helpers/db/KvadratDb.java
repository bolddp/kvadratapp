package se.danielkonsult.www.kvadratab.helpers.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.repositories.office.DefaultOfficeDataRepository;
import se.danielkonsult.www.kvadratab.repositories.office.OfficeDataRepository;

/**
 * Handles the database that stores data that is downloaded from the
 * Kvadrat web page.
 */
public class KvadratDb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Kvadrat.db";
    private static final int DATABASE_VERSION = 1;

    private OfficeDataRepository _officeDataRepository;

    // Protected methods

    protected OfficeDataRepository getOfficeDataRepository() {
        if (_officeDataRepository == null)
            _officeDataRepository = new DefaultOfficeDataRepository(this);
        return _officeDataRepository;
    }

    // Constructor

    public KvadratDb() {
        super(AppCtrl.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    protected KvadratDb(Context context, String databaseName){
        super(context, databaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbSpec.ConsultantEntry.SQL_CREATE);
        db.execSQL(DbSpec.OfficeEntry.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbSpec.OfficeEntry.SQL_DELETE);
        db.execSQL(DbSpec.ConsultantEntry.SQL_DELETE);

        onCreate(db);
    }

    // Database operation methods

    public void insertOffice(final OfficeData office, final DbOperationListener listener) {
        getOfficeDataRepository().insert(office, listener);
    }

    public void getAllOffices(final DbDataListener<OfficeData[]> listener) {
        getOfficeDataRepository().getAll(listener);
    }
}
