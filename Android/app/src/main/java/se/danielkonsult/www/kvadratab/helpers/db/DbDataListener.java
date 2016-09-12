package se.danielkonsult.www.kvadratab.helpers.db;

/**
 * Created by Daniel on 2016-09-11.
 */
public interface DbDataListener<T> {
    void onResult(T result);
    void onError(String errorMessage);
}
