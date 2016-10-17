package se.danielkonsult.www.kvadratab;

import android.app.Application;
import android.util.Log;

/**
 * Created by Daniel on 2016-10-17.
 */
public class KvadratApplication extends Application {

    // Private variables

    private static final String TAG = "KvadratApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        AppCtrl.setApplicationContext(getApplicationContext());

        Log.d(TAG, "onCreate");
    }
}
