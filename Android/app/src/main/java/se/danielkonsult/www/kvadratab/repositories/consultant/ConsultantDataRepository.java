package se.danielkonsult.www.kvadratab.repositories.consultant;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;

/**
 * Created by Daniel on 2016-09-13.
 */
public interface ConsultantDataRepository {
    ConsultantData getById(int id);
    ConsultantData[] getAll();

    void insert(ConsultantData consultant);
}
