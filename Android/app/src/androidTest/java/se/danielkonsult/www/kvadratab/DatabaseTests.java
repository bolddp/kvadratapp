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
import se.danielkonsult.www.kvadratab.helpers.db.DbDataListener;
import se.danielkonsult.www.kvadratab.helpers.db.DbOperationListener;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;

/**
 * Tests aimed at the KvadratDb class.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTests {

    // Private variables

    private static final String TAG = "DatabaseTests";

    private boolean hadExpectedBehavior;
    private OfficeData[] assertOffices;
    private TagData[] assertTags;
    private ConsultantData[] assertConsultants;

    // Private methods

    private void insertTag(KvadratDb db,TagData tagData, final CountDownLatch signal) {
        // Insert the Tag
        db.insertTag(tagData, new DbOperationListener() {
            @Override
            public void onResult(long id) {
                signal.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, errorMessage);
            }
        });
    }

    private void insertOffice(KvadratDb db,OfficeData officeData, final CountDownLatch signal) {
        // Insert the office
        db.insertOffice(officeData, new DbOperationListener() {
            @Override
            public void onResult(long id) {
                signal.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, errorMessage);
            }
        });
    }

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

        final CountDownLatch signal = new CountDownLatch(1);

        // Insert the office
        hadExpectedBehavior = false;
        KvadratTestDb db = new KvadratTestDb(ctx);
        db.insertOffice(oData, new DbOperationListener() {
            @Override
            public void onResult(long id) {
                hadExpectedBehavior = true;
                signal.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, errorMessage);
                signal.countDown();
            }
        });

        signal.await(10, TimeUnit.SECONDS);
        Assert.assertTrue(hadExpectedBehavior);

        final CountDownLatch signal2 = new CountDownLatch(1);

        // Read it back all offices and make sure that the correct one is there
        assertOffices = null;
        db = new KvadratTestDb(ctx);
        db.getAllOffices(new DbDataListener<OfficeData[]>() {
            @Override
            public void onResult(OfficeData[] offices) {
                assertOffices = offices;
                signal2.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                signal2.countDown();
            }
        });

        signal2.await(10, TimeUnit.SECONDS);
        Assert.assertNotNull(assertOffices);
        boolean foundOffice = false;
        for (OfficeData office : assertOffices) {
            if ((office.Id == oData.Id) && office.Name.equals(oData.Name))
                foundOffice = true;
        }
        Assert.assertTrue(foundOffice);

        // Read it back by id
        final CountDownLatch signal3 = new CountDownLatch(1);
        assertOffices = null;
        db = new KvadratTestDb(ctx);
        db.getOfficeById(17, new DbDataListener<OfficeData>() {
            @Override
            public void onResult(OfficeData result) {
                assertOffices = new OfficeData[]{
                    result
                };
                signal3.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                signal3.countDown();
            }
        });

        signal3.await(10, TimeUnit.SECONDS);
        Assert.assertNotNull(assertOffices);
        Assert.assertEquals(1, assertOffices.length);
        OfficeData office = assertOffices[0];
        Assert.assertTrue((office.Id == oData.Id) && office.Name.equals(oData.Name));
    }

    /**
     * Test that a tag can be read and written to the database.
     */
    @Test
    public void shouldStoreAndReadBackTag() throws InterruptedException {

        TagData tagData = new TagData();
        tagData.Id = 4;
        tagData.Name = "Systemutveckling";

        Context ctx = InstrumentationRegistry.getTargetContext();
        ctx.deleteDatabase(KvadratTestDb.DATABASE_NAME);

        final CountDownLatch signal = new CountDownLatch(1);

        // Insert the Tag
        hadExpectedBehavior = false;
        KvadratTestDb db = new KvadratTestDb(ctx);
        db.insertTag(tagData, new DbOperationListener() {
            @Override
            public void onResult(long id) {
                hadExpectedBehavior = true;
                signal.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, errorMessage);
                signal.countDown();
            }
        });

        signal.await(10, TimeUnit.SECONDS);
        Assert.assertTrue(hadExpectedBehavior);

        final CountDownLatch signal2 = new CountDownLatch(1);
        // Read it back all Tags and make sure that the correct one is there
        assertTags = null;
        db = new KvadratTestDb(ctx);
        db.getAllTags(new DbDataListener<TagData[]>() {
            @Override
            public void onResult(TagData[] tags) {
                assertTags = tags;
                signal2.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                signal2.countDown();
            }
        });

        signal2.await(10, TimeUnit.SECONDS);
        Assert.assertNotNull(assertTags);
        boolean foundTag = false;
        for (TagData Tag : assertTags) {
            if ((Tag.Id == tagData.Id) && Tag.Name.equals(tagData.Name))
                foundTag = true;
        }
        Assert.assertTrue(foundTag);

        // Read it back by id
        final CountDownLatch signal3 = new CountDownLatch(1);
        assertTags = null;
        db = new KvadratTestDb(ctx);
        db.getTagById(4, new DbDataListener<TagData>() {
            @Override
            public void onResult(TagData result) {
                assertTags = new TagData[]{
                        result
                };
                signal3.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                signal3.countDown();
            }
        });

        signal3.await(10, TimeUnit.SECONDS);
        Assert.assertNotNull(assertTags);
        Assert.assertEquals(1, assertTags.length);
        TagData tag = assertTags[0];
        Assert.assertTrue((tag.Id == tagData.Id) && tag.Name.equals(tagData.Name));
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
        final CountDownLatch signal = new CountDownLatch(2);

        // Create tag
        TagData tagData = new TagData();
        tagData.Id = 4;
        tagData.Name = "Systemutveckling";
        insertTag(db, tagData, signal);

        // Create office
        OfficeData officeData = new OfficeData();
        officeData.Id = 17;
        officeData.Name = "Jönköping";
        insertOffice(db, officeData, signal);

        signal.await(10, TimeUnit.SECONDS);
        Assert.assertTrue(signal.getCount() == 0);

        ConsultantData consultantData = new ConsultantData();
        consultantData.Id = 6985;
        consultantData.Name = "Daniel Persson";
        consultantData.JobRole = "Systemutväcklare";
        consultantData.Description = "Daniel är en glad och positiv kille, förstås, vad skulle vi annars skriva här?";
        consultantData.OfficeId = 999; // Wrong!!

        // First try to insert with faulty office ref
        final CountDownLatch signal4 = new CountDownLatch(1);
        hadExpectedBehavior = false;
        db.insertConsultant(consultantData, new DbOperationListener() {
            @Override
            public void onResult(long _id) {
                signal4.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                hadExpectedBehavior = true;
                Log.e(TAG, errorMessage);
                signal4.countDown();
            }
        });

        signal4.await(10, TimeUnit.SECONDS);
        Assert.assertTrue(hadExpectedBehavior);

        // Then change the office link and try again
        consultantData.OfficeId = officeData.Id;

        final CountDownLatch signal2 = new CountDownLatch(1);
        hadExpectedBehavior = false;
        db.insertConsultant(consultantData, new DbOperationListener() {
            @Override
            public void onResult(long _id) {
                hadExpectedBehavior = true;
                signal2.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, errorMessage);
            }
        });

        signal2.await(10, TimeUnit.SECONDS);
        Assert.assertTrue(hadExpectedBehavior);

        final CountDownLatch signal3 = new CountDownLatch(1);
        // Read back all consultants and make sure that the correct one is there
        assertConsultants = null;
        db = new KvadratTestDb(ctx);
        db.getAllConsultants(new DbDataListener<ConsultantData[]>() {
            @Override
            public void onResult(ConsultantData[] consultants) {
                assertConsultants = consultants;
                signal3.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                signal3.countDown();
            }
        });

        signal3.await(10, TimeUnit.SECONDS);
        Assert.assertNotNull(assertConsultants);
        ConsultantData foundConsultant = null;
        for (ConsultantData consultant : assertConsultants) {
            if (consultant.Id == consultantData.Id)
                foundConsultant = consultant;
        }
        Assert.assertNotNull(foundConsultant);
        Assert.assertEquals(consultantData.Id, foundConsultant.Id);
    }
}
