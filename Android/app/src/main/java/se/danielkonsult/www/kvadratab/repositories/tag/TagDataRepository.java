package se.danielkonsult.www.kvadratab.repositories.tag;

import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.helpers.db.DbDataListener;
import se.danielkonsult.www.kvadratab.helpers.db.DbOperationListener;

/**
 * Reads and writes tags data from the app database. Tags
 * are set on consultants and are searchable.
 */
public interface TagDataRepository {
    void getById(int id, DbDataListener<TagData> listener);
    void getAll(DbDataListener<TagData[]> listener);

    void insert(TagData tagData, DbOperationListener listener);
}
