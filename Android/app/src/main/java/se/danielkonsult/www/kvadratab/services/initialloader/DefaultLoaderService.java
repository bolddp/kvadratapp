package se.danielkonsult.www.kvadratab.services.initialloader;

import android.graphics.Bitmap;

import java.io.IOException;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.entities.SummaryData;
import se.danielkonsult.www.kvadratab.services.notification.InfoNotification;

/**
 * Performs the initial loading of consultant data from the
 * web page into the application database.
 */
public class DefaultLoaderService implements LoaderService {

    // Private variables

    private final String TAG = "InitialLoader";

    /**
     * Loads the summary data (offices and tags) and adds it to the database.
     */
    private SummaryData loadSummaryData() throws IOException {
        SummaryData summaryData = AppCtrl.getWebPageScraper().scrapeSummaryData();

        for (OfficeData od : summaryData.OfficeDatas)
            AppCtrl.getDb().getOfficeDataRepository().insert(od);

        for (TagData td : summaryData.TagDatas)
            AppCtrl.getDb().getTagDataRepository().insert(td);

        return summaryData;
    }

    /**
     * Links offices and consultants together by loading one office at a time
     * and update the office id of the returned consultants.
     */
    private void linkOfficesToConsultants(OfficeData[] offices) throws IOException {
        for (OfficeData od: offices){
            ConsultantData[] consultants = AppCtrl.getWebPageScraper().scrapeConsultants(od.Id, 0);
            for (ConsultantData cd : consultants){
                AppCtrl.getDb().getConsultantDataRepository().updateOffice(cd.Id, od.Id);
            }
        }
    }

    // Public methods

    /**
     * Returns a value indicating whether initial loading of consultants
     * is needed.
     */
    @Override
    public boolean isInitialLoadNeeded() {
        // Check the number of consultants
        KvadratDb db = AppCtrl.getDb();
        int consultantCount = db.getConsultantDataRepository().getCount();

        // Then also check the preferences flag that signals that initial
        // loading has been correctly performed
        boolean hasInitialLoadingBeenPerformed = AppCtrl.getPrefsService().getHasInitialLoadingBeenPerformed();

        // Is there a discrepancy? Then start over
        if ((consultantCount > 0) && !hasInitialLoadingBeenPerformed){
            AppCtrl.dropDatabase();
            AppCtrl.getImageService().deleteAllConsultantImages();

            return true;
        }

        return !hasInitialLoadingBeenPerformed;
    }

    /**
     * Performs the initial loading of data.
     */
    @Override
    public void run(final LoaderServiceListener listener) {
        // Run it all on a background thread since it involves network traffic etc.
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                KvadratDb db = AppCtrl.getDb();

                try {
                    // Notify listeners that the initial load has started
                    listener.onInitialLoadStarted();

                    // Load offices and tags. No progress reported since this should
                    // go rather fast
                    SummaryData summaryData = loadSummaryData();

                    int progress = 0;
                    // Scrape the web page for all consultants and loop them
                    ConsultantData[] consultants = AppCtrl.getWebPageScraper().scrapeConsultants(0,0);
                    listener.onInitialLoadProgress(progress, consultants.length);
                    for (ConsultantData cd : consultants){
                        // Save the consultant to database
                        db.getConsultantDataRepository().insert(cd);
                        // Load the consultant image and save it to disk
                        Bitmap bitmap = AppCtrl.getImageService().downloadConsultantBitmap(cd.Id);
                        AppCtrl.getImageService().saveConsultantBitmapToFile(cd.Id, bitmap);

                        // Notify listeners of this new glorious consultant
                        listener.onConsultantAdded(cd, bitmap);

                        progress++;
                        listener.onInitialLoadProgress(progress, consultants.length);
                    }

                    linkOfficesToConsultants(summaryData.OfficeDatas);

                    // Indicate that a complete initial load has been performed
                    AppCtrl.getPrefsService().setHasInitialLoadingBeenPerformed(true);

                    // Create a notification about this
                    AppCtrl.getNotificationService().add(new InfoNotification("Initial inladdning av konsulter avslutad"));

                    // All done for now, notify
                    listener.onInitialLoadingCompleted();
                }
                catch (Throwable ex){
                    // Notify listeners of the problem
                    listener.onError(TAG, ex.getMessage());
                }
            }
        });
        thread.start();
    }
}
