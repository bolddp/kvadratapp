package se.danielkonsult.www.kvadratab.activities;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.helpers.Dialogs;

/**
 * Created by Daniel on 2016-10-08.
 */
public class BaseActivity extends AppCompatActivity {

    // Private variables

    private RelativeLayout _layoutHourglass;

    private Thread _backgroundThread;

    private Handler _hourGlassHandler;
    private boolean _isShowingHourglass;

    // Protected methods

    protected void runInBackground(Runnable runnable) {
        _backgroundThread = new Thread(runnable);
        _backgroundThread.start();
    }

    protected void displayError(String message, DialogInterface.OnClickListener onClickListener) {
        Dialogs.displayError(this, getString(R.string.error_header), message, getString(R.string.btn_ok), onClickListener);
    }

    // Public methods

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        // Inject the hourglass layout into the root view
        ViewGroup rootView = (ViewGroup) ((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);

        _layoutHourglass = (RelativeLayout) getLayoutInflater().inflate(R.layout.hourglass, null);
        _layoutHourglass.setVisibility(View.GONE);
        rootView.addView(_layoutHourglass);

        if (rootView instanceof FrameLayout){
            _layoutHourglass.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
        else if (rootView instanceof DrawerLayout){
            _layoutHourglass.setLayoutParams(new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT));
        }

        _hourGlassHandler = new Handler(Looper.getMainLooper());
        _isShowingHourglass = false;
    }

    /*
     * Displays the hourglass with a specific message.
     */
    public void showHourglass() {
        if (!_isShowingHourglass) {
            _isShowingHourglass = true;
            // Show hourglass with a delay
            _hourGlassHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Hasn't the hourglass been hidden yet?
                    if (_isShowingHourglass)
                        _layoutHourglass.setVisibility(View.VISIBLE);
                }
            }, 500);
        }
    }

    public void hideHourglass() {
        boolean isShowingHourglass = _isShowingHourglass;
        _isShowingHourglass = false;
        if (isShowingHourglass) {
            _hourGlassHandler.post(new Runnable() {
                @Override
                public void run() {
                    _layoutHourglass.setVisibility(View.GONE);
                }
            });
        }
    }
}
