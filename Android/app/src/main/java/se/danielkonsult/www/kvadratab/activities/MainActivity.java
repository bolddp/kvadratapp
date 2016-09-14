package se.danielkonsult.www.kvadratab.activities;

import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.services.data.DataServiceListener;

public class MainActivity extends AppCompatActivity implements DataServiceListener {

    // Private variables

    private TextView _tvMain;

    // Private methods

    private void addText(String text){
        String existingText = _tvMain.getText().toString();
        _tvMain.setText(existingText + text + "\r\n");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _tvMain = (TextView) findViewById(R.id.tvMain);
        _tvMain.setMovementMethod(new ScrollingMovementMethod());

        // this.deleteDatabase(KvadratDb.DATABASE_NAME);

        AppCtrl.setApplicationContext(getApplicationContext());

        AppCtrl.getDataService().registerListener(this);
        AppCtrl.getDataService().start();
    }

    @Override
    public void onInitialLoadStarted() {
        addText("Initial load!");
    }

    @Override
    public void onInitialLoadProgress(int progressCount, int totalCount) {
    }

    @Override
    public void onConsultantAdded(ConsultantData consultant, Bitmap bitmap) {
        addText(String.format("Added consultant %s (id %d)", consultant.Name, consultant.Id));
    }

    @Override
    public void onLoaded() {

    }

    @Override
    public void onError(String tag, String errorMessage) {

    }
}
