package se.danielkonsult.www.kvadratab.repositories.tag;

import se.danielkonsult.www.kvadratab.entities.TagData;

/**
 * Reads and writes tags data from the app database. Tags
 * are set on consultants and are searchable.
 */
public interface TagDataRepository {
    TagData getById(int id);
    TagData[] getAll();

    void insert(TagData tagData);
}
