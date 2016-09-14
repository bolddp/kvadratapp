package se.danielkonsult.www.kvadratab.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.services.data.DataServiceListener;

public class MainActivity extends AppCompatActivity implements DataServiceListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.deleteDatabase(KvadratDb.DATABASE_NAME);

        AppCtrl.setApplicationContext(getApplicationContext());

        AppCtrl.getDataService().registerListener(this);
        AppCtrl.getDataService().start();
    }

    @Override
    public void onInitialLoadStarted() {
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
