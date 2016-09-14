package se.danielkonsult.www.kvadratab.helpers.scraper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Downloads images from the Kvadrat home page.
 */
public class ImageDownloader {

    private static final String CONSULTANT_IMAGE_URL_TEMPLATE = "http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=%d";
    private static final int STD_TIMEOUT = 10000;
    private static final String USER_AGENT = "KvadratApp/1.0";
    private static final String ACCEPT = "image/*";

    /***
     * Downloads a consultant image by its id.
     */
    public Bitmap downloadConsultantImageById(int id) throws IOException {
        HttpURLConnection httpCon = null;
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            URL url = new URL(String.format(CONSULTANT_IMAGE_URL_TEMPLATE, id));
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(STD_TIMEOUT);
            httpCon.setRequestProperty("User-Agent", USER_AGENT);
            httpCon.setRequestProperty("Accept", ACCEPT);

            is = httpCon.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

            return bitmap;
        } finally {
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
