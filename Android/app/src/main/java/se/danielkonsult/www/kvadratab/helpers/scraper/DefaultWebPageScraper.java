package se.danielkonsult.www.kvadratab.helpers.scraper;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.ConsultantDetails;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.helpers.Constants;
import se.danielkonsult.www.kvadratab.helpers.Utils;
import se.danielkonsult.www.kvadratab.entities.SummaryData;

/**
 * Performs scraping operations of the Kvadrat AB web page.
 */
public class DefaultWebPageScraper implements WebPageScraper {

    // Private variables

    private static final String SCRAPE_ALLCONSULTANTS_URL = "http://www.kvadrat.se/wp-content/themes/blocks/ext/consultdata_new.php";
    private static final String SCRAPE_SUMMARYDATA_URL = "http://www.kvadrat.se/konsulter/konsulter/";
    private static final String SCRAPE_CONSULTANTDETAILS_URL = "http://www.kvadrat.se/profil/?id=%d";
    private static final String SCRAPE_ADMINISTRATION_OFFICES = "http://www.kvadrat.se/kontakt/";

    private static final int STD_TIMEOUT = 10000;
    private static final String USER_AGENT = "KvadratApp/1.0";
    private static final String ACCEPT = "text/html";

    // Private methods

    /**
     * Adds an office representing the administration, with a fixed id and name.
     */
    private void appendAdministrationOffice(SummaryData summaryData) {
        List<OfficeData> offices = new ArrayList<OfficeData>(Arrays.asList(summaryData.OfficeDatas));
        offices.add(new OfficeData(Constants.ADMINISTRATION_OFFICE_ID, Constants.ADMINISTRATION_OFFICE_NAME));

        summaryData.OfficeDatas = offices.toArray(new OfficeData[offices.size()]);
    }

    private List<ConsultantData> scrapeAdministrationDetails(String adminUrl) throws IOException {
        HttpURLConnection httpCon = null;
        InputStream is = null;
        try {
            URL url = new URL(adminUrl);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(STD_TIMEOUT);
            httpCon.setRequestProperty("User-Agent", USER_AGENT);
            httpCon.setRequestProperty("Accept", ACCEPT);

            is = httpCon.getInputStream();

            // Try to read the contents of the URL as a string
            String urlContents = Utils.getStringFromStream(is);
            if (urlContents == null)
                return null;

            return Arrays.asList(AdministrationParser.parseOfficeDetails(urlContents));
        }
        finally {
            try {
                if (httpCon != null)
                    httpCon.disconnect();
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Public methods

    @Override
    public ConsultantData[] scrapeConsultants(int officeId, int tagId) throws IOException {
        HttpURLConnection httpCon = null;
        InputStream is = null;
        try {
            URL url = new URL(SCRAPE_ALLCONSULTANTS_URL);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(STD_TIMEOUT);
            httpCon.setRequestProperty("User-Agent", USER_AGENT);
            httpCon.setRequestProperty("Accept", ACCEPT);
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setRequestMethod("POST");

            // Set body
            String str = String.format("consultantsearch=&offset=1&count=6&offices=%d&tags=%d", officeId, tagId);
            byte[] outputBytes = str.getBytes("UTF-8");
            BufferedOutputStream outStream = null;
            try {
                outStream = new BufferedOutputStream(httpCon.getOutputStream());
                outStream.write(outputBytes);
                outStream.flush();
            } finally {
                if (outStream != null)
                    outStream.close();
            }
            is = httpCon.getInputStream();

            // Try to read the contents of the URL as a string
            String urlContents = Utils.getStringFromStream(is);
            if (urlContents == null)
                return null;

            return ConsultantDataParser.parse(urlContents);
        }
        finally {
            try {
                if (httpCon != null)
                    httpCon.disconnect();
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public SummaryData scrapeSummaryData() throws IOException {
        HttpURLConnection httpCon = null;
        InputStream is = null;
        try {
            URL url = new URL(SCRAPE_SUMMARYDATA_URL);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(STD_TIMEOUT);
            httpCon.setRequestProperty("User-Agent", USER_AGENT);
            httpCon.setRequestProperty("Accept", ACCEPT);

            is = httpCon.getInputStream();

            // Try to read the contents of the URL as a string
            String urlContents = Utils.getStringFromStream(is);
            if (urlContents == null)
                return null;

            SummaryData result = SummaryDataParser.parse(urlContents);
            appendAdministrationOffice(result);

            return result;
        }
        finally {
            try {
                if (httpCon != null)
                    httpCon.disconnect();
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ConsultantDetails scrapeConsultantDetails(int consultantId) throws IOException {
        HttpURLConnection httpCon = null;
        InputStream is = null;
        try {
            URL url = new URL(String.format(SCRAPE_CONSULTANTDETAILS_URL, consultantId));
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(STD_TIMEOUT);
            httpCon.setRequestProperty("User-Agent", USER_AGENT);
            httpCon.setRequestProperty("Accept", ACCEPT);

            is = httpCon.getInputStream();

            // Try to read the contents of the URL as a string
            String urlContents = Utils.getStringFromStream(is);
            if (urlContents == null)
                return null;

            return ConsultantDetailsParser.parse(urlContents);
        }
        finally {
            try {
                if (httpCon != null)
                    httpCon.disconnect();
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ConsultantData[] scrapeAdministration() throws IOException {
        HttpURLConnection httpCon = null;
        InputStream is = null;
        try {
            URL url = new URL(SCRAPE_ADMINISTRATION_OFFICES);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(STD_TIMEOUT);
            httpCon.setRequestProperty("User-Agent", USER_AGENT);
            httpCon.setRequestProperty("Accept", ACCEPT);

            is = httpCon.getInputStream();

            // Try to read the contents of the URL as a string
            String urlContents = Utils.getStringFromStream(is);
            if (urlContents == null)
                return null;

            List<ConsultantData> result = new ArrayList<>();
            String[] administrationOffices = AdministrationParser.parseOfficeUrls(urlContents);
            for (String adminUrl : administrationOffices){
                result.addAll(scrapeAdministrationDetails(adminUrl));
            }

            return result.toArray(new ConsultantData[result.size()]);
        }
        finally {
            try {
                if (httpCon != null)
                    httpCon.disconnect();
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
