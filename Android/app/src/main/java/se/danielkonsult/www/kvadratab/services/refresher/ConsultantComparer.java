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
import se.danielkonsult.www.kvadratab.helpers.KvadratAppException;
import se.danielkonsult.www.kvadratab.services.notification.ConsultantDeletedNotification;
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

    public static List<Notification> compare() throws IOException, KvadratAppException {
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

        // Also build a hash of all scraped consultants to be able to detect deleted consultants later on
        HashMap<Integer, ConsultantData> scrapedHash = new HashMap<>();

        // Loop the offices and request the consultants by them
        for (OfficeData office : offices){
            ConsultantData[] scrapedConsultants = AppCtrl.getWebPageScraper().scrapeConsultants(office.Id, 0);

            for (ConsultantData scrapedConsultant : scrapedConsultants){
                try {
                    scrapedHash.put(scrapedConsultant.Id, scrapedConsultant);

                    if (!existingHash.containsKey(scrapedConsultant.Id)) {
                        // Insert the consultant and link it to the correct office
                        scrapedConsultant.OfficeId = office.Id;
                        AppCtrl.getDb().getConsultantDataRepository().insert(scrapedConsultant);
                        // AppCtrl.getDb().getConsultantDataRepository().updateOffice(scrapedConsultant.Id, office.Id);

                        // Download the image and save to file
                        Bitmap bitmap = AppCtrl.getImageService().downloadConsultantBitmap(scrapedConsultant.Id);
                        AppCtrl.getImageService().saveConsultantBitmapToFile(scrapedConsultant.Id, bitmap);

                        // It's a new consultant
                        result.add(new ConsultantInsertedNotification(scrapedConsultant.Id, scrapedConsultant.FirstName, scrapedConsultant.LastName, office.Name));
                    }
                    else {
                        // It's an existing consultant
                        ConsultantData existing = existingHash.get(scrapedConsultant.Id);

                        // Have the consultant moved to another office?
                        if (existing.OfficeId != office.Id) {
                            AppCtrl.getDb().getConsultantDataRepository().updateOffice(existing.Id, office.Id);
                            result.add(new ConsultantUpdatedOfficeNotification(scrapedConsultant.Id, scrapedConsultant.FirstName, scrapedConsultant.LastName, office.Name));
                        }

                        // Has the name of the consultant changed?
                        if (!existing.FirstName.equals(scrapedConsultant.FirstName) ||
                            !existing.LastName.equals(scrapedConsultant.LastName)) {
                            AppCtrl.getDb().getConsultantDataRepository().updateName(existing.Id, scrapedConsultant.FirstName, scrapedConsultant.LastName);
                            result.add(new ConsultantUpdatedNameNotification(scrapedConsultant.Id, existing.FirstName, existing.LastName, scrapedConsultant.FirstName, scrapedConsultant.LastName, office.Name));
                        }

                        // Should we compare bitmaps this time (determined earlier)?
                        if (shouldCompareBitmaps){
                            Bitmap existingBitmap = AppCtrl.getImageService().getConsultantBitmapFromFile(existing.Id);
                            // Scrape the bitmap and save it to file to be able to compare it to the existing bitmap, also read from file
                            AppCtrl.getImageService().saveConsultantBitmapToFile(100000, AppCtrl.getImageService().downloadConsultantBitmap(existing.Id));
                            Bitmap scrapedBitmap = AppCtrl.getImageService().getConsultantBitmapFromFile(100000);

                            if (!existingBitmap.sameAs(scrapedBitmap)){
                                AppCtrl.getImageService().saveConsultantBitmapToFile(existing.Id, scrapedBitmap);
                                result.add(new ConsultantUpdatedBitmapNotification(existing.Id, scrapedConsultant.FirstName, scrapedConsultant.LastName, office.Name));
                            }
                        }
                    }
                } catch (Exception ex) {
                    throw new KvadratAppException(String.format("Fel vid behandling av konsult tillhörande kontor! (Officeid: %d, konsultid: %d)", office.Id, scrapedConsultant.Id), ex);
                }
            }
        }

        // Loop all existing consultants and see if anyone of them is missing in the scraped data
        for (ConsultantData exCon : existingConsultants) {
            if (!scrapedHash.containsKey(exCon.Id)) {
                // The consultant is gone, delete from database and create notification
                AppCtrl.getDb().getConsultantDataRepository().delete(exCon.Id);
                result.add(new ConsultantDeletedNotification(exCon.Id, exCon.FirstName, exCon.LastName, exCon.Office.Name));
            }
        }

        // Update the timestamp for when images were being compared
        if (shouldCompareBitmaps){
            AppCtrl.getPrefsService().setImageComparisonTimestamp(System.currentTimeMillis());
        }

        return result;
    }

}
