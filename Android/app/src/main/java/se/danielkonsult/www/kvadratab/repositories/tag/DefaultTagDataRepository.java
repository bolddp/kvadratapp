package se.danielkonsult.www.kvadratab.repositories.tag;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.helpers.db.DbSpec;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;

/**
 * Created by Daniel on 2016-09-11.
 */
public class DefaultTagDataRepository implements TagDataRepository {

    // Private variables

    private final String[] queryProjection = {
            DbSpec.TagEntry.COLUMN_NAME_ID,
            DbSpec.TagEntry.COLUMN_NAME_NAME,
    };

    private KvadratDb _db;

    // Constructor

    public DefaultTagDataRepository(KvadratDb db) {
        _db = db;
    }

    // Public methods

    @Override
    public TagData getById(int id) {
        String selection = DbSpec.TagEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {
                Integer.toString(id)
        };

        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor c = db.query(DbSpec.TagEntry.TABLE_NAME, queryProjection, selection, selectionArgs, null, null, null, null);
        if (c.moveToFirst()){
            return getFromCursor(c);
        }

        return null;
    }

    /**
     * Reads a TagData object from a db cursor.
     */
    private TagData getFromCursor(Cursor c) {
        TagData oData = new TagData();
        oData.Id = c.getInt(c.getColumnIndex(DbSpec.TagEntry.COLUMN_NAME_ID));
        oData.Name = c.getString(c.getColumnIndex(DbSpec.TagEntry.COLUMN_NAME_NAME));

        return oData;
    }

    @Override
    public TagData[] getAll() {
        List<TagData> result = new ArrayList<>();

        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor c = db.query(DbSpec.TagEntry.TABLE_NAME, queryProjection, null, null, null, null, null);
        while (c.moveToNext()){
            result.add(getFromCursor(c));
        }

        return result.toArray(new TagData[result.size()]);
    }

    @Override
    public void insert(TagData tag) {
        SQLiteDatabase db = _db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbSpec.TagEntry.COLUMN_NAME_ID, tag.Id);
        values.put(DbSpec.TagEntry.COLUMN_NAME_NAME, tag.Name);

        db.insertOrThrow(DbSpec.TagEntry.TABLE_NAME, null, values);
    }
}
