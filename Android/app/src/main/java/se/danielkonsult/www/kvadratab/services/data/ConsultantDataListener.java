package se.danielkonsult.www.kvadratab.services.data;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;

/**
 * Listener for asynchronous retrieval of consultant data.
 */
public interface ConsultantDataListener {
    void onResult(ConsultantData consultantData);
    void onError(String errorMessage);
}
