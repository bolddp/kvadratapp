package se.danielkonsult.www.kvadratab.services.data;

import android.app.Activity;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;

/**
 * Manages a list of DataServiceListeners, also making sure that
 * all notifications are run on the UI thread.
 */
public class DataServiceListeners implements DataServiceListener {

    // Private variables

    private final List<DataServiceListener> _listeners = new ArrayList<>();
    private final Handler handler = new Handler();

    // Private methods

    private void runOnUiThread(DataServiceListener listener, Runnable runnable) {
        // Is the listener an Activity?
        if (listener instanceof Activity)
            ((Activity)listener).runOnUiThread(runnable);
        else
            handler.post(runnable);
    }

    // Public methods

    public void registerListener(DataServiceListener listener) {
        synchronized (_listeners) {
            if (!_listeners.contains(listener))
                _listeners.add(listener);
        }
    }

    public void unregisterListener(DataServiceListener listener) {
        synchronized (_listeners){
            if (_listeners.contains(listener))
                _listeners.remove(listener);
        }
    }

    @Override
    public void onInitialLoadStarted() {
        synchronized (_listeners){
            for (final DataServiceListener listener : _listeners)
                runOnUiThread(listener, new Runnable() {
                    @Override
                    public void run() {
                        listener.onInitialLoadStarted();
                    }
                });
        }
    }

    @Override
    public void onInitialLoadProgress(int progressCount, int totalCount) {

    }

    @Override
    public void onConsultantAdded(ConsultantData consultant) {

    }

    @Override
    public void onLoaded() {

    }
}
