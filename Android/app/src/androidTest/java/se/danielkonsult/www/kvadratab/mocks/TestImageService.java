package se.danielkonsult.www.kvadratab.mocks;

import android.graphics.Bitmap;

import java.io.IOException;

import se.danielkonsult.www.kvadratab.services.image.DefaultImageService;
import se.danielkonsult.www.kvadratab.services.image.ImageService;

/**
 * Test class that can simulate updated consultant images by mixing up
 * the consultant id's.
 */
public class TestImageService implements ImageService {

    private final ImageService _defaultImageService = new DefaultImageService();

    @Override
    public Bitmap downloadConsultantBitmap(int id) throws IOException {
        // When performing an image download, we swap one consultant's image to
        // simulate that it has been updated
        if (id == 6271)
            return _defaultImageService.downloadConsultantBitmap(7373);

        return _defaultImageService.downloadConsultantBitmap(id);
    }

    @Override
    public void saveConsultantBitmapToFile(int id, Bitmap bitmap) throws IOException {
        _defaultImageService.saveConsultantBitmapToFile(id, bitmap);
    }

    @Override
    public Bitmap getConsultantBitmapFromFile(int id) {
        // When reading "existing" images, the actual file is always returned
        return _defaultImageService.getConsultantBitmapFromFile(id);
    }

    @Override
    public void deleteAllConsultantImages() {

    }
}
