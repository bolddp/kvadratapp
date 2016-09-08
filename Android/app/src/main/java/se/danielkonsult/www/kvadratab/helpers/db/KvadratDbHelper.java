package se.danielkonsult.www.kvadratab.helpers.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daniel on 2016-09-08.
 */
public class KvadratDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Kvadrat.db";
    public static final int DATABASE_VERSION = 1;

    // Constructor

    public KvadratDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
