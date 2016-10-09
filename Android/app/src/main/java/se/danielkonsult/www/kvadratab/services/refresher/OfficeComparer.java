package se.danielkonsult.www.kvadratab.services.refresher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.SummaryData;
import se.danielkonsult.www.kvadratab.helpers.KvadratAppException;
import se.danielkonsult.www.kvadratab.services.notification.Notification;
import se.danielkonsult.www.kvadratab.services.notification.OfficeUpdatedNotification;
import se.danielkonsult.www.kvadratab.services.notification.OfficeInsertedNotification;
import se.danielkonsult.www.kvadratab.services.notification.OfficeDeletedNotification;

/**
 * Compares existing and new office data and creates notifications
 * and makes database updates if any differences are detected.
 */
public class OfficeComparer {

    public static List<Notification> compare() throws IOException, KvadratAppException {
        SummaryData summaryData = AppCtrl.getWebPageScraper().scrapeSummaryData();
        OfficeData[] existingOffices = AppCtrl.getDb().getOfficeDataRepository().getAll();
        OfficeData[] scrapedOffices = summaryData.OfficeDatas;

        List<Notification> result = new ArrayList<>();

        if ((existingOffices == null) || (existingOffices.length == 0))
            throw new KvadratAppException("Kunde inte läsa kontor från databasen!");
        if ((scrapedOffices == null) || (scrapedOffices.length == 0))
            throw new KvadratAppException("Kunde inte läsa kontor från webbsidan!");

        // Create hashes based on the id
        HashMap<Integer, OfficeData> existingHash = new HashMap<>();
        for (OfficeData od : existingOffices)
            existingHash.put(od.Id, od);

        HashMap<Integer, OfficeData> scrapedHash = new HashMap<>();
        for (OfficeData od : scrapedOffices)
            scrapedHash.put(od.Id, od);

        // Have any offices been removed? (Found in existing but not in scraped)
        for (OfficeData od : existingOffices) {
            if (!scrapedHash.containsKey(od.Id)) {
                result.add(new OfficeDeletedNotification(od.Id, od.Name));
                AppCtrl.getDb().getOfficeDataRepository().delete(od.Id);
            }
            else {
                // It exists, but it might have changed?
                OfficeData scrapedItem = scrapedHash.get(od.Id);
                if (!od.Name.equals(scrapedItem.Name)) {
                    result.add(new OfficeUpdatedNotification(od.Id, od.Name, scrapedItem.Name));
                    // Update the database as well
                    AppCtrl.getDb().getOfficeDataRepository().update(od.Id, scrapedItem.Name);
                }
            }
        }

        // Have any offices been added? Found in scraped but not in existing
        for (OfficeData od : scrapedOffices){
            if (!existingHash.containsKey(od.Id)){
                result.add(new OfficeInsertedNotification(od.Id, od.Name));
                // Update the database as well
                AppCtrl.getDb().getOfficeDataRepository().insert(od);
            }
        }

        return result;
    }
}
