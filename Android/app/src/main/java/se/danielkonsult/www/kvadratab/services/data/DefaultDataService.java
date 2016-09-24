package se.danielkonsult.www.kvadratab.services.data;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.helpers.Utils;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.helpers.scraper.ImageHelper;

/**
 * Created by Daniel on 2016-09-14.
 */
public class DefaultDataService implements DataService {

    // Refresh the database once a week
    private static final int REFRESH_INTERVAL_HOURS = 7 * 24;

    // Private variables

    private final DataServiceListeners _listeners = new DataServiceListeners();
    private static ConsultantData[] _allConsultants;
    private static ConsultantData[] _filteredConsultants;
    private static OfficeData[] _offices;
    private ConsultantFilter _filter;

    // Private methods

    private void setFilteredConsultants(ConsultantData[] consultantDatas) {
        _filteredConsultants = consultantDatas;
        if (_listeners != null)
            _listeners.onConsultantsUpdated();
    }

    /**
     * Uses the current filter to update the list of
     * filtered consultants from all consultants.
     */
    private void applyFilter() {
        List<ConsultantData> result = new ArrayList<>();

        // Is the filter empty? Then copy all consultants and exit the function
        if (Utils.isStringNullOrEmpty(_filter.getName().trim()) &&
                _filter.getOfficeIds().size() == 0) {
            for (ConsultantData cd: _allConsultants)
                result.add(cd);

            setFilteredConsultants(result.toArray(new ConsultantData[result.size()]));
            return;
        }

        String namePiece1 = "";
        String namePiece2 = "";
        boolean shouldFilterByName = false;
        // Is there any name filter? Then split it into first and last name
        if (!Utils.isStringNullOrEmpty(_filter.getName().trim())){
            shouldFilterByName = true;
            int spaceIndex = _filter.getName().indexOf(" ");
            if (spaceIndex < 0)
                namePiece1 = _filter.getName().toLowerCase().trim();
            else {
                namePiece1 = _filter.getName().toLowerCase().substring(0,spaceIndex).trim();
                namePiece2 = _filter.getName().toLowerCase().substring(spaceIndex).trim();
            }
        }

        for (ConsultantData cd : getAllConsultants()) {
            boolean isSelected = true;

            // Are there any specific offices in the filter?
            if (_filter.getOfficeIds().size() > 0){
                if (!_filter.getOfficeIds().contains(cd.OfficeId))
                    isSelected = false;
            }
            // Should we filter by name?
            if (isSelected && shouldFilterByName) {
                isSelected = false;

                // Is there only one name piece?
                if (Utils.isStringNullOrEmpty(namePiece2) &&
                        (cd.FirstName.toLowerCase().startsWith(namePiece1) || cd.LastName.toLowerCase().startsWith(namePiece1))){
                    isSelected = true;
                }
                // Filter by both first and last name
                else if (!Utils.isStringNullOrEmpty(namePiece2) &&
                        cd.FirstName.toLowerCase().startsWith(namePiece1) && cd.LastName.toLowerCase().startsWith(namePiece2)) {
                    isSelected = true;
                }
            }

            // Is the consultant still selected?
            if (isSelected)
                result.add(cd);
        }

        setFilteredConsultants(result.toArray(new ConsultantData[result.size()]));
    }

    // Constructor

    public DefaultDataService() {
        _filter = new ConsultantFilter();
    }

    @Override
    public void registerListener(DataServiceListener listener) {
        _listeners.registerListener(listener);
    }

    @Override
    public void unregisterListener(DataServiceListener listener) {
        _listeners.unregisterListener(listener);
    }

    @Override
    public void start() {
        // Start on a separate thread to not lock up GUI
        Runnable startRunnable = new Runnable() {
            @Override
            public void run() {
                // Do we have any consultants in the database yet? And how long is it since the last refresh?
                KvadratDb db = AppCtrl.getDb();
                int consultantCount = db.getConsultantCount();

                boolean isDataOld = false;
                if (consultantCount > 0){
                    // There are consultants, but how long since they were refreshed?
                    int hoursSinceLastRefresh = AppCtrl.getPrefsService().getHoursSinceLastRefresh();
                    if (hoursSinceLastRefresh > REFRESH_INTERVAL_HOURS){
                        // Drop the database and then get a new handle to it
                        AppCtrl.dropDatabase();
                        db = AppCtrl.getDb();
                        // Delete all consultant images
                        ImageHelper.deleteAllConsultantImages();

                        isDataOld = true;
                    }
                }

                if ((consultantCount == 0) || isDataOld) {
                    InitialLoader loader = new InitialLoader(db, _listeners);
                    loader.run();
                }
                else {
                    // Prep by loading the consultants before saying we're finished
                    setOffices(db.getAllOffices());
                    setAllConsultants(db.getAllConsultants(true));

                    // Signal that the data already is available, but also
                    // check if it's time for a refresh of the data.
                    _listeners.onConsultantsUpdated();

//                    Refresher refresher = new Refresher(db, _listeners);
//                    refresher.run();
                }
            }
        };
        AsyncTask.execute(startRunnable);
    }

    @Override
    public void setOffices(OfficeData[] offices) {
        _offices = offices;
    }

    @Override
    public OfficeData[] getOffices() {
        if (_offices == null)
            _offices = AppCtrl.getDb().getAllOffices();
        return  _offices;
    }

    @Override
    public void setAllConsultants(ConsultantData[] consultants) {
        _allConsultants = consultants;
        applyFilter();
    }

    @Override
    public ConsultantData[] getAllConsultants() {
        if (_allConsultants == null)
            _allConsultants = AppCtrl.getDb().getAllConsultants(true);
        return _allConsultants;
    }

    @Override
    public ConsultantData[] getFilteredConsultants() {
        return _filteredConsultants;
    }

    @Override
    public void setFilter(ConsultantFilter filter) {
        _filter = filter;
        applyFilter();
    }

    @Override
    public ConsultantFilter getFilter() {
        return _filter;
    }
}
