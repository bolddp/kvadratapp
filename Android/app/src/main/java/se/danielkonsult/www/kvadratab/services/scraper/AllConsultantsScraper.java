package se.danielkonsult.www.kvadratab.services.scraper;

import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.Utils;

/**
 * Performs a scraping of the Kvadrat web page so a list of all available consultants,
 * regardless of tag or office, can be compiled.
 */
public class AllConsultantsScraper extends AsyncTask<Void, Integer, ConsultantData[]> {

    // Private variables

    private final String MAIN_PAGE_URL = "http://www.kvadrat.se/wp-content/themes/blocks/ext/consultdata_new.php";
    private static final int STD_TIMEOUT = 20000;
    private static final String USER_AGENT = "KvadratApp/1.0";
    private static final String ACCEPT = "text/html";
    private final ConsultantDataListener _listener;

    private int statusCode;
    private String errorMessage;

    // Private methods

    @Override
    protected ConsultantData[] doInBackground(Void... params) {
        HttpURLConnection httpCon = null;
        InputStream is = null;
        try {
            URL url = new URL(MAIN_PAGE_URL);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(STD_TIMEOUT);
            httpCon.setRequestProperty("User-Agent", USER_AGENT);
            httpCon.setRequestProperty("Accept", ACCEPT);

            statusCode = httpCon.getResponseCode();

            is = httpCon.getInputStream();

            // Try to read the contents of the URL as a string
            String urlContents = Utils.getStringFromStream(is);
            if (urlContents == null)
                return null;

            return ConsultantDataParser.parse(urlContents);
        }
        catch (Throwable e) {
            errorMessage = e.getMessage();
            return  null;
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
    protected void onPostExecute(ConsultantData[] consultantDatas) {
        if (consultantDatas == null)
            _listener.onError(statusCode, errorMessage);
        else
            _listener.onResult(consultantDatas);
    }

    // Constructor

    public AllConsultantsScraper(ConsultantDataListener listener){
        _listener = listener;
    }
}
