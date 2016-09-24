package se.danielkonsult.www.kvadratab.services.data;

import android.graphics.Bitmap;

import java.io.IOException;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.helpers.scraper.SummaryData;
import se.danielkonsult.www.kvadratab.helpers.scraper.WebPageScraper;

/**
 * Performs the initial loading of consultant data from the
 * web page into the application database.
 */
public class InitialLoader {

    // Private variables

    private final String TAG = "InitialLoader";

    private KvadratDb _db;
    private DataServiceListeners listeners;

    /**
     * Loads the summary data (offices and tags) and adds it to the database.
     */
    private SummaryData loadSummaryData() throws IOException {
        SummaryData summaryData = WebPageScraper.scrapeSummaryData();

        for (OfficeData od : summaryData.OfficeDatas)
            _db.insertOffice(od);

        for (TagData td : summaryData.TagDatas)
            _db.insertTag(td);

        return summaryData;
    }

    /**
     * Links offices and consultants together by loading one office at a time
     * and update the office id of the returned consultants.
     */
    private void linkOfficesToConsultants(OfficeData[] offices) throws IOException {
        for (OfficeData od: offices){
            ConsultantData[] consultants = WebPageScraper.scrapeConsultants(od.Id, 0);
            for (ConsultantData cd : consultants){
                _db.updateConsultantOffice(cd.Id, od.Id);
            }
        }
    }

    // Constructor

    public InitialLoader(KvadratDb db, DataServiceListeners listeners) {
        this._db = db;
        this.listeners = listeners;
    }

    // Public methods

    public void run() {

        KvadratDb db = AppCtrl.getDb();

        try {
            // Notify listeners that the initial load has started
            listeners.onInitialLoadStarted();

            // Load offices and tags. No progress reported since this should
            // go rather fast
            SummaryData summaryData = loadSummaryData();

            int progress = 0;
            // Scrape the web page for all consultants and loop them
            ConsultantData[] consultants = WebPageScraper.scrapeConsultants(0,0);
            listeners.onInitialLoadProgress(progress, consultants.length);
            for (ConsultantData cd : consultants){
                // Save the consultant to database
                db.insertConsultant(cd);
                // Load the consultant image and save it to disk
                Bitmap bitmap = AppCtrl.getImageService().downloadConsultantBitmapAndSaveToFile(cd.Id);

                // Notify listeners of this new glorious consultant
                listeners.onConsultantAdded(cd, bitmap);

                progress++;
                listeners.onInitialLoadProgress(progress, consultants.length);
            }

            linkOfficesToConsultants(summaryData.OfficeDatas);

            AppCtrl.getDataService().setOffices(summaryData.OfficeDatas);

            // Make sure all consultants are available in the DataService
            consultants = AppCtrl.getDb().getAllConsultants(true);
            AppCtrl.getDataService().setAllConsultants(consultants);

            // Indicate that a complete refresh has been performed
            AppCtrl.getPrefsService().setRefreshPerformed();

            // All done for now, notify
            listeners.onConsultantsUpdated();
        }
        catch (Throwable ex){
            // Notify listeners of the problem
            listeners.onError(TAG, ex.getMessage());
        }
    }
}
