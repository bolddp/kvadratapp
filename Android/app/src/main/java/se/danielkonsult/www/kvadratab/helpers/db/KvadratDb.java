package se.danielkonsult.www.kvadratab.helpers.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.NotificationData;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.repositories.consultant.ConsultantCompetenceRepository;
import se.danielkonsult.www.kvadratab.repositories.consultant.ConsultantDataRepository;
import se.danielkonsult.www.kvadratab.repositories.consultant.DefaultConsultantCompetenceRepository;
import se.danielkonsult.www.kvadratab.repositories.consultant.DefaultConsultantDataRepository;
import se.danielkonsult.www.kvadratab.repositories.notification.DefaultNotificationRepository;
import se.danielkonsult.www.kvadratab.repositories.notification.NotificationRepository;
import se.danielkonsult.www.kvadratab.repositories.office.DefaultOfficeDataRepository;
import se.danielkonsult.www.kvadratab.repositories.office.OfficeDataRepository;
import se.danielkonsult.www.kvadratab.repositories.tag.DefaultTagDataRepository;
import se.danielkonsult.www.kvadratab.repositories.tag.TagDataRepository;
import se.danielkonsult.www.kvadratab.services.notification.Notification;

/**
 * Handles the database that stores data that is downloaded from the
 * Kvadrat web page.
 */
public class KvadratDb extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Kvadrat.db";
    // public static final int DATABASE_VERSION = 1;
    public static final int DATABASE_VERSION = 2;

    private OfficeDataRepository _officeDataRepository;
    private TagDataRepository _tagDataRepository;
    private ConsultantDataRepository _consultantDataRepository;
    private NotificationRepository _notificationRepository;
    private ConsultantCompetenceRepository _consultantCompetenceRepository;

    // Constructor

    public KvadratDb() {
        super(AppCtrl.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    protected KvadratDb(Context context, String databaseName){
        super(context, databaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL(DbSpec.OfficeEntry.SQL_CREATE);
        db.execSQL(DbSpec.TagEntry.SQL_CREATE);
        db.execSQL(DbSpec.ConsultantEntry.SQL_CREATE);
        db.execSQL(DbSpec.ConsultantTagEntry.SQL_CREATE);
        db.execSQL(DbSpec.ConsultantCompetenceEntry.SQL_CREATE);
        db.execSQL(DbSpec.NotificationEntry.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int version = oldVersion;version < newVersion-1;version++){
            if (version == 2){
                // TBD Upgrade from version 2 to version 3
            }
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    // Protected methods

    public OfficeDataRepository getOfficeDataRepository() {
        if (_officeDataRepository == null)
            _officeDataRepository = new DefaultOfficeDataRepository(this);
        return _officeDataRepository;
    }

    public TagDataRepository getTagDataRepository(){
        if (_tagDataRepository == null)
            _tagDataRepository = new DefaultTagDataRepository(this);
        return _tagDataRepository;
    }


    public ConsultantDataRepository getConsultantDataRepository() {
        if (_consultantDataRepository == null)
            _consultantDataRepository = new DefaultConsultantDataRepository(this);

        return _consultantDataRepository;
    }

    public NotificationRepository getNotificationRepository(){
        if (_notificationRepository == null)
            _notificationRepository = new DefaultNotificationRepository(this);

        return _notificationRepository;
    }

    public ConsultantCompetenceRepository getConsultantCompetenceRepository() {
        if (_consultantCompetenceRepository == null)
            _consultantCompetenceRepository = new DefaultConsultantCompetenceRepository(this);

        return _consultantCompetenceRepository;
    }
}
