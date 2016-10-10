package se.danielkonsult.www.kvadratab;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.helpers.KvadratAppException;
import se.danielkonsult.www.kvadratab.mocks.TestNotificationService;
import se.danielkonsult.www.kvadratab.mocks.TestRefresherService;
import se.danielkonsult.www.kvadratab.mocks.TestWebPageScraper;
import se.danielkonsult.www.kvadratab.repositories.office.OfficeDataRepository;
import se.danielkonsult.www.kvadratab.services.image.DefaultImageService;
import se.danielkonsult.www.kvadratab.services.image.ImageService;
import se.danielkonsult.www.kvadratab.services.notification.ConsultantUpdatedBitmapNotification;
import se.danielkonsult.www.kvadratab.services.notification.ConsultantUpdatedNameNotification;
import se.danielkonsult.www.kvadratab.services.notification.ConsultantUpdatedOfficeNotification;
import se.danielkonsult.www.kvadratab.services.notification.Notification;
import se.danielkonsult.www.kvadratab.services.notification.OfficeDeletedNotification;
import se.danielkonsult.www.kvadratab.services.notification.OfficeInsertedNotification;
import se.danielkonsult.www.kvadratab.services.notification.OfficeUpdatedNotification;
import se.danielkonsult.www.kvadratab.services.refresher.ConsultantComparer;
import se.danielkonsult.www.kvadratab.services.refresher.OfficeComparer;

/**
 * Tests for the refresher functionality, detecting added and updated data
 * on the web page.
 */
@RunWith(AndroidJUnit4.class)
public class RefresherTests {

    private void setupTestEnvironment() {
        // Clean out the testdatabase
        Context ctx = InstrumentationRegistry.getTargetContext();
        ctx.deleteDatabase(KvadratTestDb.DATABASE_NAME);

        AppCtrl.setApplicationContext(ctx);

        AppCtrl.setTestDb(new KvadratTestDb(ctx));
        AppCtrl.setTestRefresherService(new TestRefresherService());
        AppCtrl.setTestWebPageScraper(new TestWebPageScraper());
        AppCtrl.setTestNotificationService(new TestNotificationService());
    }

    @Test
    public void shouldCompareOffices() throws IOException, KvadratAppException {
        setupTestEnvironment();

        // Insert some offices
        OfficeData od1 = new OfficeData(1, "Office 1");
        OfficeData od2 = new OfficeData(2, "Office 2");
        OfficeData od3 = new OfficeData(3, "Office 3");
        OfficeData od4 = new OfficeData(4, "Office 4");
        // Insert 3 of the offices
        AppCtrl.getDb().getOfficeDataRepository().insert(od1);
        AppCtrl.getDb().getOfficeDataRepository().insert(od2);
        AppCtrl.getDb().getOfficeDataRepository().insert(od3);

        OfficeDataRepository rep = AppCtrl.getDb().getOfficeDataRepository();

        Assert.assertNotNull(rep.getById(od1.Id));
        Assert.assertNull(rep.getById(4));

        // Prep the web page scraper, where office 1 is gone, office 3 has a new name and office 4 is new
        TestWebPageScraper scraper = (TestWebPageScraper) AppCtrl.getWebPageScraper();
        scraper.setSummaryData(new OfficeData[] {
                new OfficeData(2, "Office 2"),
                new OfficeData(3, "Office 3 updated"),
                od4
        }, null);

        // Compare the office datas
        List<Notification> notifications = OfficeComparer.compare();

        // Assert that the correct notifications are present by putting them in a hash
        // with the Notification type as key
        HashMap<String, Notification> notHash = new HashMap<>();
        for (Notification not : notifications)
                notHash.put(not.getClass().getSimpleName(), not);

        OfficeDeletedNotification deleteNot = (OfficeDeletedNotification) notHash.get(OfficeDeletedNotification.class.getSimpleName());
        Assert.assertEquals(od1.Id, deleteNot.Id);
        Assert.assertEquals(od1.Name, deleteNot.Name);

        OfficeUpdatedNotification updateNot = (OfficeUpdatedNotification) notHash.get(OfficeUpdatedNotification.class.getSimpleName());
        Assert.assertEquals(od3.Id, updateNot.Id);
        Assert.assertEquals(od3.Name, updateNot.OldName);
        Assert.assertEquals("Office 3 updated", updateNot.NewName);

        OfficeInsertedNotification newNot = (OfficeInsertedNotification) notHash.get(OfficeInsertedNotification.class.getSimpleName());
        Assert.assertEquals(od4.Id, newNot.OfficeId);
        Assert.assertEquals(od4.Name, newNot.Name);

        // Assert database contents
        Assert.assertEquals(3, notifications.size());
        Assert.assertNull(rep.getById(od1.Id));                 // Office 1 should be gone
        Assert.assertEquals("Office 4", rep.getById(4).Name);   // Office 4 should exist
        Assert.assertEquals("Office 3 updated", rep.getById(3).Name);
    }

