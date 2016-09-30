package se.danielkonsult.www.kvadratab.services.data;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.OfficeData;

/**
 * Created by Daniel on 2016-09-14.
 */
public interface DataService {

    void setListener(DataServiceListener listener);

    /**
     * Gets a list of all offices.
     * @return
     */
    OfficeData[] getOffices();

    ConsultantData[] getAllConsultants();

    /**
     * Gets a list of all consultants that is cached in the data service.
     */
    ConsultantData[] getFilteredConsultants();

    /**
     * Tries a filter without applying it and returns the number of hits
     * it would produce.
     */
    int tryFilter(ConsultantFilter filter);

    void useTriedFilter();

    /**
     * Sets the filter that should be used to select displayed consultants.
     */
    void setFilter(ConsultantFilter filter);

    /**
     * Gets the current filter.
     */
    ConsultantFilter getFilter();
}
