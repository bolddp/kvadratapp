package se.danielkonsult.www.kvadratab.services.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import se.danielkonsult.www.kvadratab.AppCtrl;

/**
 * Downloads images from the Kvadrat home page.
 */
public class DefaultImageService implements ImageService {

    private static final String CONSULTANT_IMAGE_URL_TEMPLATE = "http://www.kvadrat.se/wp-content/themes/blocks/consultant-image.php?id=%d";
    private static final String CONSULTANT_FILENAME_PREFIX = "img_consultant_";
    private static final int BITMAP_TARGET_SIZE = 600;

    private static final int STD_TIMEOUT = 10000;
    private static final String USER_AGENT = "KvadratApp/1.0";
    private static final String ACCEPT = "image/*";

    /**
     * Gets the filename that should be used for a particular consultant id.
     */
    private String getFileNameFromId(int id) {
        return CONSULTANT_FILENAME_PREFIX + Integer.toString(id);
    }

    /**
     * Converts an input stream to a byte array.
     */
    private byte[] getByteArrayFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();

        return buffer.toByteArray();
    }

    /**
     * Saves a byte array to file in the application's private directory
     */
    private void saveBytesToFile(byte[] bytes, String fileName) throws IOException {
        File file = new File(AppCtrl.getApplicationContext().getFilesDir(), fileName);
        OutputStream output = new FileOutputStream(file);
        try {
            try {
                output.write(bytes);
                output.flush();
            } finally {
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes a Bitmap and scales it down if it's too large.
     */
    private Bitmap getScaledDownBitmap(Bitmap bitmap, int targetSize) {
        if (bitmap.getWidth() <= targetSize)
            return bitmap;
        else {
            return Bitmap.createScaledBitmap(bitmap, targetSize, targetSize, false);
        }
    }

    /***
     * Downloads a consultant image by its id and resizes it if necessary.
     */
    @Override
    public Bitmap downloadConsultantBitmap(int id) throws IOException {
        HttpURLConnection httpCon = null;
        InputStream is = null;
        try {
            URL url = new URL(String.format(CONSULTANT_IMAGE_URL_TEMPLATE, id));
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(STD_TIMEOUT);
            httpCon.setRequestProperty("User-Agent", USER_AGENT);
            httpCon.setRequestProperty("Accept", ACCEPT);

            is = httpCon.getInputStream();

            // Get the bytes and convert it to a bitmap
            byte[] bytes = getByteArrayFromStream(is);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

            return getScaledDownBitmap(bitmap, BITMAP_TARGET_SIZE);
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

    @Override
    public void saveConsultantBitmapToFile(int id, Bitmap bitmap) throws IOException {
        // Convert it to JPG
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
        byte[] bytes = stream.toByteArray();

        saveBytesToFile(bytes, getFileNameFromId(id));
    }

    @Override
    public Bitmap getConsultantBitmapFromFile(int id){
        File imgFile = new File(AppCtrl.getApplicationContext().getFilesDir(), getFileNameFromId(id));
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);

        return bitmap;
    }

    /*
    Deletes all consultant images that can be found in the application directory,
    based on the file's prefix.
     */
    @Override
    public void deleteAllConsultantImages() {
        // Delete the image file if there is any
        FilenameFilter imageFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.startsWith(CONSULTANT_FILENAME_PREFIX)) {
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
    }
}
