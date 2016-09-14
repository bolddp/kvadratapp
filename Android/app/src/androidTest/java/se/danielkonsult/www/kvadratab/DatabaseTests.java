package se.danielkonsult.www.kvadratab;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;

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

        KvadratDb db = new KvadratTestDb(ctx);

        // Start by checking that there are no consultants
        db = new KvadratTestDb(ctx);
        int consultantCount = db.getConsultantCount();
        Assert.assertEquals(0, consultantCount);

        // Create and insert tag
        TagData tagData = new TagData();
        tagData.Id = 4;
        tagData.Name = "Systemutveckling";
        db.insertTag(tagData);

        // Create and insert office
        OfficeData officeData = new OfficeData();
        officeData.Id = 17;
        officeData.Name = "Jönköping";
        db.insertOffice(officeData);

        ConsultantData consultantData = new ConsultantData();
        consultantData.Id = 6985;
        consultantData.Name = "Daniel Persson";
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
        ConsultantData[] assertConsultants = db.getAllConsultants();
        Assert.assertNotNull(assertConsultants);

        ConsultantData foundConsultant = null;
        for (ConsultantData consultant : assertConsultants) {
            if (consultant.Id == consultantData.Id)
                foundConsultant = consultant;
        }
        Assert.assertNotNull(foundConsultant);
        Assert.assertEquals(consultantData.Id, foundConsultant.Id);

        // End by counting the consultants to see that it works as well
        db = new KvadratTestDb(ctx);
        consultantCount = db.getConsultantCount();
        Assert.assertEquals(1, consultantCount);
    }
}
