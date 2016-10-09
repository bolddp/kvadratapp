package se.danielkonsult.www.kvadratab.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.services.initialloader.LoaderService;
import se.danielkonsult.www.kvadratab.services.initialloader.LoaderServiceListener;

public class MainActivity extends BaseActivity implements LoaderServiceListener {

    // Private variables

    private static final int IMAGE_UPDATE_INTERVAL = 1500;
    private static final long IMAGE_FADE_DURATION = 100;

    private Handler _handler = new Handler();

    private RelativeLayout _layoutConsultantImage;
    private ImageView _imgConsultant;
    private TextView _tvLoading;
    private ProgressBar _progbarMain;
    private long pictureUpdateTimestamp = 0;
    private boolean _isDoingInitialLoading;

    // Private methods

    private void fadeInViews(final View[] views) {
        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(IMAGE_FADE_DURATION);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                for (View view: views){
                    view.setVisibility(View.VISIBLE);
                }
            }

            public void onAnimationRepeat(Animation animation) { }
            public void onAnimationStart(Animation animation) { }
        });

        // Start the animations
        for (View view: views){
            view.startAnimation(fadeIn);
        }
    }

    /**
     * Updates the consultant image that is displayed during
     * loading.
     */
    private void setConsultantImage(final Bitmap bitmap) {
        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(IMAGE_FADE_DURATION);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation)
            {
                // Update the bitmap and start the fade in
                _imgConsultant.setVisibility(View.VISIBLE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(IMAGE_FADE_DURATION);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                // Update the bitmap and start the fade in
                _imgConsultant.setImageBitmap(bitmap);
                _imgConsultant.setVisibility(View.INVISIBLE);
                _imgConsultant.startAnimation(fadeIn);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        // If there is an image since before, we need to both fade out and in,
        // otherwise we just need to fade in
        if (_imgConsultant.getDrawable() != null)
            _imgConsultant.startAnimation(fadeOut);
        else{
            _imgConsultant.setImageBitmap(bitmap);
            _imgConsultant.startAnimation(fadeIn);
        }
    }

    private void gotoConsultantListActivity() {
        Intent intent = new Intent(this, ConsultantListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make sure the phone doesn't go black during prolonged loading
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        _layoutConsultantImage = (RelativeLayout) findViewById(R.id.layoutConsultantImage);
        _imgConsultant = (ImageView) findViewById(R.id.imgConsultant);

        _tvLoading = (TextView) findViewById(R.id.tvLoading);
        _tvLoading.setText(R.string.logo_text);

        _progbarMain = (ProgressBar) findViewById(R.id.progbarMain);
        _progbarMain.setProgress(0);

        // this.deleteDatabase(KvadratDb.DATABASE_NAME);

        AppCtrl.setApplicationContext(getApplicationContext());

        // Start initial loading or goto consultant list activity
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Make sure the refresher background worker is running as well
                AppCtrl.getRefresherService().ensureStarted();

                // Do we need to perform an initial load?
                LoaderService loaderService = AppCtrl.getInitialLoader();
                if (loaderService.isInitialLoadNeeded())
                    loaderService.run(MainActivity.this);
                else
                    gotoConsultantListActivity();
            }
        }, 1500);
    }

    @Override
    public void onInitialLoadStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _isDoingInitialLoading = true;
                // Fade in the "Loading..." text
                _tvLoading.setText(R.string.loading_logo_text);
            }
        });
    }

    @Override
    public void onInitialLoadProgress(final int progressCount, final int totalCount) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _progbarMain.setProgress((int)((progressCount * 100.0) / totalCount));
            }
        });
    }

    @Override
    public void onConsultantAdded(ConsultantData consultant, final Bitmap bitmap) {
        // Run it on the UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Is the progress bar still hidden?
                if (_layoutConsultantImage.getVisibility() == View.INVISIBLE){
                    fadeInViews(new View[] { _layoutConsultantImage });
                }

                // Is it time to update the image?
                if ((SystemClock.uptimeMillis() - IMAGE_UPDATE_INTERVAL) > pictureUpdateTimestamp){
                    setConsultantImage(bitmap);
                    pictureUpdateTimestamp = SystemClock.uptimeMillis();
                }
            }
        });
    }

    @Override
    public void onInitialLoadingCompleted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // The initial downloading is complete (or it was just a refresh)
                _isDoingInitialLoading = false;
                // Go on to the consultant list activity, removing this activity from the back stack
                gotoConsultantListActivity();
            }
        });
    }

    @Override
    public void onError(String tag, final String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Notify the user
                if (_isDoingInitialLoading){
                    _isDoingInitialLoading = false;

                    AppCtrl.dropDatabase();
                    AppCtrl.getImageService().deleteAllConsultantImages();

                    String message = String.format(getString(R.string.msg_initialload_error_template), errorMessage);

                    displayError(message, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Finish the activity
                            MainActivity.this.finish();
                        }
                    });
                }

            }
        });
    }
}
