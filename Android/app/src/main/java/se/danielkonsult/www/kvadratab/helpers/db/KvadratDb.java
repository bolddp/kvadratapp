package se.danielkonsult.www.kvadratab.helpers.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.repositories.consultant.ConsultantDataRepository;
import se.danielkonsult.www.kvadratab.repositories.consultant.DefaultConsultantDataRepository;
import se.danielkonsult.www.kvadratab.repositories.office.DefaultOfficeDataRepository;
import se.danielkonsult.www.kvadratab.repositories.office.OfficeDataRepository;
import se.danielkonsult.www.kvadratab.repositories.tag.DefaultTagDataRepository;
import se.danielkonsult.www.kvadratab.repositories.tag.TagDataRepository;

/**
 * Handles the database that stores data that is downloaded from the
 * Kvadrat web page.
 */
public class KvadratDb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Kvadrat.db";
    private static final int DATABASE_VERSION = 1;

    private OfficeDataRepository _officeDataRepository;
    private TagDataRepository _tagDataRepository;
    private ConsultantDataRepository _consultantDataRepository;

    // Protected methods

    protected OfficeDataRepository getOfficeDataRepository() {
        if (_officeDataRepository == null)
            _officeDataRepository = new DefaultOfficeDataRepository(this);
        return _officeDataRepository;
    }

    protected TagDataRepository getTagDataRepository(){
        if (_tagDataRepository == null)
            _tagDataRepository = new DefaultTagDataRepository(this);
        return _tagDataRepository;
    }


    protected ConsultantDataRepository getConsultantDataRepository() {
        if (_consultantDataRepository == null)
            _consultantDataRepository = new DefaultConsultantDataRepository(this);

        return _consultantDataRepository;
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
        db.execSQL(DbSpec.OfficeEntry.SQL_CREATE);
        db.execSQL(DbSpec.TagEntry.SQL_CREATE);
        db.execSQL(DbSpec.ConsultantEntry.SQL_CREATE);
        db.execSQL(DbSpec.ConsultantTagEntry.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbSpec.OfficeEntry.SQL_DELETE);
        db.execSQL(DbSpec.TagEntry.SQL_DELETE);
        db.execSQL(DbSpec.ConsultantEntry.SQL_DELETE);
        db.execSQL(DbSpec.ConsultantTagEntry.SQL_DELETE);

        onCreate(db);
    }

    // Database operation methods

    public void getOfficeById(int id, final DbDataListener<OfficeData> listener){
        getOfficeDataRepository().getById(id, listener);
    }

    public void getAllOffices(final DbDataListener<OfficeData[]> listener) {
        getOfficeDataRepository().getAll(listener);
    }

    public void insertOffice(final OfficeData office, final DbOperationListener listener) {
        getOfficeDataRepository().insert(office, listener);
    }

    public void getTagById(int id, final DbDataListener<TagData> listener){
        getTagDataRepository().getById(id, listener);
    }

    public void getAllTags(final DbDataListener<TagData[]> listener) {
        getTagDataRepository().getAll(listener);
    }

    public void insertTag(final TagData tag, final DbOperationListener listener) {
        getTagDataRepository().insert(tag, listener);
    }

    public void getConsultantById(final int id, DbDataListener<ConsultantData> listener){

    }

    public void getAllConsultants(DbDataListener<ConsultantData[]> listener){
        getConsultantDataRepository().getAll(listener);
    }

    public void insertConsultant(ConsultantData consultant, DbOperationListener listener){
        getConsultantDataRepository().insert(consultant, listener);
    }
}
