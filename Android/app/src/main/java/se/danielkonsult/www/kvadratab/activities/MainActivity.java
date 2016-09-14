package se.danielkonsult.www.kvadratab.activities;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.services.data.DataServiceListener;

public class MainActivity extends AppCompatActivity implements DataServiceListener {

    // Private variables

    private final Handler _handler = new Handler();

    private RelativeLayout _layoutConsultantImage;
    private ImageView _imgConsultant;
    private TextView _tvLoading;
    private ProgressBar _progbarMain;

    // Private methods

    private void fadeInLoadingViews() {
        _layoutConsultantImage.setVisibility(View.VISIBLE);
        _tvLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _layoutConsultantImage = (RelativeLayout) findViewById(R.id.layoutConsultantImage);
        _imgConsultant = (ImageView) findViewById(R.id.imgConsultant);
        _tvLoading = (TextView) findViewById(R.id.tvLoading);

        _progbarMain = (ProgressBar) findViewById(R.id.progbarMain);
        _progbarMain.setProgress(0);

        this.deleteDatabase(KvadratDb.DATABASE_NAME);

        AppCtrl.setApplicationContext(getApplicationContext());

        AppCtrl.getDataService().registerListener(this);

        // Start the data service with a short delay
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppCtrl.getDataService().start();
            }
        }, 3000);

    }

    @Override
    public void onInitialLoadStarted() {
        fadeInLoadingViews();
    }

    @Override
    public void onInitialLoadProgress(int progressCount, int totalCount) {
        _progbarMain.setProgress((int)((progressCount * 100.0) / totalCount));
    }

    @Override
    public void onConsultantAdded(ConsultantData consultant, Bitmap bitmap) {
        _imgConsultant.setImageBitmap(bitmap);
    }

    @Override
    public void onLoaded() {

    }

    @Override
    public void onError(String tag, String errorMessage) {

    }
}
