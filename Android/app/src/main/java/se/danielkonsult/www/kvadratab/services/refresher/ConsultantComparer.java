package se.danielkonsult.www.kvadratab.services.refresher;

import android.graphics.Bitmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.helpers.Constants;
import se.danielkonsult.www.kvadratab.services.notification.ConsultantInsertedNotification;
import se.danielkonsult.www.kvadratab.services.notification.ConsultantUpdatedNameNotification;
import se.danielkonsult.www.kvadratab.services.notification.ConsultantUpdatedOfficeNotification;
import se.danielkonsult.www.kvadratab.services.notification.ConsultantUpdatedBitmapNotification;
import se.danielkonsult.www.kvadratab.services.notification.Notification;

/**
 * Compares new and existing consultant data and creates
 * notifications and performs database updates to reflect the changes.
 */
public class ConsultantComparer {

    public static List<Notification> compare() throws IOException {
        List<Notification> result = new ArrayList<>();

        long lastImageComparisonTimestamp = AppCtrl.getPrefsService().getImageComparisonTimestamp();
        if (lastImageComparisonTimestamp == 0) {
            // This seems to be the first comparison that is performed, we set the timestamp
            // to avoid performing a image comparison this first time
            lastImageComparisonTimestamp = System.currentTimeMillis();
            AppCtrl.getPrefsService().setImageComparisonTimestamp(lastImageComparisonTimestamp);
        }
        boolean shouldCompareBitmaps = (System.currentTimeMillis() - lastImageComparisonTimestamp) >
                (1000 * 3600 * Constants.REFRESHER_CONSULTANT_IMAGE_COMPARISON_INTERVAL_HOURS);

        // Get all offices from db
        OfficeData[] offices = AppCtrl.getDb().getOfficeDataRepository().getAll();

        // Load all consultants and create a hashmap for quick lookup
        ConsultantData[] existingConsultants = AppCtrl.getDb().getConsultantDataRepository().getAll(true);
        HashMap<Integer, ConsultantData> existingHash = new HashMap<>();
        for (ConsultantData cd : existingConsultants)
                existingHash.put(cd.Id, cd);

        // Also keep a list of all found consultants to be able to spot deleted consultants
        List<ConsultantData> allOfficeConsultants = new ArrayList<>();

        // Loop the offices and request the consultants by them
        for (OfficeData office : offices){
            ConsultantData[] scrapedConsultants = AppCtrl.getWebPageScraper().scrapeConsultants(office.Id, 0);

            for (ConsultantData scrapedConsultant : scrapedConsultants){
                allOfficeConsultants.add(scrapedConsultant);

                if (!existingHash.containsKey(scrapedConsultant.Id)) {
                    // Insert the consultant and link it to the correct office
                    AppCtrl.getDb().getConsultantDataRepository().insert(scrapedConsultant);
                    AppCtrl.getDb().getConsultantDataRepository().updateOffice(scrapedConsultant.Id, office.Id);

                    // Download the image and save to file
                    AppCtrl.getImageService().downloadConsultantBitmapAndSaveToFile(scrapedConsultant.Id);

                    // It's a new consultant
                    result.add(new ConsultantInsertedNotification(scrapedConsultant.Id, scrapedConsultant.FirstName, scrapedConsultant.LastName, office.Name));
                }
                else {
                    // It's an existing consultant
                    ConsultantData existing = existingHash.get(scrapedConsultant.Id);

                    // Have the consultant moved to another office?
                    if (existing.OfficeId != scrapedConsultant.OfficeId) {
                        AppCtrl.getDb().getConsultantDataRepository().updateOffice(existing.Id, scrapedConsultant.OfficeId);
                        result.add(new ConsultantUpdatedOfficeNotification(scrapedConsultant.Id, scrapedConsultant.FirstName, scrapedConsultant.LastName, office.Name));
                    }

                    // Has the name of the consultant changed?
                    if (!existing.FirstName.equals(scrapedConsultant.FirstName) ||
                        !existing.LastName.equals(scrapedConsultant.LastName)) {
                        AppCtrl.getDb().getConsultantDataRepository().updateName(existing.Id, scrapedConsultant.FirstName, scrapedConsultant.LastName);
                        result.add(new ConsultantUpdatedNameNotification(scrapedConsultant.Id, existing.FirstName, scrapedConsultant.FirstName, existing.LastName, scrapedConsultant.LastName));
                    }

                    // Should we compare bitmaps this time (determined earlier)?
                    if (shouldCompareBitmaps){
                        Bitmap existingBitmap = AppCtrl.getImageService().getConsultantBitmapFromFile(existing.Id);
                        Bitmap scrapedBitmap = AppCtrl.getImageService().downloadConsultantBitmapAndSaveToFile(existing.Id);

                        if (!existingBitmap.sameAs(scrapedBitmap)){
                            // It's already been downloaded and saved, all we need to do is create a notification
                            result.add(new ConsultantUpdatedBitmapNotification(existing.Id, scrapedConsultant.FirstName, scrapedConsultant.LastName, office.Name));
                        }
                    }
                }
            }
        }

        // Update the timestamp for when images where being compared
        if (shouldCompareBitmaps){
            AppCtrl.getPrefsService().setImageComparisonTimestamp(System.currentTimeMillis());
        }

        return result;
    }

}
