package se.danielkonsult.www.kvadratab;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.danielkonsult.www.kvadratab.helpers.GenderHelper;
import se.danielkonsult.www.kvadratab.mocks.TestImageService;
import se.danielkonsult.www.kvadratab.mocks.TestNotificationService;
import se.danielkonsult.www.kvadratab.mocks.TestPrefsService;
import se.danielkonsult.www.kvadratab.mocks.TestRefresherService;
import se.danielkonsult.www.kvadratab.mocks.TestWebPageScraper;

/**
 * Created by bigha on 2017-03-02.
 */

@RunWith(AndroidJUnit4.class)
public class GenderHelperTests {

    private void setupTestEnvironment() {
        // Clean out the testdatabase
        Context ctx = InstrumentationRegistry.getTargetContext();
        AppCtrl.setApplicationContext(ctx);
    }

    @Test
    public void shouldCorrectlyIdentifyFemaleNames() {
        setupTestEnvironment();

        GenderHelper gh = new GenderHelper();
        Assert.assertTrue(gh.getGender("Susanne Östblom").equals("F"));
        Assert.assertTrue(gh.getGender("Buket Özavci").equals("F"));
        Assert.assertTrue(gh.getGender("Viktoria Svensson").equals("F"));
        Assert.assertTrue(gh.getGender("Elisabet Sofianidou").equals("F"));
        Assert.assertTrue(gh.getGender("Ulrika Smedman").equals("F"));
        Assert.assertTrue(gh.getGender("Jonas Uddman").equals("M"));
        Assert.assertTrue(gh.getGender("Roland Heimdahl").equals("M"));
        Assert.assertTrue(gh.getGender("Carl-Fredrik Neikter").equals("M"));
        Assert.assertTrue(gh.getGender("Niklas Mossberg").equals("M"));
        Assert.assertTrue(gh.getGender("Riku Kotiranta").equals("M"));
    }
}
