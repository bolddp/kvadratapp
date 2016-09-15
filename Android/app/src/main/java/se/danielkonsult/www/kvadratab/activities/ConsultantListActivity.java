package se.danielkonsult.www.kvadratab.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.adapters.ConsultantListAdapter;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;

public class ConsultantListActivity extends AppCompatActivity {

    // Private variables

    private final Handler handler = new Handler();

    private ListView _lvMain;

    // Public methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_list);

        _lvMain = (ListView) findViewById(R.id.lvMain);

        // Initiate the loading of the consultants
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setupConsultants();
            }
        }, 1000);
    }

    private void setupConsultants() {
        ConsultantData[] consultantDatas = AppCtrl.getDataService().getConsultants();
        _lvMain.setAdapter(new ConsultantListAdapter(ConsultantListActivity.this, consultantDatas));

    }
}
