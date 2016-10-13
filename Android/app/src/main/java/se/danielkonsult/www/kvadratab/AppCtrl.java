package se.danielkonsult.www.kvadratab;

import android.content.Context;

import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.helpers.scraper.DefaultWebPageScraper;
import se.danielkonsult.www.kvadratab.helpers.scraper.WebPageScraper;
import se.danielkonsult.www.kvadratab.repositories.notification.DefaultNotificationRepository;
import se.danielkonsult.www.kvadratab.services.data.DataService;
import se.danielkonsult.www.kvadratab.services.data.DefaultDataService;
import se.danielkonsult.www.kvadratab.services.initialloader.DefaultLoaderService;
import se.danielkonsult.www.kvadratab.services.initialloader.LoaderService;
import se.danielkonsult.www.kvadratab.services.notification.DefaultNotificationService;
import se.danielkonsult.www.kvadratab.services.notification.NotificationService;
import se.danielkonsult.www.kvadratab.services.refresher.DefaultRefresherService;
import se.danielkonsult.www.kvadratab.services.image.DefaultImageService;
import se.danielkonsult.www.kvadratab.services.image.ImageService;
import se.danielkonsult.www.kvadratab.services.prefs.DefaultPrefsService;
import se.danielkonsult.www.kvadratab.services.prefs.PrefsService;
import se.danielkonsult.www.kvadratab.services.refresher.RefresherService;

/**
 * Application-wide singleton with a responsibility to provide services
 * and data to the rest of the application.
 */
public class AppCtrl {

    private static Context _applicationContext;
    private static LoaderService _initialLoader;
    private static KvadratDb _db;
    private static DataService _dataService;
    private static PrefsService _prefsService;
    private static ImageService _imageService;
    private static RefresherService _refresherService;
    private static WebPageScraper _webPageScraper;
    private static NotificationService _notificationService;
    private static boolean _testFlag;

    /**
     * Returns the application context that has been set.
     */
    public static Context getApplicationContext() {
        return _applicationContext;
    }

    public static void setApplicationContext(Context context){
        _applicationContext = context;
    }

    public static KvadratDb getDb() {
        if (_db == null)
            _db = new KvadratDb();
        return _db;
    }

    /* Drops the database and forces a new copy to be created the next
    time it is requested.
     */
    public static void dropDatabase() {
        getApplicationContext().deleteDatabase(KvadratDb.DATABASE_NAME);
        _db = new KvadratDb();
    }

    public static DataService getDataService() {
        if (_dataService == null)
            _dataService = new DefaultDataService();
        return _dataService;
    }

    public static PrefsService getPrefsService() {
        if (_prefsService == null)
            _prefsService = new DefaultPrefsService();

        return _prefsService;
    }

    public static ImageService getImageService() {
        if (_imageService == null)
            _imageService = new DefaultImageService();

        return _imageService;
    }

    public static LoaderService getLoaderService() {
        if (_initialLoader == null)
            _initialLoader = new DefaultLoaderService();

        return _initialLoader;
    }

    public static RefresherService getRefresherService() {
        if (_refresherService == null)
            _refresherService = new DefaultRefresherService();

        return _refresherService;
    }

    public static NotificationService getNotificationService() {
        if (_notificationService == null)
            _notificationService = new DefaultNotificationService();

        return _notificationService;
    }

    public static WebPageScraper getWebPageScraper() {
        if (_webPageScraper == null)
            _webPageScraper = new DefaultWebPageScraper();

        return _webPageScraper;
    }

    // Test methods

    /**
     * Sets the db instance to use, instead of lazy creating it. Used for tests.
     */
    public static void setTestDb(KvadratDb db) {
        _db = db;
    }

    public static void setTestRefresherService(RefresherService refresherService) {
        _refresherService = refresherService;
    }

    public static void setTestWebPageScraper(WebPageScraper webPageScraper) {
        _webPageScraper = webPageScraper;
    }

    public static void setTestNotificationService(NotificationService notificationService) {
        _notificationService = notificationService;
    }

    public static void setTestImageService(ImageService imageService) {
        _imageService = imageService;
    }

    public static void setPrefsService(PrefsService prefsService) {
        _prefsService = prefsService;
    }
}