    /**
     * Tests that consultants can be correctly compared, creating the notifications
     * and performing the correct updates of the database as changes are detected.
     */
    @Test
    public void shouldCompareConsultants() throws IOException, KvadratAppException {
        setupTestEnvironment();

        ImageService imgService = new DefaultImageService();

        // Create two offices
        OfficeData od1 = new OfficeData(1, "Jönköping");
        OfficeData od2 = new OfficeData(2, "Skövde");
        AppCtrl.getDb().getOfficeDataRepository().insert(od1);
        AppCtrl.getDb().getOfficeDataRepository().insert(od2);

        // Create some consultants and download their images
        ConsultantData cd1 = new ConsultantData(6985, "Daniel", "Persson", 1);
        ConsultantData cd2 = new ConsultantData(6271, "May-Lis", "Farnes", 1);
        AppCtrl.getDb().getConsultantDataRepository().insert(cd1);
        AppCtrl.getDb().getConsultantDataRepository().insert(cd2);
        // Download actual images (not optimal that unit test accesses web, but WTH...)
        imgService.downloadConsultantBitmapAndSaveToFile(6985);

        // Prep the test scraper with consultant data
        TestWebPageScraper scraper = (TestWebPageScraper) AppCtrl.getWebPageScraper();
        scraper.setConsultantData(2, new ConsultantData[] {
                new ConsultantData(6985, "Daniel", "Pärsson", 2)
        });

        // Compare the consultant datas
        List<Notification> notifications = ConsultantComparer.compare();

        // Assert that the correct notifications are present by putting them in a hash
        // with the Notification type as key
        HashMap<String, Notification> notHash = new HashMap<>();
        for (Notification not : notifications)
            notHash.put(not.getClass().getSimpleName(), not);

        ConsultantUpdatedNameNotification nameNot = (ConsultantUpdatedNameNotification) notHash.get(ConsultantUpdatedNameNotification.class.getSimpleName());
        Assert.assertEquals(cd1.Id, nameNot.ConsultantId);
        Assert.assertEquals("Daniel", nameNot.OldFirstName);
        Assert.assertEquals(cd1.FirstName, nameNot.OldFirstName);
        Assert.assertEquals("Daniel", nameNot.NewFirstName);
        Assert.assertEquals("Pärsson", nameNot.NewLastName);

        ConsultantUpdatedOfficeNotification officeNot = (ConsultantUpdatedOfficeNotification) notHash.get(ConsultantUpdatedOfficeNotification.class.getSimpleName());
        Assert.assertEquals(cd1.Id, officeNot.ConsultantId);
        Assert.assertEquals("Daniel", officeNot.FirstName);
        Assert.assertEquals("Pärsson", officeNot.LastName); // Notification uses updated name
        Assert.assertEquals(od2.Name, officeNot.NewOffice);

        ConsultantUpdatedBitmapNotification bitmapNot = (ConsultantUpdatedBitmapNotification) notHash.get(ConsultantUpdatedBitmapNotification.class.getSimpleName());
        Assert.assertEquals(cd2.Id, bitmapNot.ConsultantId);
    }
}
