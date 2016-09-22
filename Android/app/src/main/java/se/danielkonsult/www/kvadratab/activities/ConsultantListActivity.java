package se.danielkonsult.www.kvadratab.activities;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.adapters.ConsultantListAdapter;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.fragments.ConsultantFilterFragment;

public class ConsultantListActivity extends AppCompatActivity implements ConsultantFilterFragment.Listener {

    // Private variables

    private static final String TAG_FILTER = "CONSULTANT_FILTER";
    private static final long FRAGMENT_FILTER_FADE_DURATION = 200;

    private final Handler handler = new Handler();

    private ListView _lvMain;
    private FloatingActionButton _fabFilter;
    private ConsultantFilterFragment _fragmentConsultantFilter;

    // Private methods

    private void setupConsultants() {
        ConsultantData[] consultantDatas = AppCtrl.getDataService().getFilteredConsultants();
        _lvMain.setAdapter(new ConsultantListAdapter(ConsultantListActivity.this, consultantDatas));
    }

    /**
     * Hides or displays the consultant filter.
     */
    private void toggleFilterView() {
        FragmentManager fm = getSupportFragmentManager();
        if (_fragmentConsultantFilter == null){

            _fabFilter.setVisibility(View.GONE);

            // Display the fragment
            _fragmentConsultantFilter = new ConsultantFilterFragment();
            _fragmentConsultantFilter.setListener(this);

            fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.layoutFragmentContainer, _fragmentConsultantFilter, TAG_FILTER)
                    .addToBackStack("filter")
                    .commit();
        }
        else {
            // Filter has closed
            _fragmentConsultantFilter = null;
            _fabFilter.setVisibility(View.VISIBLE);
        }
    }

    // Public methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_list);

        _lvMain = (ListView) findViewById(R.id.lvMain);
        _fabFilter = (FloatingActionButton) findViewById(R.id.fabFilter);
        _fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilterView();
            }
        });

        // Initiate the loading of the consultants
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setupConsultants();
            }
        }, 100);
    }


    // Methods (ConsultantFilterFragment.Listener)

    @Override
    public void onClose() {
        // Indicate that there is no longer any fragment to display
        toggleFilterView();
    }
}
