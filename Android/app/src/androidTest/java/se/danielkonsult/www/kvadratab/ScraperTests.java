package se.danielkonsult.www.kvadratab;

import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.ConsultantDetails;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.helpers.scraper.DefaultWebPageScraper;
import se.danielkonsult.www.kvadratab.helpers.scraper.ConsultantDataParser;
import se.danielkonsult.www.kvadratab.entities.SummaryData;
import se.danielkonsult.www.kvadratab.helpers.scraper.SummaryDataParser;

/**
 * Tests of web page scraper functionality.
 */
@RunWith(AndroidJUnit4.class)
public class ScraperTests {

    @Test
    public void shouldParseMainPageWebData(){

        final String testData = "<a class='single-consultant' href='../../konsult/6573-kristoffer-arvidsson'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6573\"><div class='full-name'>Kristoffer Arvidsson</div><div class='job-role'>Managementkonsult</div><div class='description'>Kristoffer har en lång erfarenhet som managementko&hellip;</div></a><a class='single-consultant' href='../../konsult/8538-mattias-bybro'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8538\"><div class='full-name'>Mattias Bybro</div><div class='job-role'>Mjukvaruingenjör</div><div class='description'>Mattias har under mer än ett decennium i mjukvarub&hellip;</div></a><a class='single-consultant' href='../../konsult/8648-magnus-carlsson'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8648\"><div class='full-name'>Magnus Carlsson</div><div class='job-role'>Senior Verksamhetskonsult</div><div class='description'>Hjälper företag att hitta rätt lösningar för att u&hellip;</div></a><a class='single-consultant' href='../../konsult/7838-johan-davidsson'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7838\"><div class='full-name'>Johan Davidsson</div><div class='job-role'>Front end-utvecklare</div><div class='description'>Johan är en dedikerad front end-utvecklare men med&hellip;</div></a><a class='single-consultant' href='../../konsult/6976-martin-ekermo'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6976\"><div class='full-name'>Martin Ekermo</div><div class='job-role'>Systemarkitekt</div><div class='description'>Martin Ekermo är en senior och driven utvecklare o&hellip;</div></a><a class='single-consultant' href='../../konsult/8623-nils-ekman'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8623\"><div class='full-name'>Nils Ekman</div><div class='job-role'>Java-utvecklare</div><div class='description'>Nils är en driven och bred mjukvaruutvecklare inom&hellip;</div></a>";

        ConsultantData[] webDatas = ConsultantDataParser.parse(testData);

        Assert.assertEquals(6, webDatas.length);
        for (ConsultantData webData : webDatas) {
            Assert.assertTrue(webData.Id > 0);
            Assert.assertTrue(webData.FirstName != null && webData.FirstName != "");
            Assert.assertTrue(webData.LastName != null && webData.LastName != "");
        }
    }

    /**
     * Tests that a complete list of consultants can be fetched from the main web page.
     * @throws Throwable
     */
    @Test
    public void shouldScrapeAllConsultantsFromWebPage() throws IOException {

        ConsultantData[] assertConsultantDatas = new DefaultWebPageScraper().scrapeConsultants(0,0);
        Assert.assertNotNull(assertConsultantDatas);

        boolean foundConsultant = false;

        // Don't assert an exact number since the web page contents most likely will change
        Assert.assertTrue(String.format("Length: %d", assertConsultantDatas.length), assertConsultantDatas.length > 240);
        for (ConsultantData webData : assertConsultantDatas) {
            Assert.assertTrue(webData.Id > 0);
            Assert.assertTrue(webData.FirstName != null && webData.FirstName != "");
            Assert.assertTrue(webData.LastName != null && webData.LastName != "");
            // Look for one specific consultant to verify parsing of names
            if (webData.FirstName.equals("Sven") && webData.LastName.equals("Ehn"))
                foundConsultant = true;
        }
        Assert.assertTrue(foundConsultant);
    }

    /**
     * Tests that the details of a consultant can be scraped from the web page.
     */
    @Test
    public void shouldScrapeConsultantDetailsFromWeb() throws IOException {
        ConsultantDetails details = new DefaultWebPageScraper().scrapeConsultantDetails(5833);

        // Assert the competences
        Assert.assertEquals("Ledarskaps-/Kommunikationskonsult", details.CompetenceAreas[0]);
        Assert.assertEquals("Föreläsare/inspiratör", details.CompetenceAreas[1]);
        Assert.assertEquals("Utbildare/kursledare", details.CompetenceAreas[2]);
        Assert.assertEquals("Workshopledare/Facilitator", details.CompetenceAreas[3]);

        // Assert bits and pieces of Description and Overview
        Assert.assertTrue(details.Description.contains("Stellan har många bra referenser"));
        Assert.assertTrue(details.Overview.contains("Med sin humor, värme och medmänsklighet"));
    }

