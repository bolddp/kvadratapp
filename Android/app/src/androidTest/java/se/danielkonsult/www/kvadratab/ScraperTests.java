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
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.helpers.scraper.WebPageScraper;
import se.danielkonsult.www.kvadratab.helpers.scraper.ConsultantDataParser;
import se.danielkonsult.www.kvadratab.helpers.scraper.SummaryData;
import se.danielkonsult.www.kvadratab.helpers.scraper.SummaryDataParser;

/**
 * Tests of web page scraper functionality.
 */
@RunWith(AndroidJUnit4.class)
public class ScraperTests {

    @Test
    public void shouldParseMainPageWebData(){

        final String testData = "<a class='single-consultant' href='../profil/?id=8106'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8106\"><div class='full-name'>Jan Abramsson</div><div class='job-role'>Testledning</div><div class='description'>Jan är en driven konsult med stor erfarenhet och f&hellip;</div></a><a class='single-consultant' href='../profil/?id=7830'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7830\"><div class='full-name'>Mats Adolfsson</div><div class='job-role'>Teknisk Projektledare</div><div class='description'>Mats har en lång erfarenhet av roller såsom linjec&hellip;</div></a><a class='single-consultant' href='../profil/?id=7031'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7031\"><div class='full-name'>Mona Ahlgren</div><div class='job-role'>Projektledare (PMP certifierad)</div><div class='description'>Monas huvudinriktning är projektledning med lång e&hellip;</div></a><a class='single-consultant' href='../profil/?id=8222'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8222\"><div class='full-name'>Serdil Akyuz</div><div class='job-role'>Javautvecklare</div><div class='description'>Serdil har stor erfarenhet av systemutveckling fra&hellip;</div></a><a class='single-consultant' href='../profil/?id=10'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=10\"><div class='full-name'>Peter Almeren</div><div class='job-role'>Verksamhetsanalytiker</div><div class='description'>Peter har över 40 års erfarenhet som konsult, allt&hellip;</div></a><a class='single-consultant' href='../profil/?id=7295'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7295\"><div class='full-name'>Simon Almström</div><div class='job-role'>Agile coach</div><div class='description'>Simon är precis den person som du vill ha på ditt &hellip;</div></a><a class='single-consultant' href='../profil/?id=3692'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=3692\"><div class='full-name'>Magnus Altin</div><div class='job-role'>Systemutvecklare</div><div class='description'>Systemutveckling och design främst av webbaserade &hellip;</div></a><a class='single-consultant' href='../profil/?id=6597'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6597\"><div class='full-name'>Håkan Alvgren</div><div class='job-role'>Projektledare</div><div class='description'>Som konsult har Håkan mestadels arbetat med projek&hellip;</div></a><a class='single-consultant' href='../profil/?id=6357'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6357\"><div class='full-name'>Peter Amling</div><div class='job-role'>Förändringsledare, projektledare</div><div class='description'>Peter har en bred erfarenhet av förändringsarbete &hellip;</div></a><a class='single-consultant' href='../profil/?id=43'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=43\"><div class='full-name'>Olle Anderson</div><div class='job-role'>Business Intelligence</div><div class='description'>Har mer än 15 års erfarenhet av system- och databa&hellip;</div></a><a class='single-consultant' href='../profil/?id=59'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=59\"><div class='full-name'>Ulf H Andersson</div><div class='job-role'>Systemdesigner</div><div class='description'>Mycket erfaren systemutvecklare. Arbetar gärna när&hellip;</div></a><a class='single-consultant' href='../profil/?id=5804'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=5804\"><div class='full-name'>Henrik Andoff</div><div class='job-role'>Arkitektur & Systemering</div><div class='description'>Civilingenjör Datateknik med examen 1997. Har seda&hellip;</div></a><a class='single-consultant' href='../profil/?id=42'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=42\"><div class='full-name'>Kjersti Anthonisen</div><div class='job-role'>System-/Kravanalytiker</div><div class='description'>Kjersti har lång erfarenhet av system-/kravanalys &hellip;</div></a><a class='single-consultant' href='../profil/?id=6573'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6573\"><div class='full-name'>Kristoffer Arvidsson</div><div class='job-role'>Managementkonsult</div><div class='description'>Kristoffer har en lång erfarenhet som managementko&hellip;</div></a><a class='single-consultant' href='../profil/?id=7406'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7406\"><div class='full-name'>Peter Arvidsson</div><div class='job-role'>Projektledare, Scrum Master</div><div class='description'>Peter Arvidsson är en senior konsult som sedan 199&hellip;</div></a><a class='single-consultant' href='../profil/?id=8206'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8206\"><div class='full-name'>Martin Asztely</div><div class='job-role'>Program and Project Manager</div><div class='description'>My main capabilities lies in analysing, planning, &hellip;</div></a><a class='single-consultant' href='../profil/?id=7169'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7169\"><div class='full-name'>Daniel Attström</div><div class='job-role'>Projektledare, lösningsarkitekt, analys</div><div class='description'>Daniel arbetar och trivs bäst i projektledarroller&hellip;</div></a><a class='single-consultant' href='../profil/?id=4558'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=4558\"><div class='full-name'>Bobbie Baars</div><div class='job-role'>Senior projektledare (IPMA-B certifierad)</div><div class='description'>Om Bobbie får förtroendet att leda ert projekt får&hellip;</div></a><a class='single-consultant' href='../profil/?id=6457'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6457\"><div class='full-name'>Anna Balksjö</div><div class='job-role'>Projektledare (PRINCE2)</div><div class='description'>Anna Balksjö är en erfaren projektledare som har e&hellip;</div></a><a class='single-consultant' href='../profil/?id=6743'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6743\"><div class='full-name'>Elin Berg</div><div class='job-role'>Systemutvecklare</div><div class='description'>Elin har arbetat med utveckling av webb- och Windo&hellip;</div></a><a class='single-consultant' href='../profil/?id=3981'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=3981\"><div class='full-name'>Mikael Berg</div><div class='job-role'>Projektledare PMP, PMI-ACP</div><div class='description'>Mikael har lång erfarenhet inom områdena projektle&hellip;</div></a><a class='single-consultant' href='../profil/?id=7397'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7397\"><div class='full-name'>Markus Berglund</div><div class='job-role'>Systemarkitekt</div><div class='description'>Jag har bred erfarenhet inom utveckling och forskn&hellip;</div></a><a class='single-consultant' href='../profil/?id=5164'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=5164\"><div class='full-name'>Anna Bergqvist</div><div class='job-role'>Kommunikationskonsult</div><div class='description'>Anna är en erfaren och driven kommunikatör och pro&hellip;</div></a><a class='single-consultant' href='../profil/?id=7833'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7833\"><div class='full-name'>Mikael Bergström</div><div class='job-role'>Systemutveckling</div><div class='description'>Mikael har en lång och gedigen erfarenhet av syste&hellip;</div></a><a class='single-consultant' href='../profil/?id=75'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=75\"><div class='full-name'>Abtin Bidarian</div><div class='job-role'>Programmering</div><div class='description'>Abtin är en ambitiös och skicklig systemutvecklare&hellip;</div></a><a class='single-consultant' href='../profil/?id=12'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=12\"><div class='full-name'>David Bjurström</div><div class='job-role'>IT-arkitekt</div><div class='description'>David har mer än 18 års erfarenhet av Systemutveck&hellip;</div></a><a class='single-consultant' href='../profil/?id=7786'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7786\"><div class='full-name'>Simon Björklén</div><div class='job-role'>Mjukvaruarkitekt</div><div class='description'>Simon är en Datavetare som arbetar som mjukvaruark&hellip;</div></a><a class='single-consultant' href='../profil/?id=4783'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=4783\"><div class='full-name'>Peder Björklund</div><div class='job-role'>Verksamhets- och affärsarkitekt</div><div class='description'>Peder Björklund har sedan 1996 framgångsrikt arbet&hellip;</div></a><a class='single-consultant' href='../profil/?id=6556'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6556\"><div class='full-name'>Per Brange</div><div class='job-role'>Projektledare IPMA</div><div class='description'>Per Brange är seniorkonsult och arbetar oftast som&hellip;</div></a><a class='single-consultant' href='../profil/?id=5575'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=5575\"><div class='full-name'>Elisabet Brickman</div><div class='job-role'>Kravanalytiker</div><div class='description'>Elisabet har arbetat inom IT-branschen sedan 1999 &hellip;</div></a><a class='single-consultant' href='../profil/?id=3246'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=3246\"><div class='full-name'>Henrik Burton</div><div class='job-role'>Utredare  Projektledare  Verksamhetsutvecklare</div><div class='description'>Henrik är en erfaren seniorkonsult inom projektled&hellip;</div></a><a class='single-consultant' href='../profil/?id=8538'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8538\"><div class='full-name'>Mattias Bybro</div><div class='job-role'>Mjukvaruingenjör</div><div class='description'>Mattias har under mer än ett decennium i mjukvarub&hellip;</div></a><a class='single-consultant' href='../profil/?id=8272'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8272\"><div class='full-name'>Slava Ceornea</div><div class='job-role'>Systemutvecklare</div><div class='description'>Slava har jobbat som lead utvecklare på flera uppd&hellip;</div></a><a class='single-consultant' href='../profil/?id=8580'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8580\"><div class='full-name'>Alf Claesson</div><div class='job-role'>Pm3 specialist, projektledare och förändringsled...</div><div class='description'>Senior IT-chef och konsult. Passionerad ledare ino&hellip;</div></a><a class='single-consultant' href='../profil/?id=8338'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8338\"><div class='full-name'>Henrik Cooke</div><div class='job-role'>Utvecklare</div><div class='description'>Henrik är en driven systemutvecklare med fokus på &hellip;</div></a><a class='single-consultant' href='../profil/?id=8074'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8074\"><div class='full-name'>Mikael Cullberg</div><div class='job-role'>Projektledare</div><div class='description'>Mikael har mer än 20 års erfarenhet av IT-bransche&hellip;</div></a><a class='single-consultant' href='../profil/?id=7582'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7582\"><div class='full-name'>Annika Dahlqvist</div><div class='job-role'>Teknisk analytiker</div><div class='description'>Annika Dahlqvist har en flerårig erfarenhet av ana&hellip;</div></a><a class='single-consultant' href='../profil/?id=7964'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7964\"><div class='full-name'>Jonas Dannaeus</div><div class='job-role'>Data warehousearkitektur </div><div class='description'>Jonas har mer än 20 års erfarenhet som IT-konsult &hellip;</div></a><a class='single-consultant' href='../profil/?id=7838'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7838\"><div class='full-name'>Johan Davidsson</div><div class='job-role'>Front end-utvecklare</div><div class='description'>Johan är en dedikerad front end-utvecklare men med&hellip;</div></a><a class='single-consultant' href='../profil/?id=6975'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6975\"><div class='full-name'>Christian Delén</div><div class='job-role'>Verksamhetsutvecklare</div><div class='description'>Christian har de senaste åren arbetat som personal&hellip;</div></a><a class='single-consultant' href='../profil/?id=6282'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6282\"><div class='full-name'>Maria Delmar</div><div class='job-role'>Kravledare/Kravanalytiker</div><div class='description'>Maria arbetar främst med verksamhets- och kravanal&hellip;</div></a><a class='single-consultant' href='../profil/?id=6494'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6494\"><div class='full-name'>Kristina Diné</div><div class='job-role'>Managementkonsult</div><div class='description'>Kristina har en lång och gedigen erfarenhet som ma&hellip;</div></a><a class='single-consultant' href='../profil/?id=5922'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=5922\"><div class='full-name'>Robert Dybeck</div><div class='job-role'>Employer Branding </div><div class='description'>Jag får energi av människor och älskar att ge den &hellip;</div></a><a class='single-consultant' href='../profil/?id=5654'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=5654\"><div class='full-name'>Sven Ehn</div><div class='job-role'>Förändringsledare/Projektledare</div><div class='description'>Sven har drygt 30 års yrkeserfarenhet från olika b&hellip;</div></a><a class='single-consultant' href='../profil/?id=6976'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6976\"><div class='full-name'>Martin Ekermo</div><div class='job-role'>Systemarkitekt</div><div class='description'>Martin Ekermo är en senior och driven utvecklare o&hellip;</div></a><a class='single-consultant' href='../profil/?id=3587'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=3587\"><div class='full-name'>Kaj Ekvall</div><div class='job-role'>Affärsutvecklare</div><div class='description'>Kaj Ekvall har över 15 års erfarenhet av arbete me&hellip;</div></a>";

        ConsultantData[] webDatas = ConsultantDataParser.parse(testData);

        Assert.assertEquals(46, webDatas.length);
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

        ConsultantData[] assertConsultantDatas = WebPageScraper.scrapeConsultants(0,0);
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
        ConsultantData consultant = WebPageScraper.scrapeConsultantDetails(5833);

        // Assert the competences
        Assert.assertEquals("Ledarskaps-/Kommunikationskonsult", consultant.CompetenceAreas[0]);
        Assert.assertEquals("Föreläsare/inspiratör", consultant.CompetenceAreas[1]);
        Assert.assertEquals("Utbildare/kursledare", consultant.CompetenceAreas[2]);
        Assert.assertEquals("Workshopledare/Facilitator", consultant.CompetenceAreas[3]);

        // Assert bits and pieces of Overview and Overview2
        Assert.assertTrue(consultant.Overview.contains("Stellan har många bra referenser"));
        Assert.assertTrue(consultant.Overview2.contains("Med sin humor, värme och medmänsklighet"));
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

        ConsultantData[] assertConsultantDatas = WebPageScraper.scrapeConsultants(17,0);
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

        SummaryData assertSummaryPageData = WebPageScraper.scrapeSummaryData();
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

        Bitmap bitmap = AppCtrl.getImageService().downloadConsultantBitmapAndSaveToFile(7829);

        Assert.assertNotNull(bitmap);
        Assert.assertEquals(600, bitmap.getWidth());
        Assert.assertEquals(600, bitmap.getHeight());

        Bitmap bitmap2 = AppCtrl.getImageService().getConsultantBitmapFromFile(7829);
        Assert.assertNotNull(bitmap2);
        Assert.assertEquals(600, bitmap2.getWidth());
        Assert.assertEquals(600, bitmap2.getHeight());
    }
}
