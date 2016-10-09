package se.danielkonsult.www.kvadratab.repositories.office;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.helpers.db.DbSpec;

/**
 * Created by Daniel on 2016-09-11.
 */
public class DefaultOfficeDataRepository implements OfficeDataRepository {

    // Private variables

    private final String[] queryProjection = {
            DbSpec.OfficeEntry.COLUMN_NAME_ID,
            DbSpec.OfficeEntry.COLUMN_NAME_NAME,
    };

    private KvadratDb _db;

    // Private methods

    /**
     * Reads an OfficeData object from a db cursor.
     */
    private OfficeData getFromCursor(Cursor c) {
        OfficeData oData = new OfficeData();
        oData.Id = c.getInt(c.getColumnIndex(DbSpec.OfficeEntry.COLUMN_NAME_ID));
        oData.Name = c.getString(c.getColumnIndex(DbSpec.OfficeEntry.COLUMN_NAME_NAME));

        return oData;
    }

    // Constructor

    public DefaultOfficeDataRepository(KvadratDb db) {
        _db = db;
    }

    // Public methods

    @Override
    public OfficeData getById(final int id) {
        String selection = DbSpec.OfficeEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {
                Integer.toString(id)
        };

        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor c = db.query(DbSpec.OfficeEntry.TABLE_NAME, queryProjection, selection, selectionArgs, null, null, null, null);
        if (c.moveToFirst()){
            return getFromCursor(c);
        }

        return null;
    }

    @Override
    public OfficeData[] getAll() {
        List<OfficeData> result = new ArrayList<>();

        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor c = db.query(DbSpec.OfficeEntry.TABLE_NAME, queryProjection, null, null, null, null, null);
        while (c.moveToNext()){
            result.add(getFromCursor(c));
        }

        return result.toArray(new OfficeData[result.size()]);
    }

    @Override
    public void insert(final OfficeData office) {
        SQLiteDatabase db = _db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbSpec.OfficeEntry.COLUMN_NAME_ID, office.Id);
        values.put(DbSpec.OfficeEntry.COLUMN_NAME_NAME, office.Name);

        db.insertOrThrow(DbSpec.OfficeEntry.TABLE_NAME, null, values);
    }

    @Override
    public void update(int officeId, String name) {
        SQLiteDatabase db = _db.getWritableDatabase();
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(DbSpec.OfficeEntry.COLUMN_NAME_NAME, name);

        String filter = String.format("%s = %d", DbSpec.OfficeEntry.COLUMN_NAME_ID, officeId);

        db.update(DbSpec.OfficeEntry.TABLE_NAME, updatedValues, filter, null);
    }
}
