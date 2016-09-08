package se.danielkonsult.www.kvadratab.services.scraper;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.danielkonsult.www.kvadratab.entities.MainPageWebData;

/**
 * Performs a scraping of the Kvadrat web page where all consultants are listed.
 */
public class MainPageScraper extends AsyncTask<Void, Integer, MainPageWebData[]> {

    // Private variables

    private final String MAIN_PAGE_URL = "http://www.kvadrat.se/wp-content/themes/blocks/ext/consultdata_new.php";
    private static final int STD_TIMEOUT = 20000;
    private static final String USER_AGENT = "KvadratApp/1.0";
    private static final String ACCEPT = "text/html";
    private final MainPageScraperListener _listener;

    private int statusCode;
    private String errorMessage;

    // Private methods

    /**
     * Converts an input stream to a string, using UTF-8 encoding.
     * @param inputStream
     * @return
     */
    private String getStringFromStream(InputStream inputStream) {
        try{
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(inputStream, "UTF-8");
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            return out.toString();
        }
        catch (Throwable ex){
            errorMessage = ex.getMessage();
            return null;
        }
    }

    @Override
    protected MainPageWebData[] doInBackground(Void... params) {
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
            String urlContents = getStringFromStream(is);
            if (urlContents == null)
                return null;

            return MainPageParser.parse(urlContents);
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
    protected void onPostExecute(MainPageWebData[] mainPageWebDatas) {
        if (mainPageWebDatas == null)
            _listener.onError(statusCode, errorMessage);
        else
            _listener.onResult(mainPageWebDatas);
    }

    // Constructor

    public MainPageScraper(MainPageScraperListener listener){
        _listener = listener;
    }
}
