package se.danielkonsult.www.kvadratab;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.ConsultantDetails;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.services.notification.ConsultantInsertedNotification;
import se.danielkonsult.www.kvadratab.services.notification.OfficeInsertedNotification;
import se.danielkonsult.www.kvadratab.services.notification.Notification;

/**
 * Tests aimed at the KvadratDb class.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTests {

    // Private variables

    private static final String TAG = "DatabaseTests";

    // Private methods

    /**
     * Test that an office can be written and read in the app database.
     */
    @Test
    public void shouldStoreAndReadBackOffice() throws InterruptedException {

        OfficeData oData = new OfficeData();
        oData.Id = 17;
        oData.Name = "Jönköping";

        Context ctx = InstrumentationRegistry.getTargetContext();
        ctx.deleteDatabase(KvadratTestDb.DATABASE_NAME);

        KvadratTestDb db = new KvadratTestDb(ctx);
        db.getOfficeDataRepository().insert(oData);

        // Read it back all offices and make sure that the correct one is there
        db = new KvadratTestDb(ctx);
        OfficeData[] assertOffices = db.getOfficeDataRepository().getAll();
        Assert.assertNotNull(assertOffices);

        boolean foundOffice = false;
        for (OfficeData office : assertOffices) {
            if ((office.Id == oData.Id) && office.Name.equals(oData.Name))
                foundOffice = true;
        }
        Assert.assertTrue(foundOffice);

        // Read it back by id
        db = new KvadratTestDb(ctx);
        OfficeData assertOffice = db.getOfficeDataRepository().getById(oData.Id);
        Assert.assertNotNull(assertOffice);
        Assert.assertTrue((assertOffice.Id == oData.Id) && assertOffice.Name.equals(oData.Name));
    }

    /**
     * Test that a tag can be read and written to the database.
     */
    @Test
    public void shouldStoreAndReadBackTag() throws InterruptedException {
        Context ctx = InstrumentationRegistry.getTargetContext();
        ctx.deleteDatabase(KvadratTestDb.DATABASE_NAME);

        TagData tagData = new TagData();
        tagData.Id = 4;
        tagData.Name = "Systemutveckling";

        // Insert the Tag
        KvadratTestDb db = new KvadratTestDb(ctx);
        db.getTagDataRepository().insert(tagData);

        // Read it back all Tags and make sure that the correct one is there
        db = new KvadratTestDb(ctx);
        TagData[] assertTags = db.getTagDataRepository().getAll();
        Assert.assertNotNull(assertTags);
        boolean foundTag = false;
        for (TagData Tag : assertTags) {
            if ((Tag.Id == tagData.Id) && Tag.Name.equals(tagData.Name))
                foundTag = true;
        }
        Assert.assertTrue(foundTag);

        // Read it back by id
        db = new KvadratTestDb(ctx);
        TagData assertTag = db.getTagDataRepository().getById(tagData.Id);
        Assert.assertNotNull(assertTag);
        Assert.assertTrue((assertTag.Id == tagData.Id) && assertTag.Name.equals(tagData.Name));
    }


    /**
     * Test that a tag can be read and written to the database.
     */
    @Test
    public void shouldStoreAndReadBackConsultant() throws InterruptedException {

        // Clean out the testdatabase
        Context ctx = InstrumentationRegistry.getTargetContext();
        ctx.deleteDatabase(KvadratTestDb.DATABASE_NAME);

        // Start by checking that there are no consultants
        KvadratDb db = new KvadratTestDb(ctx);
        int consultantCount = db.getConsultantDataRepository().getCount();
        Assert.assertEquals(0, consultantCount);

        // Create and insert tag
        TagData tagData = new TagData();
        tagData.Id = 4;
        tagData.Name = "Systemutveckling";
        db.getTagDataRepository().insert(tagData);

        // Create and insert 2 offices
        OfficeData officeData = new OfficeData();
        officeData.Id = 17;
        officeData.Name = "Jönköping";
        db.getOfficeDataRepository().insert(officeData);

        OfficeData officeData2 = new OfficeData();
        officeData2.Id = 6;
        officeData2.Name = "Linköping";
        db.getOfficeDataRepository().insert(officeData2);

        ConsultantData consultantData = new ConsultantData();
        consultantData.Id = 6985;
        consultantData.FirstName = "Daniel";
        consultantData.LastName = "Persson";
        consultantData.JobRole = "Systemutväcklare";
        consultantData.Description = "Daniel är en glad och positiv kille, förstås, vad skulle vi annars skriva här?";
        consultantData.OfficeId = 999; // Wrong!!

        db = new KvadratTestDb(ctx);
        try {
            db.getConsultantDataRepository().insert(consultantData);
            Assert.fail("Insert with invalid office id succeeded!");
        }
        catch (Throwable ex){
            // No code
        }

        // Then change the office link and try again
        consultantData.OfficeId = officeData.Id;
        db.getConsultantDataRepository().insert(consultantData);

        db = new KvadratTestDb(ctx);
        ConsultantData[] assertConsultants = db.getConsultantDataRepository().getAll(true);
        Assert.assertNotNull(assertConsultants);

        ConsultantData foundConsultant = null;
        for (ConsultantData consultant : assertConsultants) {
            if (consultant.Id == consultantData.Id)
                foundConsultant = consultant;
        }
        Assert.assertNotNull(foundConsultant);
        Assert.assertEquals(consultantData.Id, foundConsultant.Id);
        Assert.assertEquals(consultantData.OfficeId, foundConsultant.OfficeId);
        Assert.assertEquals(consultantData.FirstName, foundConsultant.FirstName);
        Assert.assertEquals(consultantData.LastName, foundConsultant.LastName);
        // Verify that the office has been linked properly
        Assert.assertEquals(officeData.Name, foundConsultant.Office.Name);

        // Update the office id of the consultant
        db = new KvadratTestDb(ctx);
        db.getConsultantDataRepository().updateOffice(consultantData.Id, officeData2.Id);
        // Assert the new office
        db = new KvadratTestDb(ctx);
        foundConsultant = db.getConsultantDataRepository().getById(consultantData.Id, false);
        Assert.assertEquals(officeData2.Id, foundConsultant.OfficeId);

        // Add two more consultants to check that sorting works properly
        ConsultantData consultantData2 = new ConsultantData();
        consultantData2.Id = 12;
        consultantData2.FirstName = "Anna";
        consultantData2.LastName = "Persson";
        consultantData2.JobRole = "Testare";
        consultantData2.Description = "Anna är toppen!";
        consultantData2.OfficeId = officeData.Id; // Jönköping

        ConsultantData consultantData3 = new ConsultantData();
        consultantData3.Id = 345;
        consultantData3.FirstName = "Zäta";
        consultantData3.LastName = "Bengtsson";
        consultantData3.JobRole = "Managementkonsult";
        consultantData3.Description = "Zäta är en lysande personlighet!";
        consultantData3.OfficeId = officeData.Id; // Jönköping

        db = new KvadratTestDb(ctx);
        db.getConsultantDataRepository().insert(consultantData2);
        db.getConsultantDataRepository().insert(consultantData3);

        // Get the consultants back and make sure that they're sorted by last and then by first name
        db = new KvadratTestDb(ctx);
        assertConsultants = db.getConsultantDataRepository().getAll(false);
        Assert.assertEquals("Zäta", assertConsultants[0].FirstName);
        Assert.assertEquals("Anna", assertConsultants[1].FirstName);
        Assert.assertEquals("Daniel", assertConsultants[2].FirstName);

        // Count the consultants
        db = new KvadratTestDb(ctx);
        consultantCount = db.getConsultantDataRepository().getCount();
        Assert.assertEquals(3, consultantCount);

        // Update the details
        ConsultantDetails details = new ConsultantDetails();
        details.CompetenceAreas = new String[] { ".NET-utvecklare", "Android-utvecklareåäö" };
        details.Description = "Det här är description!";
        details.Overview = "Det här är overview";
        db = new KvadratTestDb(ctx);
        db.getConsultantDataRepository().updateDetails(consultantData3.Id, details);

        // Read it back
        db = new KvadratTestDb(ctx);
        foundConsultant = db.getConsultantDataRepository().getById(consultantData3.Id, true);
        Assert.assertEquals(foundConsultant.Description, details.Description);
        Assert.assertEquals(foundConsultant.Overview, details.Overview);
        Assert.assertEquals(details.CompetenceAreas.length, foundConsultant.CompetenceAreas.length);

        for (int a = 0;a < details.CompetenceAreas.length;a++){
            Assert.assertEquals(details.CompetenceAreas[a], foundConsultant.CompetenceAreas[a]);
        }
    }

    @Test
    public void shouldWriteAndReadNotifications() throws InterruptedException {
        // Clean out the testdatabase
        Context ctx = InstrumentationRegistry.getTargetContext();
        ctx.deleteDatabase(KvadratTestDb.DATABASE_NAME);

        // Make sure that there are no notifications
        KvadratDb db = new KvadratTestDb(ctx);
        Notification[] notifications = db.getNotificationRepository().getNotifications(0);

        Assert.assertEquals(0, notifications.length);

        // Create new notifications (with a delay to ensure different timestamps)
        ConsultantInsertedNotification ncn = new ConsultantInsertedNotification(6978, "Daniel Persson", "Jönköping");
        Thread.sleep(200);
        OfficeInsertedNotification non = new OfficeInsertedNotification(90, "Vetlanda");

        // Insert them
        db = new KvadratTestDb(ctx);
        db.getNotificationRepository().insert(ncn);
        db.getNotificationRepository().insert(non);

        // Read it back
        db = new KvadratTestDb(ctx);
        notifications = db.getNotificationRepository().getNotifications(0);
        Assert.assertEquals(2, notifications.length);
        Assert.assertTrue(notifications[0] instanceof OfficeInsertedNotification);
        Assert.assertTrue(notifications[1] instanceof ConsultantInsertedNotification);

        ConsultantInsertedNotification existing = (ConsultantInsertedNotification) notifications[1];
        Assert.assertEquals(ncn.ConsultantId, existing.ConsultantId);
        Assert.assertEquals(ncn.Timestamp, existing.Timestamp);
        Assert.assertEquals(ncn.FirstName, existing.FirstName);
        Assert.assertEquals(ncn.Office, existing.Office);
    }
}
