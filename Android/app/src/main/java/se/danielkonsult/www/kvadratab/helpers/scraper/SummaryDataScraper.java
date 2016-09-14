package se.danielkonsult.www.kvadratab.helpers.scraper;

import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import se.danielkonsult.www.kvadratab.helpers.Utils;

/**
 * Requests info about consultants that belong to a specific office or tag.
 */
public class SummaryDataScraper extends AsyncTask<Void, Integer, SummaryData> {

    private final String MAIN_PAGE_URL = "http://www.kvadrat.se/konsulter/konsulter/";
    private static final int STD_TIMEOUT = 20000;
    private static final String USER_AGENT = "KvadratApp/1.0";
    private static final String ACCEPT = "text/html";
    private final SummaryDataListener _listener;

    private int statusCode;
    private String errorMessage;

    @Override
    protected SummaryData doInBackground(Void... params) {
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

            return SummaryDataParser.parse(urlContents);
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
    protected void onPostExecute(SummaryData summaryPageData) {
        if (summaryPageData == null)
            _listener.onError(statusCode, errorMessage);
        else
            _listener.onResult(summaryPageData);
    }

    public SummaryDataScraper(SummaryDataListener _listener) {
        this._listener = _listener;
    }
}
