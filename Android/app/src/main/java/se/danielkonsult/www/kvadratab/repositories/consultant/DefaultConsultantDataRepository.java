package se.danielkonsult.www.kvadratab.repositories.consultant;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.Utils;
import se.danielkonsult.www.kvadratab.helpers.db.DbSpec;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;

/**
 * Handles database reading and writing of consultant data.
 */
public class DefaultConsultantDataRepository implements ConsultantDataRepository {

    // Private variables

    private final String[] queryProjection = {
            DbSpec.ConsultantEntry.COLUMN_NAME_ID,
            DbSpec.ConsultantEntry.COLUMN_NAME_FIRSTNAME,
            DbSpec.ConsultantEntry.COLUMN_NAME_LASTNAME,
            DbSpec.ConsultantEntry.COLUMN_NAME_JOBROLE,
            DbSpec.ConsultantEntry.COLUMN_NAME_DESCRIPTION,
            DbSpec.ConsultantEntry.COLUMN_NAME_OFFICEID
    };

    private KvadratDb _db;

    // Private methods

    /**
     * Reads a ConsultantData object from a db cursor.
     */
    private ConsultantData getFromCursor(Cursor c) {
        ConsultantData consultantData = new ConsultantData();
        consultantData.Id = c.getInt(c.getColumnIndex(DbSpec.ConsultantEntry.COLUMN_NAME_ID));
        consultantData.FirstName = c.getString(c.getColumnIndex(DbSpec.ConsultantEntry.COLUMN_NAME_FIRSTNAME));
        consultantData.LastName = c.getString(c.getColumnIndex(DbSpec.ConsultantEntry.COLUMN_NAME_LASTNAME));
        consultantData.JobRole = c.getString(c.getColumnIndex(DbSpec.ConsultantEntry.COLUMN_NAME_JOBROLE));
        consultantData.Description = c.getString(c.getColumnIndex(DbSpec.ConsultantEntry.COLUMN_NAME_DESCRIPTION));
        consultantData.OfficeId = c.getInt(c.getColumnIndex(DbSpec.ConsultantEntry.COLUMN_NAME_OFFICEID));

        return consultantData;
    }

    // Constructor

    public DefaultConsultantDataRepository(KvadratDb _db) {
        this._db = _db;
    }

    @Override
    public ConsultantData getById(int id) {
        String selection = DbSpec.ConsultantEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {
                Integer.toString(id)
        };

        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor c = db.query(DbSpec.ConsultantEntry.TABLE_NAME, queryProjection, selection, selectionArgs, null, null, null, null);
        if (c.moveToFirst()){
            return getFromCursor(c);
        }

        return null;
    }

    @Override
    public ConsultantData[] getAll() {
        List<ConsultantData> result = new ArrayList<>();

        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor c = db.query(DbSpec.ConsultantEntry.TABLE_NAME, queryProjection, null, null, null, null, null);
        while (c.moveToNext()){
            result.add(getFromCursor(c));
        }

        return result.toArray(new ConsultantData[result.size()]);
    }

    @Override
    public int getCount() {
        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor c = db.rawQuery(DbSpec.ConsultantEntry.SQL_COUNT_ALL, null);
        if (c.moveToFirst()){
            return c.getInt(0);
        }
        return -1;
    }

    @Override
    public void insert(ConsultantData consultant) {
        SQLiteDatabase db = _db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbSpec.ConsultantEntry.COLUMN_NAME_ID, consultant.Id);
        values.put(DbSpec.ConsultantEntry.COLUMN_NAME_FIRSTNAME, consultant.FirstName);
        values.put(DbSpec.ConsultantEntry.COLUMN_NAME_LASTNAME, consultant.LastName);
        if (!Utils.isStringNullOrEmpty(consultant.JobRole))
            values.put(DbSpec.ConsultantEntry.COLUMN_NAME_JOBROLE, consultant.JobRole);
        if (!Utils.isStringNullOrEmpty(consultant.Description))
            values.put(DbSpec.ConsultantEntry.COLUMN_NAME_DESCRIPTION, consultant.Description);

        // Insert office data
        if (consultant.OfficeId != 0)
            values.put(DbSpec.ConsultantEntry.COLUMN_NAME_OFFICEID, consultant.OfficeId);
        else if ((consultant.Office != null) && (consultant.Office.Id != 0))
            values.put(DbSpec.ConsultantEntry.COLUMN_NAME_OFFICEID, consultant.Office.Id);

        db.insertOrThrow(DbSpec.ConsultantEntry.TABLE_NAME, null, values);
    }

    @Override
    public void updateOffice(int consultantId, int officeId) {
        SQLiteDatabase db = _db.getWritableDatabase();

        String sql = String.format(DbSpec.ConsultantEntry.SQL_UPDATE_OFFICE_ID, officeId, consultantId);
        db.execSQL(sql);
    }
}
