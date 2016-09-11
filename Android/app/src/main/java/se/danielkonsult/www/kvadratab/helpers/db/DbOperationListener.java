package se.danielkonsult.www.kvadratab.helpers.db;

/**
 * Listener for database operations that don't return any data.
 */
public interface DbOperationListener {
    void onResult(long _id);
    void onError(String errorMessage);
}
