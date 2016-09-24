package se.danielkonsult.www.kvadratab.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
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
import se.danielkonsult.www.kvadratab.helpers.Dialogs;
import se.danielkonsult.www.kvadratab.helpers.scraper.ImageHelper;
import se.danielkonsult.www.kvadratab.services.data.DataServiceListener;

public class MainActivity extends AppCompatActivity implements DataServiceListener {

    // Private variables

    private static final int IMAGE_UPDATE_INTERVAL = 1500;
    private static final long IMAGE_FADE_DURATION = 100;
    private final Handler _handler = new Handler();

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

        // Start the data service with a short delay
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppCtrl.getDataService().start();
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppCtrl.getDataService().registerListener(this);
    }

    @Override
    protected void onPause() {
        AppCtrl.getDataService().unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onInitialLoadStarted() {
        _isDoingInitialLoading = true;
        // Fade in the "Loading..." text
        _tvLoading.setText(R.string.loading_logo_text);
    }

    @Override
    public void onInitialLoadProgress(int progressCount, int totalCount) {
        _progbarMain.setProgress((int)((progressCount * 100.0) / totalCount));
    }

    @Override
    public void onConsultantAdded(ConsultantData consultant, Bitmap bitmap) {
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

    @Override
    public void onConsultantsUpdated() {
        // The initial downloading is complete (or it was just a refresh)
        _isDoingInitialLoading = false;

        // Go on to the consultant list activity, removing this activity from the back stack
        Intent intent = new Intent(this, ConsultantListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onError(String tag, String errorMessage) {
        // Notify the user
        if (_isDoingInitialLoading){
            _isDoingInitialLoading = false;

            AppCtrl.dropDatabase();
            ImageHelper.deleteAllConsultantImages();

            String message = String.format(getString(R.string.msg_initialload_error_template), errorMessage);

            Dialogs.displayError(this, getString(R.string.error_header), message, getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Finish the activity
                    MainActivity.this.finish();
                }
            });
        }
    }
}
