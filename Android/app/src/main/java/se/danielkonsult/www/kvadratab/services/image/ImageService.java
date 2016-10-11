package se.danielkonsult.www.kvadratab.services.image;

import android.graphics.Bitmap;

import java.io.IOException;

/**
 * Created by Daniel on 2016-09-24.
 */
public interface ImageService {
    Bitmap downloadConsultantBitmap(int id) throws IOException;

    void saveConsultantBitmapToFile(int id, Bitmap bitmap) throws IOException;

    Bitmap getConsultantBitmapFromFile(int id);

    /*
        Deletes all consultant images that can be found in the application directory,
        based on the file's prefix.
         */
    void deleteAllConsultantImages();
}
