package se.danielkonsult.www.kvadratab.services.refresher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.services.notification.ConsultantInsertedNotification;
import se.danielkonsult.www.kvadratab.services.notification.Notification;

/**
 * Compares new and existing consultant data and creates
 * notifications and performs database updates to reflect the changes.
 */
public class ConsultantComparer {

    public static List<Notification> compare() throws IOException {
        List<Notification> result = new ArrayList<>();

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
            ConsultantData[] officeConsultants = AppCtrl.getWebPageScraper().scrapeConsultants(office.Id, 0);

            for (ConsultantData officeConsultant : officeConsultants){
                allOfficeConsultants.add(officeConsultant);

                if (!existingHash.containsKey(officeConsultant.Id)) {
                    // It's a new consultant
                    result.add(new ConsultantInsertedNotification(officeConsultant.Id, officeConsultant.FirstName, officeConsultant.LastName, office.Name));

                    // Insert the consultant and link it to the correct office
                    AppCtrl.getDb().getConsultantDataRepository().insert(officeConsultant);
                    AppCtrl.getDb().getConsultantDataRepository().updateOffice(officeConsultant.Id, office.Id);
                }

                ** // Fortsätt!
            }
        }
    }

}
