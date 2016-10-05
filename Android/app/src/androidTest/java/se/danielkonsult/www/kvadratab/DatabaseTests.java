package se.danielkonsult.www.kvadratab;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.services.notification.NewConsultantNotification;
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
        db.insertOffice(oData);

        // Read it back all offices and make sure that the correct one is there
        db = new KvadratTestDb(ctx);
        OfficeData[] assertOffices = db.getAllOffices();
        Assert.assertNotNull(assertOffices);

        boolean foundOffice = false;
        for (OfficeData office : assertOffices) {
            if ((office.Id == oData.Id) && office.Name.equals(oData.Name))
                foundOffice = true;
        }
        Assert.assertTrue(foundOffice);

        // Read it back by id
        db = new KvadratTestDb(ctx);
        OfficeData assertOffice = db.getOfficeById(17);
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
        db.insertTag(tagData);

        // Read it back all Tags and make sure that the correct one is there
        db = new KvadratTestDb(ctx);
        TagData[] assertTags = db.getAllTags();
        Assert.assertNotNull(assertTags);
        boolean foundTag = false;
        for (TagData Tag : assertTags) {
            if ((Tag.Id == tagData.Id) && Tag.Name.equals(tagData.Name))
                foundTag = true;
        }
        Assert.assertTrue(foundTag);

        // Read it back by id
        db = new KvadratTestDb(ctx);
        TagData assertTag = db.getTagById(4);
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
        int consultantCount = db.getConsultantCount();
        Assert.assertEquals(0, consultantCount);

        // Create and insert tag
        TagData tagData = new TagData();
        tagData.Id = 4;
        tagData.Name = "Systemutveckling";
        db.insertTag(tagData);

        // Create and insert 2 offices
        OfficeData officeData = new OfficeData();
        officeData.Id = 17;
        officeData.Name = "Jönköping";
        db.insertOffice(officeData);

        OfficeData officeData2 = new OfficeData();
        officeData2.Id = 6;
        officeData2.Name = "Linköping";
        db.insertOffice(officeData2);

        ConsultantData consultantData = new ConsultantData();
        consultantData.Id = 6985;
        consultantData.FirstName = "Daniel";
        consultantData.LastName = "Persson";
        consultantData.JobRole = "Systemutväcklare";
        consultantData.Description = "Daniel är en glad och positiv kille, förstås, vad skulle vi annars skriva här?";
        consultantData.OfficeId = 999; // Wrong!!

        db = new KvadratTestDb(ctx);
        try {
            db.insertConsultant(consultantData);
            Assert.fail("Insert with invalid office id succeeded!");
        }
        catch (Throwable ex){
            // No code
        }

        // Then change the office link and try again
        consultantData.OfficeId = officeData.Id;
        db.insertConsultant(consultantData);

        db = new KvadratTestDb(ctx);
        ConsultantData[] assertConsultants = db.getAllConsultants(true);
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
        db.updateConsultantOffice(consultantData.Id, officeData2.Id);
        // Assert the new office
        db = new KvadratTestDb(ctx);
        foundConsultant = db.getConsultantById(consultantData.Id, false);
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
        db.insertConsultant(consultantData2);
        db.insertConsultant(consultantData3);

        // Get the consultants back and make sure that they're sorted by last and then by first name
        db = new KvadratTestDb(ctx);
        assertConsultants = db.getAllConsultants(false);
        Assert.assertEquals("Zäta", assertConsultants[0].FirstName);
        Assert.assertEquals("Anna", assertConsultants[1].FirstName);
        Assert.assertEquals("Daniel", assertConsultants[2].FirstName);

        // End by counting the consultants to see that it works as well
        db = new KvadratTestDb(ctx);
        consultantCount = db.getConsultantCount();
        Assert.assertEquals(3, consultantCount);
    }

    @Test
    public void shouldWriteAndReadNotifications(){
        // Clean out the testdatabase
        Context ctx = InstrumentationRegistry.getTargetContext();
        ctx.deleteDatabase(KvadratTestDb.DATABASE_NAME);

        // Make sure that there are no notifications
        KvadratDb db = new KvadratTestDb(ctx);
        Notification[] notifications = db.getAllNotifications();

        Assert.assertEquals(0, notifications.length);

        // Create a new notification
        NewConsultantNotification nd = new NewConsultantNotification(6978, "Daniel Persson", "Jönköping");

        // Insert it
        db = new KvadratTestDb(ctx);
        db.insertNotification(nd);

        // Read it back
        db = new KvadratTestDb(ctx);
        notifications = db.getAllNotifications();
        Assert.assertEquals(1, notifications.length);
        Assert.assertTrue(notifications[0] instanceof NewConsultantNotification);

        NewConsultantNotification existing = (NewConsultantNotification) notifications[0];
        Assert.assertEquals(nd.Id, existing.Id);
        Assert.assertEquals(nd.Timestamp, existing.Timestamp);
        Assert.assertEquals(nd.Name, existing.Name);
        Assert.assertEquals(nd.Office, existing.Office);
    }
}
