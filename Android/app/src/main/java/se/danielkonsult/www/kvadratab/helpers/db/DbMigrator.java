package se.danielkonsult.www.kvadratab.helpers.db;

/**
 * Contains logic that is used when the database should be migrated
 * to a new version.
 */

public interface DbMigrator {

    /**
     * Makes sure that the gender is set on all consultants.
     */
    void ensureGenderSet(KvadratDb db);
}
