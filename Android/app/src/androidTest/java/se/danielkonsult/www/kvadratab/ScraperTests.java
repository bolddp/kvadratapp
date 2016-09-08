package se.danielkonsult.www.kvadratab;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import se.danielkonsult.www.kvadratab.entities.MainPageWebData;
import se.danielkonsult.www.kvadratab.services.scraper.MainPageParser;
import se.danielkonsult.www.kvadratab.services.scraper.MainPageScraper;
import se.danielkonsult.www.kvadratab.services.scraper.MainPageScraperListener;
import se.danielkonsult.www.kvadratab.services.scraper.SummaryPageData;
import se.danielkonsult.www.kvadratab.services.scraper.SummaryPageParser;

/**
 * Tests of web page scraper functionality.
 */
@RunWith(AndroidJUnit4.class)
public class ScraperTests {

    static MainPageWebData[] assertMainPageWebDatas;

    @Test
    public void shouldParseMainPageWebData(){

        final String testData = "<a class='single-consultant' href='../profil/?id=8106'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8106\"><div class='full-name'>Jan Abramsson</div><div class='job-role'>Testledning</div><div class='description'>Jan är en driven konsult med stor erfarenhet och f&hellip;</div></a><a class='single-consultant' href='../profil/?id=7830'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7830\"><div class='full-name'>Mats Adolfsson</div><div class='job-role'>Teknisk Projektledare</div><div class='description'>Mats har en lång erfarenhet av roller såsom linjec&hellip;</div></a><a class='single-consultant' href='../profil/?id=7031'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7031\"><div class='full-name'>Mona Ahlgren</div><div class='job-role'>Projektledare (PMP certifierad)</div><div class='description'>Monas huvudinriktning är projektledning med lång e&hellip;</div></a><a class='single-consultant' href='../profil/?id=8222'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8222\"><div class='full-name'>Serdil Akyuz</div><div class='job-role'>Javautvecklare</div><div class='description'>Serdil har stor erfarenhet av systemutveckling fra&hellip;</div></a><a class='single-consultant' href='../profil/?id=10'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=10\"><div class='full-name'>Peter Almeren</div><div class='job-role'>Verksamhetsanalytiker</div><div class='description'>Peter har över 40 års erfarenhet som konsult, allt&hellip;</div></a><a class='single-consultant' href='../profil/?id=7295'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7295\"><div class='full-name'>Simon Almström</div><div class='job-role'>Agile coach</div><div class='description'>Simon är precis den person som du vill ha på ditt &hellip;</div></a><a class='single-consultant' href='../profil/?id=3692'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=3692\"><div class='full-name'>Magnus Altin</div><div class='job-role'>Systemutvecklare</div><div class='description'>Systemutveckling och design främst av webbaserade &hellip;</div></a><a class='single-consultant' href='../profil/?id=6597'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6597\"><div class='full-name'>Håkan Alvgren</div><div class='job-role'>Projektledare</div><div class='description'>Som konsult har Håkan mestadels arbetat med projek&hellip;</div></a><a class='single-consultant' href='../profil/?id=6357'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6357\"><div class='full-name'>Peter Amling</div><div class='job-role'>Förändringsledare, projektledare</div><div class='description'>Peter har en bred erfarenhet av förändringsarbete &hellip;</div></a><a class='single-consultant' href='../profil/?id=43'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=43\"><div class='full-name'>Olle Anderson</div><div class='job-role'>Business Intelligence</div><div class='description'>Har mer än 15 års erfarenhet av system- och databa&hellip;</div></a><a class='single-consultant' href='../profil/?id=59'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=59\"><div class='full-name'>Ulf H Andersson</div><div class='job-role'>Systemdesigner</div><div class='description'>Mycket erfaren systemutvecklare. Arbetar gärna när&hellip;</div></a><a class='single-consultant' href='../profil/?id=5804'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=5804\"><div class='full-name'>Henrik Andoff</div><div class='job-role'>Arkitektur & Systemering</div><div class='description'>Civilingenjör Datateknik med examen 1997. Har seda&hellip;</div></a><a class='single-consultant' href='../profil/?id=42'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=42\"><div class='full-name'>Kjersti Anthonisen</div><div class='job-role'>System-/Kravanalytiker</div><div class='description'>Kjersti har lång erfarenhet av system-/kravanalys &hellip;</div></a><a class='single-consultant' href='../profil/?id=6573'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6573\"><div class='full-name'>Kristoffer Arvidsson</div><div class='job-role'>Managementkonsult</div><div class='description'>Kristoffer har en lång erfarenhet som managementko&hellip;</div></a><a class='single-consultant' href='../profil/?id=7406'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7406\"><div class='full-name'>Peter Arvidsson</div><div class='job-role'>Projektledare, Scrum Master</div><div class='description'>Peter Arvidsson är en senior konsult som sedan 199&hellip;</div></a><a class='single-consultant' href='../profil/?id=8206'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8206\"><div class='full-name'>Martin Asztely</div><div class='job-role'>Program and Project Manager</div><div class='description'>My main capabilities lies in analysing, planning, &hellip;</div></a><a class='single-consultant' href='../profil/?id=7169'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7169\"><div class='full-name'>Daniel Attström</div><div class='job-role'>Projektledare, lösningsarkitekt, analys</div><div class='description'>Daniel arbetar och trivs bäst i projektledarroller&hellip;</div></a><a class='single-consultant' href='../profil/?id=4558'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=4558\"><div class='full-name'>Bobbie Baars</div><div class='job-role'>Senior projektledare (IPMA-B certifierad)</div><div class='description'>Om Bobbie får förtroendet att leda ert projekt får&hellip;</div></a><a class='single-consultant' href='../profil/?id=6457'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6457\"><div class='full-name'>Anna Balksjö</div><div class='job-role'>Projektledare (PRINCE2)</div><div class='description'>Anna Balksjö är en erfaren projektledare som har e&hellip;</div></a><a class='single-consultant' href='../profil/?id=6743'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6743\"><div class='full-name'>Elin Berg</div><div class='job-role'>Systemutvecklare</div><div class='description'>Elin har arbetat med utveckling av webb- och Windo&hellip;</div></a><a class='single-consultant' href='../profil/?id=3981'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=3981\"><div class='full-name'>Mikael Berg</div><div class='job-role'>Projektledare PMP, PMI-ACP</div><div class='description'>Mikael har lång erfarenhet inom områdena projektle&hellip;</div></a><a class='single-consultant' href='../profil/?id=7397'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7397\"><div class='full-name'>Markus Berglund</div><div class='job-role'>Systemarkitekt</div><div class='description'>Jag har bred erfarenhet inom utveckling och forskn&hellip;</div></a><a class='single-consultant' href='../profil/?id=5164'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=5164\"><div class='full-name'>Anna Bergqvist</div><div class='job-role'>Kommunikationskonsult</div><div class='description'>Anna är en erfaren och driven kommunikatör och pro&hellip;</div></a><a class='single-consultant' href='../profil/?id=7833'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7833\"><div class='full-name'>Mikael Bergström</div><div class='job-role'>Systemutveckling</div><div class='description'>Mikael har en lång och gedigen erfarenhet av syste&hellip;</div></a><a class='single-consultant' href='../profil/?id=75'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=75\"><div class='full-name'>Abtin Bidarian</div><div class='job-role'>Programmering</div><div class='description'>Abtin är en ambitiös och skicklig systemutvecklare&hellip;</div></a><a class='single-consultant' href='../profil/?id=12'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=12\"><div class='full-name'>David Bjurström</div><div class='job-role'>IT-arkitekt</div><div class='description'>David har mer än 18 års erfarenhet av Systemutveck&hellip;</div></a><a class='single-consultant' href='../profil/?id=7786'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7786\"><div class='full-name'>Simon Björklén</div><div class='job-role'>Mjukvaruarkitekt</div><div class='description'>Simon är en Datavetare som arbetar som mjukvaruark&hellip;</div></a><a class='single-consultant' href='../profil/?id=4783'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=4783\"><div class='full-name'>Peder Björklund</div><div class='job-role'>Verksamhets- och affärsarkitekt</div><div class='description'>Peder Björklund har sedan 1996 framgångsrikt arbet&hellip;</div></a><a class='single-consultant' href='../profil/?id=6556'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6556\"><div class='full-name'>Per Brange</div><div class='job-role'>Projektledare IPMA</div><div class='description'>Per Brange är seniorkonsult och arbetar oftast som&hellip;</div></a><a class='single-consultant' href='../profil/?id=5575'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=5575\"><div class='full-name'>Elisabet Brickman</div><div class='job-role'>Kravanalytiker</div><div class='description'>Elisabet har arbetat inom IT-branschen sedan 1999 &hellip;</div></a><a class='single-consultant' href='../profil/?id=3246'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=3246\"><div class='full-name'>Henrik Burton</div><div class='job-role'>Utredare  Projektledare  Verksamhetsutvecklare</div><div class='description'>Henrik är en erfaren seniorkonsult inom projektled&hellip;</div></a><a class='single-consultant' href='../profil/?id=8538'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8538\"><div class='full-name'>Mattias Bybro</div><div class='job-role'>Mjukvaruingenjör</div><div class='description'>Mattias har under mer än ett decennium i mjukvarub&hellip;</div></a><a class='single-consultant' href='../profil/?id=8272'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8272\"><div class='full-name'>Slava Ceornea</div><div class='job-role'>Systemutvecklare</div><div class='description'>Slava har jobbat som lead utvecklare på flera uppd&hellip;</div></a><a class='single-consultant' href='../profil/?id=8580'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8580\"><div class='full-name'>Alf Claesson</div><div class='job-role'>Pm3 specialist, projektledare och förändringsled...</div><div class='description'>Senior IT-chef och konsult. Passionerad ledare ino&hellip;</div></a><a class='single-consultant' href='../profil/?id=8338'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8338\"><div class='full-name'>Henrik Cooke</div><div class='job-role'>Utvecklare</div><div class='description'>Henrik är en driven systemutvecklare med fokus på &hellip;</div></a><a class='single-consultant' href='../profil/?id=8074'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=8074\"><div class='full-name'>Mikael Cullberg</div><div class='job-role'>Projektledare</div><div class='description'>Mikael har mer än 20 års erfarenhet av IT-bransche&hellip;</div></a><a class='single-consultant' href='../profil/?id=7582'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7582\"><div class='full-name'>Annika Dahlqvist</div><div class='job-role'>Teknisk analytiker</div><div class='description'>Annika Dahlqvist har en flerårig erfarenhet av ana&hellip;</div></a><a class='single-consultant' href='../profil/?id=7964'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7964\"><div class='full-name'>Jonas Dannaeus</div><div class='job-role'>Data warehousearkitektur </div><div class='description'>Jonas har mer än 20 års erfarenhet som IT-konsult &hellip;</div></a><a class='single-consultant' href='../profil/?id=7838'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=7838\"><div class='full-name'>Johan Davidsson</div><div class='job-role'>Front end-utvecklare</div><div class='description'>Johan är en dedikerad front end-utvecklare men med&hellip;</div></a><a class='single-consultant' href='../profil/?id=6975'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6975\"><div class='full-name'>Christian Delén</div><div class='job-role'>Verksamhetsutvecklare</div><div class='description'>Christian har de senaste åren arbetat som personal&hellip;</div></a><a class='single-consultant' href='../profil/?id=6282'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6282\"><div class='full-name'>Maria Delmar</div><div class='job-role'>Kravledare/Kravanalytiker</div><div class='description'>Maria arbetar främst med verksamhets- och kravanal&hellip;</div></a><a class='single-consultant' href='../profil/?id=6494'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6494\"><div class='full-name'>Kristina Diné</div><div class='job-role'>Managementkonsult</div><div class='description'>Kristina har en lång och gedigen erfarenhet som ma&hellip;</div></a><a class='single-consultant' href='../profil/?id=5922'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=5922\"><div class='full-name'>Robert Dybeck</div><div class='job-role'>Employer Branding </div><div class='description'>Jag får energi av människor och älskar att ge den &hellip;</div></a><a class='single-consultant' href='../profil/?id=5654'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=5654\"><div class='full-name'>Sven Ehn</div><div class='job-role'>Förändringsledare/Projektledare</div><div class='description'>Sven har drygt 30 års yrkeserfarenhet från olika b&hellip;</div></a><a class='single-consultant' href='../profil/?id=6976'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=6976\"><div class='full-name'>Martin Ekermo</div><div class='job-role'>Systemarkitekt</div><div class='description'>Martin Ekermo är en senior och driven utvecklare o&hellip;</div></a><a class='single-consultant' href='../profil/?id=3587'><img class=\"profile-picture\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=3587\"><div class='full-name'>Kaj Ekvall</div><div class='job-role'>Affärsutvecklare</div><div class='description'>Kaj Ekvall har över 15 års erfarenhet av arbete me&hellip;</div></a>";

        MainPageWebData[] webDatas = MainPageParser.parse(testData);

        Assert.assertEquals(46, webDatas.length);
        for (MainPageWebData webData : webDatas) {
            Assert.assertTrue(webData.Id > 0);
            Assert.assertTrue(webData.Name != null && webData.Name != "");
        }
    }