    @Test
    public void shouldScrapeSummaryWebPage() {

        final String testData = "<form action=\"\" method=\"post\" class=\"consultants-form\" id=\"form-consultantsearch-filter\">\n" +
                "    <input class=\"consultants-search\" id=\"consultantsearch-filter\" autocomplete=\"off\" >\n" +
                "</form>\n" +
                "\n" +
                "<h2 class=\"small-heading\">Visa konsulter som kan:</h2>\n" +
                "\n" +
                "<div class=\"filter-section\" id=\"tag-filter\">\n" +
                "    <a class=\"filter-tag active-tag\" data-id=\"0\" href=\"\">Alla konsulter</a>\n" +
                "\t<a class=\"filter-tag\" data-id=\"1\" href=\"\">IT Management</a>\n" +
                "    <a class=\"filter-tag\" data-id=\"2\" href=\"\">Management</a>\n" +
                "    <a class=\"filter-tag\" data-id=\"3\" href=\"\">Projektledning</a>\n" +
                "    <a class=\"filter-tag\" data-id=\"4\" href=\"\">Systemutveckling</a>\n" +
                "    <a class=\"filter-tag\" data-id=\"5\" href=\"\">Test</a>\n" +
                "    <a class=\"filter-tag\" data-id=\"6\" href=\"\">Arkitektur</a>\n" +
                "    <a class=\"filter-tag\" data-id=\"7\" href=\"\">Krav</a>\n" +
                "    <a class=\"filter-tag\" data-id=\"8\" href=\"\">Utbildning</a>\n" +
                "    <div style=\"clear:both;\"></div>\n" +
                "</div>\n" +
                "\n" +
                "<h2 class=\"small-heading\">Visa konsulter från kontoret i:</h2>\n" +
                "\n" +
                "<div class='filter-section' id='office-filter'>\n" +
                "\t<a class='filter-tag active-tag' data-id='0' href=''>Alla kontor</a>\n" +
                "\t<a class='filter-tag ' data-id='2' href=''>Stockholm</a>\n" +
                "\t<a class='filter-tag' data-id='6' href=''>Linköping</a>\n" +
                "\t<a class='filter-tag' data-id='12' href=''>Malmö</a>\n" +
                "\t<a class='filter-tag' data-id='15' href=''>Göteborg</a>\n" +
                "\t<a class='filter-tag' data-id='17' href=''>Jönköping</a>\n" +
                "\t<a class='filter-tag' data-id='18' href=''>Örebro</a>\n" +
                "\t\n" +
                "</div>\n";

        SummaryData pageData = SummaryDataParser.parse(testData);

        Assert.assertNotNull(pageData);
        Assert.assertEquals(6, pageData.OfficeDatas.length);
        Assert.assertEquals(8, pageData.TagDatas.length);

    }


    /**
     * Tests that the consultants from a specific office can be scraped properly.
     */
    @Test
    public void shouldScrapeOfficeConsultantsFromWebPage() throws Throwable {

        ConsultantData[] assertConsultantDatas = new DefaultWebPageScraper().scrapeConsultants(17,0);
        Assert.assertNotNull(assertConsultantDatas);
        // Don't assert an exact number since the web page contents most likely will change
        Assert.assertTrue(String.format("Length: %d", assertConsultantDatas.length), (assertConsultantDatas.length > 12) && (assertConsultantDatas.length < 50));
        for (ConsultantData webData : assertConsultantDatas) {
            Assert.assertTrue(webData.Id > 0);
            Assert.assertTrue(webData.FirstName != null && webData.FirstName != "");
            Assert.assertTrue(webData.LastName != null && webData.LastName != "");
        }
    }

    /**
     * Tests that the summary data (available offices and tags) can be scraped from the web page.
     */
    @Test
    public void shouldScrapeSummaryDataFromWebPage() throws Throwable {

        SummaryData assertSummaryPageData = new DefaultWebPageScraper().scrapeSummaryData();
        Assert.assertNotNull(assertSummaryPageData);

        // Don't assert an exact number since the web page contents most likely will change
        Assert.assertTrue(String.format("Offices length: %d", assertSummaryPageData.OfficeDatas.length), (assertSummaryPageData.OfficeDatas.length > 5) && (assertSummaryPageData.OfficeDatas.length < 10));
        for (OfficeData officeData : assertSummaryPageData.OfficeDatas) {
            Assert.assertTrue(officeData.Id > 0);
            Assert.assertTrue(officeData.Name != null && officeData.Name != "");
        }
        Assert.assertTrue(String.format("Tags length: %d", assertSummaryPageData.TagDatas.length), (assertSummaryPageData.TagDatas.length > 7) && (assertSummaryPageData.TagDatas.length < 10));
        for (TagData tagData : assertSummaryPageData.TagDatas) {
            Assert.assertTrue(tagData.Id > 0);
            Assert.assertTrue(tagData.Name != null && tagData.Name != "");
        }
    }

    /**
     * Tests that an image can be downloaded from the web page, saved to file
     * and then read back from file.
     */
    @Test
    public void shouldDownloadConsultantImage() throws IOException {

        AppCtrl.setApplicationContext(InstrumentationRegistry.getTargetContext());

        // Delete the image file if there is any
        FilenameFilter imageFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.startsWith("img_consultant")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        File appDir = AppCtrl.getApplicationContext().getFilesDir();
        File[] files = appDir.listFiles(imageFilter);
        for (File file : files)
            file.delete();

        Bitmap bitmap = AppCtrl.getImageService().downloadConsultantBitmap(7829);
        Assert.assertNotNull(bitmap);
        Assert.assertEquals(600, bitmap.getWidth());
        Assert.assertEquals(600, bitmap.getHeight());

        AppCtrl.getImageService().saveConsultantBitmapToFile(7829, bitmap);

        Bitmap bitmap2 = AppCtrl.getImageService().getConsultantBitmapFromFile(7829);
        Assert.assertNotNull(bitmap2);
        Assert.assertEquals(600, bitmap2.getWidth());
        Assert.assertEquals(600, bitmap2.getHeight());
    }
}
