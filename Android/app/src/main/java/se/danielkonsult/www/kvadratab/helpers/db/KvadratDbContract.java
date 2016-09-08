package se.danielkonsult.www.kvadratab.helpers.db;

import android.provider.BaseColumns;

/**
 * Created by Daniel on 2016-09-08.
 */
public class KvadratDbContract {

    // Private constructor

    private KvadratDbContract() {}

    public static class ConsultantEntry implements BaseColumns{
        public static final String TABLE_NAME = "consultant";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_JOBROLE = "jobrole";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IMAGE = "image";
    }
}