    @Test
    public void shouldScrapeActualWebPage() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        assertMainPageWebDatas = null;
        MainPageScraper scraper = new MainPageScraper(new MainPageScraperListener() {
            @Override
            public void onResult(MainPageWebData[] webDatas) {
                assertMainPageWebDatas = webDatas;
                signal.countDown();
            }

            @Override
            public void onError(int statusCode, String message) {
                signal.countDown();
            }
        });
        scraper.execute();
        signal.await(10, TimeUnit.SECONDS);

        Assert.assertNotNull(assertMainPageWebDatas);
        // Don't assert an exact number since the web page contents most likely will change
        Assert.assertTrue(String.format("Length: %d", assertMainPageWebDatas.length), assertMainPageWebDatas.length > 240);
        for (MainPageWebData webData : assertMainPageWebDatas) {
            Assert.assertTrue(webData.Id > 0);
            Assert.assertTrue(webData.Name != null && webData.Name != "");
        }
    }

    @Test
    public void shouldScrapeSummaryWebPage() {

        final String testData = "HTTP/1.1 200 OK\n" +
                "Content-Type: text/html; charset=UTF-8\n" +
                "Server: Microsoft-IIS/7.5\n" +
                "X-Powered-By: PHP/5.4.24\n" +
                "X-Pingback: http://www.kvadrat.se/xmlrpc.php\n" +
                "Link: <http://www.kvadrat.se/?p=110>; rel=shortlink\n" +
                "X-Powered-By: ASP.NET\n" +
                "Date: Thu, 08 Sep 2016 20:36:57 GMT\n" +
                "Content-Length: 17408\n" +
                "\n" +
                "<!DOCTYPE html>\n" +
                "<html xmlns:og=\"http://ogp.me/ns#\" xmlns:fb=\"https://www.facebook.com/2008/fbml\">\n" +
                "\t<head>\n" +
                "\t\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "\t\t<meta charset=\"utf-8\" />\n" +
                "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\" />\n" +
                "\t\t<meta name=\"description\" content=\"\">\n" +
                "\t\t<meta property=\"fb:app_id\" content=\"\" />\n" +
                "\t\t<meta property=\"fb:admins\" content=\"\" />\n" +
                "\t\t<!-- <meta property=\"og:image\" content=\"http://www.kvadrat.se/img/logo.png\" />\t -->\n" +
                "\t\t<meta property='og:url' content='http://www.kvadrat.se'/>\n" +
                "\t\t<meta property='og:site_name' content='Kvadrat.se'/>\n" +
                "\t\t<meta property='og:description' content=''/>\n" +
                "\t\t<meta property='og:type' content='website'/>\n" +
                "\t\t<meta property='og:image' content='http://www.kvadrat.se/logotyp_og.gif'/>\n" +
                "\n" +
                "\t\t<title>Kvadrat.se &raquo; Konsulter</title>    \n" +
                "\n" +
                "\t\t<link rel=\"shortcut icon\" href=\"http://www.kvadrat.se/wp-content/themes/blocks/favicon.ico\" type=\"image/x-icon\" />\n" +
                "\n" +
                "\t\t<link rel=\"apple-touch-icon-precomposed\" href=\"/apple_57.png\" />\n" +
                "\t\t<link rel=\"apple-touch-icon-precomposed\" sizes=\"72x72\" href=\"/apple_72.png\" />\n" +
                "\t\t<link rel=\"apple-touch-icon-precomposed\" sizes=\"114x114\" href=\"/apple_114.png\" />\n" +
                "\t\t<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.6.1/css/font-awesome.min.css\">\n" +
                "\t\t<script src='http://www.kvadrat.se/wp-content/themes/blocks/external/js/custom-selectbox.js' defer></script>\n" +
                "\t\t<!-- Start of 'Sharethis' related -->\n" +
                "\t\t<script type=\"text/javascript\">var switchTo5x= true;</script><!--without this, wrong type of cursor on all pages...-->\n" +
                "\t\t<script type=\"text/javascript\" src=\"http://w.sharethis.com/button/buttons.js\"></script>\n" +
                "\t\t<script type=\"text/javascript\">stLight.options({publisher: \"4fae27fe-5ddf-48d3-a391-f3f9a65b43e9\", doNotHash: true, doNotCopy: false, hashAddressBar: false, onhover:false});</script>\n" +
                "\t\t<!-- End of 'Sharethis' related -->\n" +
                "\t\t<!-- Start of 'ModalPopup' related -->\n" +
                "\t\t<link rel=\"stylesheet\" href='http://www.kvadrat.se/wp-content/themes/blocks/external/css/overlay.css'>\n" +
                "\n" +
                "\t\t<!--<script src='http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js'></script>-->\n" +
                "\t\t<script src='http://www.kvadrat.se/wp-content/themes/blocks/external/jquery/jquery.min.js'></script>\n" +
                "\t\t<script src='http://www.kvadrat.se/wp-content/themes/blocks/external/jquery/overlay.js'></script>\n" +
                "\t\t<!--<script src='http://www.kvadrat.se/wp-content/themes/blocks/external/jquery/overlay.min.js'></script>-->\n" +
                "\t\t<!-- Vendemore Tracking -->\n" +
                "\t\t<script>document.write(\"<script type='text/javascript' src='https://d2hya7iqhf5w3h.cloudfront.net/scripts/analytics.js?v=\" + Date.now() + \"' async><\\/script>\");</script>\n" +
                "\t\t<meta http-equiv=\"Cache-control\" content=\"no-cache\">\n" +
                "\n" +
                "\t\t\n" +
                "\t\n" +
                "\t\t<!-- End of 'ModalPopup' related -->\n" +
                "\t\t<!-- CMS automatic header start -->      \n" +
                "    <link rel=\"alternate\" type=\"application/rss+xml\" title=\"Kvadrat.se &raquo; Konsulter kommentarsflöde\" href=\"http://www.kvadrat.se/konsulter/konsulter/feed/\" />\n" +
                "<link rel='stylesheet' id='wprls-style-css'  href='http://www.kvadrat.se/wp-content/plugins/slider-slideshow/admin/includes/../css/public/slider-pro.min.css?ver=4.1.9' type='text/css' media='all' />\n" +
                "<link rel='stylesheet' id='theme-style-css'  href='http://www.kvadrat.se/wp-content/themes/blocks/css/styles.css?ver=20130102' type='text/css' media='all' />\n" +
                "<link rel='stylesheet' id='cpsh-shortcodes-css'  href='http://www.kvadrat.se/wp-content/plugins/column-shortcodes/assets/css/shortcodes.css?ver=0.6.6' type='text/css' media='all' />\n" +
                "<script type='text/javascript' src='http://www.kvadrat.se/wp-includes/js/jquery/jquery.js?ver=1.11.1'></script>\n" +
                "<script type='text/javascript' src='http://www.kvadrat.se/wp-includes/js/jquery/jquery-migrate.min.js?ver=1.2.1'></script>\n" +
                "<script type='text/javascript' src='http://www.kvadrat.se/wp-content/plugins/slider-slideshow/admin/includes/../js/public/rsslider.js?ver=4.1.9'></script>\n" +
                "<link rel=\"EditURI\" type=\"application/rsd+xml\" title=\"RSD\" href=\"http://www.kvadrat.se/xmlrpc.php?rsd\" />\n" +
                "<link rel=\"wlwmanifest\" type=\"application/wlwmanifest+xml\" href=\"http://www.kvadrat.se/wp-includes/wlwmanifest.xml\" /> \n" +
                "<meta name=\"generator\" content=\"WordPress 4.1.9\" />\n" +
                "      \n" +
                "\t\t<!-- CMS automatic header end --> \n" +
                "<script>\n" +
                "  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n" +
                "  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n" +
                "  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n" +
                "  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');\n" +
                "\n" +
                "  ga('create', 'UA-55987518-1', 'auto');\n" +
                "  ga('send', 'pageview');\n" +
                "</script>     \n" +
                "\t</head>\n" +
                "\t<body class='unknown' id='body'>\n" +
                "  \n" +
                "<header>\n" +
                "<!--[if gte IE 9]>\n" +
                "  <style type=\"text/css\">\n" +
                "    .gradient {\n" +
                "       filter: none;\n" +
                "    }\n" +
                "  </style>\n" +
                "<![endif]-->\n" +
                "  <div class='wrapper' title=\"\">\n" +
                "    <a href=\"/\" class='logo-container'>\n" +
                "      <div class='logo'></div>\n" +
                "    </a>  \n" +
                "    <nav class='navigation-container'>\n" +
                "\t\t<ul class=\"menu-container\">\n" +
                "      <li class='menu-item'><a href='http://www.kvadrat.se/'>Start</a></li><li class='menu-item'><a href='http://www.kvadrat.se/nyheter/'>Nyheter</a></li><li class='menu-item'><a href='http://www.kvadrat.se/blogg/'>Blogg</a></li><li class='menu-item'><a href='http://www.kvadrat.se/kalender/'>Kalender</a></li><li class='menu-item'><a href='http://www.kvadrat.se/om-oss/'>Om oss</a></li><li class='menu-item'><a href='http://www.kvadrat.se/kontakt/'>Kontakt</a></li><li class='menu-item'><a href='http://www.kvadrat.se/logga-in-pa-friendweb/'>Logga in på vår Friendsweb</a></li>\t\t\n" +
                "\t\t</ul>\n" +
                "     <div class='mobile-menu-container'>\n" +
                "\t\t\t <div class='mobile-menu'>\n" +
                "\t\t\t\t\t\t<select>\n" +
                "\t\t\t\t\t\t\t<option value=\"/\">Hem</option>\n" +
                "\t\t\t\t\t\t\t<option value=\"/bli-kvadratare\">Bli kvadratare</option>\n" +
                "\t\t\t\t\t\t\t<option value='/bli-kvadratare/att-vara-kvadratare'> - Att vara kvadratare</option><option value='/bli-kvadratare/hur-det-fungerar'> - Varför Kvadrat?</option><option value='/bli-kvadratare/eget-bolag-hos-oss'> - Eget bolag hos oss</option><option value='/bli-kvadratare/mjukstart'> - Mjukstart</option><option value='/bli-kvadratare/underkonsult'> - Underkonsult</option><option value='/bli-kvadratare/kvadratboken'> - Kvadratboken</option>\t\t\t\t\t\t\t<option value=\"/konsulter\">Anlita Kvadrat</option>\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t<option value='/konsulter/konsulter'> - Konsulter</option><option value='/konsulter/referenser'> - Referenser</option><option value='/konsulter/kontakta-en-saljare'> - Kontakta en säljare</option><option value='/konsulter/publikationer'> - Publikationer</option><option value='/konsulter/erbjudande'> - Erbjudande</option>\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t<option value='http://www.kvadrat.se/'>Start</option><option value='http://www.kvadrat.se/nyheter/'>Nyheter</option><option value='http://www.kvadrat.se/blogg/'>Blogg</option><option value='http://www.kvadrat.se/kalender/'>Kalender</option><option value='http://www.kvadrat.se/om-oss/'>Om oss</option><option value='http://www.kvadrat.se/kontakt/'>Kontakt</option><option value='http://www.kvadrat.se/logga-in-pa-friendweb/'>Logga in på vår Friendsweb</option>\t\t\t\t\t\t</select>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<a class=\"mobile-search\" href=\"/mobile-search\">Sök på Kvadrat</a>\n" +
                "      </div>\n" +
                "\t  <div class='search-container'>\n" +
                "\t\t  <form action=\"/results\" method=\"post\">\n" +
                "\t\t\t<input class='search' name=\"comp\">\t  \n" +
                "\t\t  </form>\n" +
                "\t  </div>\n" +
                "\t\t<div class='shortcut-container ' id='shortcut1'><a href='/bli-kvadratare'><h2>Bli kvadratare</h2>Vill du bli konsult hos oss?</a></div><div class='shortcut-container active-tab' id='shortcut2'><a href='/konsulter'><h2>Anlita Kvadrat</h2>Vad behöver du hjälp med?</a></div>\t  \t\n" +
                "    </nav>  \n" +
                "  </div>\n" +
                "</header>\n" +
                "<div id='consultants' class='with-nav'>\n" +
                "\t<div class='wrapper'>\n" +
                "\t\t<div class='section-subnav' title=\"\">\n" +
                "\t<div class='wrapper'>\n" +
                "\t\t<ul id='subnav'>\n" +
                "        <li class=\"page_item page-item-110 current_page_item\"><a href=\"http://www.kvadrat.se/konsulter/konsulter/\">Konsulter</a></li>\n" +
                "<li class=\"page_item page-item-1455 page_item_has_children\"><a href=\"http://www.kvadrat.se/konsulter/erbjudande/\">Erbjudande</a></li>\n" +
                "<li class=\"page_item page-item-1451\"><a href=\"http://www.kvadrat.se/konsulter/publikationer/\">Publikationer</a></li>\n" +
                "<li class=\"page_item page-item-112 page_item_has_children\"><a href=\"http://www.kvadrat.se/konsulter/referenser/\">Referenser</a></li>\n" +
                "<li class=\"page_item page-item-114\"><a href=\"http://www.kvadrat.se/konsulter/kontakta-en-saljare/\">Kontakta en säljare</a></li>\n" +
                "\t\t</ul>\n" +
                "\t</div>\n" +
                "</div>\n" +
                "<div id='share-button'>Dela\n" +
                "<!--\t<span class=\"st_sharethis\" \n" +
                "\t\tst_title='Konsulter' \n" +
                "\t\tst_url='http://www.kvadrat.se/konsulter/konsulter/' \n" +
                "\t\tst_image='http://www.kvadrat.se/wp-content/themes/blocks/img/share-image.png' \n" +
                "\t\tst_summary='En konsult på Kvadrat har både lång branscherfarenhet och spetskompetens inom sitt område. Vi har strukturer för hur vi tar till vara på ny och aktuell kunskap och våra konsulter utvecklas ständigt. Därför kan vi också på Kvadrat erbjuda marknadens bästa konsulter. Välj område här bredvid och läs mer om våra konsulter och hur de kan hjälpa dig och din organisation.'></span> -->\t\n" +
                "</div>\n" +
                "<script>\n" +
                "\t\tstWidget.addEntry({\n" +
                "\t\t\t\t\t \"service\":\"sharethis\",\n" +
                "\t\t\t\t\t \"element\":document.getElementById('share-button'),\n" +
                "\t\t\t\t\t \"url\":\"http://www.kvadrat.se/konsulter/konsulter/\",\n" +
                "\t\t\t\t\t \"title\":\"Konsulter\",\n" +
                "\t\t\t\t\t \"type\":\"custom\",\n" +
                "\t\t\t\t\t \"image\":\"http://www.kvadrat.se/wp-content/themes/blocks/img/share-image.png\",\n" +
                "\t\t\t\t\t \"summary\":\"En konsult på Kvadrat har både lång branscherfarenhet och spetskompetens inom sitt område. Vi har strukturer för hur vi tar till vara på ny och aktuell kunskap och våra konsulter utvecklas ständigt. Därför kan vi också på Kvadrat erbjuda marknadens bästa konsulter. Välj område här bredvid och läs mer om våra konsulter och hur de kan hjälpa dig och din organisation.\"\n" +
                "\t\t});\n" +
                "</script>\t\n" +
                "\t\t\n" +
                "\t\t<div id='wide-content'>\n" +
                "\t\t\t<div class='section-consultants-intro' title=\"\">\n" +
                "<p id='pre-heading'>Våra konsulter</p><div class='small-text'> Våra kunder vänder sig återkommande till oss för att de uppskattar våra konsulter för deras personliga egenskaper och deras djupa kunskap inom många olika specialistområden. Orsaken är att vi är annorlunda än andra konsultföretag – på riktigt! \n" +
                "\t\tHos oss hittar du personer som lever sin dröm om att driva ett eget företag, samtidigt som dom har fördelarna av ett större konsultföretag. \n" +
                "\t\tVåra konsulter drivs av att ligga i framkant som yrkespersoner och den starka gemenskap som präglar Kvadrat. \n" +
                "</br></br>\n" +
                "En konsult på Kvadrat har både lång branscherfarenhet och spetskompetens inom sitt område. Vi har strukturer för hur vi tar till vara på ny och aktuell kunskap och våra konsulter utvecklas ständigt. Därför kan vi också på Kvadrat erbjuda marknadens bästa konsulter. Välj område här bredvid och läs mer om våra konsulter och hur de kan hjälpa dig och din organisation.\n" +
                "</br></br>\n" +
                " \n" +
                "Nedan kan du fritt söka efter konsulter med en specialistkompetens eller välja de förvalda kompetensområdena.\n" +
                " </div></div>\n" +
                "<form action=\"\" method=\"post\" class=\"consultants-form\" id=\"form-consultantsearch-filter\">\n" +
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
                "</div>\n" +
                "\n" +
                "<div style=\"clear:both;\" />\n" +
                "<img src=\"/wp-content/themes/blocks/img/ajax-loader.gif\" style=\"display:none\" id=\"ajax-loader\">\n" +
                "<div id='consultant-grid'></div>\t\t</div>\n" +
                "\t</div>\n" +
                "</div>\n" +
                "\n" +
                "  <footer>\n" +
                "  <div class='gutter-wrapper'>\t\n" +
                "\t\t\t\t<div class='office-container'>\n" +
                "\t\t\t  <div class='office'>\n" +
                "\t\t\t\t<h3>Stockholm</h3>\n" +
                "\t\t\t\t<div class='address'><a href=\"/kontakt/kontaktstockholm/\">Kvadrat Stockholm</a><br/>\n" +
                "\t\t\t\t\tSveavägen 90<br/>\n" +
                "\t\t\t\t\t104 30 Stockholm<br/>\t\t\t\t\t\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div class='number'>Tel: 08 - 441 87 00\t\t\t\t</div>\n" +
                "\t\t\t\t<div class='email'><a href=\"mailto:stockholm@kvadrat.se\">stockholm@kvadrat.se</a></div>\n" +
                "\t\t\t  </div>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t\t\t<div class='office-container'>\n" +
                "\t\t\t  <div class='office'>\n" +
                "\t\t\t\t<h3>Linköping</h3>\n" +
                "\t\t\t\t<div class='address'><a href=\"/kontakt/kontaktlinkoping/\">Kvadrat Linköping</a><br/>\n" +
                "\t\t\t\t\tRepslagaregatan 19<br/>\n" +
                "\t\t\t\t\t582 22 Linköping<br/>\t\t\t\t\t\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div class='number'>Tel: 08 - 441 87 00\t\t\t\t</div>\n" +
                "\t\t\t\t<div class='email'><a href=\"mailto:linkoping@kvadrat.se\">linkoping@kvadrat.se</a></div>\n" +
                "\t\t\t  </div>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t\t\t<div class='office-container'>\n" +
                "\t\t\t  <div class='office'>\n" +
                "\t\t\t\t<h3>Malmö</h3>\n" +
                "\t\t\t\t<div class='address'><a href=\"/kontakt/kontaktmalmo/\">Kvadrat Malmö</a><br/>\n" +
                "\t\t\t\t\tMäster Johansgatan 6<br/>\n" +
                "\t\t\t\t\t211 21 Malmö<br/>\t\t\t\t\t\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div class='number'>Tel: 040 - 12 76 30 \t\t\t\t</div>\n" +
                "\t\t\t\t<div class='email'><a href=\"mailto:malmo@kvadrat.se\">malmo@kvadrat.se</a></div>\n" +
                "\t\t\t  </div>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t\t\t<div class='office-container'>\n" +
                "\t\t\t  <div class='office'>\n" +
                "\t\t\t\t<h3>Örebro</h3>\n" +
                "\t\t\t\t<div class='address'><a href=\"/kontakt/kontaktorebro/\">Kvadrat Örebro</a><br/>\n" +
                "\t\t\t\t\tVasastrand 11<br/>\n" +
                "\t\t\t\t\t703 54 Örebro<br/>\t\t\t\t\t\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div class='number'>Tel: 08 - 441 87 00\t\t\t\t</div>\n" +
                "\t\t\t\t<div class='email'><a href=\"mailto:orebro@kvadrat.se\">orebro@kvadrat.se</a></div>\n" +
                "\t\t\t  </div>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t\t\t<div class='office-container'>\n" +
                "\t\t\t  <div class='office'>\n" +
                "\t\t\t\t<h3>Jönköping</h3>\n" +
                "\t\t\t\t<div class='address'><a href=\"/kontakt/kontaktjonkoping/\">Kvadrat Jönköping</a><br/>\n" +
                "\t\t\t\t\tSödra Strandgatan 3<br/>\n" +
                "\t\t\t\t\t553 20 Jönköping<br/>\t\t\t\t\t\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div class='number'>Tel: 08 - 441 87 00\t\t\t\t</div>\n" +
                "\t\t\t\t<div class='email'><a href=\"mailto:jonkoping@kvadrat.se\">jonkoping@kvadrat.se</a></div>\n" +
                "\t\t\t  </div>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t\t\t<div class='office-container'>\n" +
                "\t\t\t  <div class='office'>\n" +
                "\t\t\t\t<h3>Göteborg</h3>\n" +
                "\t\t\t\t<div class='address'><a href=\"/kontakt/kontaktgoteborg/\">Kvadrat Göteborg</a><br/>\n" +
                "\t\t\t\t\tKyrkogatan 13<br/>\n" +
                "\t\t\t\t\t411 15  Göteborg<br/>\t\t\t\t\t\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div class='number'>Tel: 031-21 70 20\t\t\t\t</div>\n" +
                "\t\t\t\t<div class='email'><a href=\"mailto:goteborg@kvadrat.se\">goteborg@kvadrat.se</a></div>\n" +
                "\t\t\t  </div>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t\t\t<div class='office-container'>\n" +
                "\t\t\t  <div class='office'>\n" +
                "\t\t\t\t<h3>Management</h3>\n" +
                "\t\t\t\t<div class='address'><a href=\"/kontakt/management/\">Kvadrat Management</a><br/>\n" +
                "\t\t\t\t\tSveavägen 90<br/>\n" +
                "\t\t\t\t\t104 30 Stockholm<br/>\t\t\t\t\t\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div class='number'>Tel: 08 - 441 87 00\t\t\t\t</div>\n" +
                "\t\t\t\t<div class='email'><a href=\"mailto:management@kvadrat.se\">management@kvadrat.se</a></div>\n" +
                "\t\t\t  </div>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t\t\n" +
                "\t<div id=\"follow\">\n" +
                "                    <a href=\"https://www.linkedin.com/company/1789202\" target=\"_blank\" title=\"LinkedIn\">\n" +
                "                        <i class=\"fa fa-linkedin-square fa-lg\" style=\"color:white; padding-right: 10px;\"></i>\n" +
                "                    </a>\n" +
                "                    <a href=\"https://twitter.com/kvadratab\" target=\"_blank\" title=\"Twitter\">\n" +
                "                        <i class=\"fa fa-twitter-square fa-lg\" style=\"color:white; padding-right: 10px;\"></i>\n" +
                "                    </a>\n" +
                "                    <a href=\"https://www.youtube.com/channel/UCipRFPJuuECJAq_wlbDxH5A\" target=\"_blank\" title=\"YouTube\">\n" +
                "                        <i class=\"fa fa-youtube-square fa-lg\" style=\"color:white; padding-right: 10px;\"></i>\n" +
                "                    </a>\n" +
                "                    <a href=\"https://www.facebook.com/KvadratSE/\" target=\"_blank\" title=\"Facebook\">\n" +
                "                        <i class=\"fa fa-facebook-square fa-lg\" style=\"color:white; padding-right: 10px;\"></i>\n" +
                "                    </a>\n" +
                "                    <a href=\"http://pratikvadrat.libsyn.com/\" target=\"_blank\" title=\"Podcast - Prat i Kvadrat\">\n" +
                "                        <i class=\"fa fa-microphone fa-lg\" style=\"color:white;\"></i>\n" +
                "                    </a>\n" +
                "                </div>\n" +
                "\t\t\t\t<a href=\"http://www.kvadrat.se/allakonsulter.html\">\n" +
                "\t\t\t\t<img id=\"allakonsulter\" src=\"http://www.kvadrat.se/wp-content/themes/blocks/img/konsulter.gif\" /> </a>\n" +
                "  </div>\n" +
                "</footer>\n" +
                "\n" +
                "<!-- Performance optimized by W3 Total Cache. Learn more: http://www.w3-edge.com/wordpress-plugins/\n" +
                "\n" +
                "Page Caching using disk: enhanced\n" +
                "\n" +
                " Served from: www.kvadrat.se @ 2016-09-08 22:36:57 by W3 Total Cache -->";

        SummaryPageData pageData = SummaryPageParser.parse(testData);

        Assert.assertNotNull(pageData);
        Assert.assertEquals(6, pageData.OfficeDatas.length);
        Assert.assertEquals(8, pageData.TagDatas.length);

    }
}
