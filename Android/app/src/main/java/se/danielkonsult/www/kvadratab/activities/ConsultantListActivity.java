package se.danielkonsult.www.kvadratab.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.adapters.ConsultantListAdapter;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.fragments.ConsultantFilterFragment;
import se.danielkonsult.www.kvadratab.helpers.Constants;
import se.danielkonsult.www.kvadratab.services.data.ConsultantFilter;
import se.danielkonsult.www.kvadratab.services.data.DataServiceListener;

public class ConsultantListActivity extends AppCompatActivity implements ConsultantFilterFragment.Listener, DataServiceListener {

    // Private variables

    private static final String TAG_FILTER = "CONSULTANT_FILTER";
    private static final long FRAGMENT_FILTER_FADE_DURATION = 200;

    private final Handler handler = new Handler();

    private ListView _lvMain;
    private FloatingActionButton _fabFilter;
    private ConsultantFilterFragment _fragmentConsultantFilter;

    // Private methods

    /*
     Makes an attempt to apply the filter and returns true if it succeeded.
     Reasons it can fail: the filter excludes all consultants and the user
     responds that the filter shouldn't be applied.
     */
    private void applyAndCloseFilter() {
        // Only apply the filter if it has actually changed
        if (_fragmentConsultantFilter.getIsFilterDirty()) {
            ConsultantFilter newFilter = _fragmentConsultantFilter.getFilter();

            if (AppCtrl.getDataService().tryFilter(newFilter) > 0)
                AppCtrl.getDataService().useTriedFilter();
            else {
                Toast.makeText(this, R.string.msg_filter_no_hits, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Close the filter view
        toggleFilterView();
    }

    /**
     * Hides or displays the consultant filter.
     */
    private void toggleFilterView() {
        int currentVisibility = _fragmentConsultantFilter.getView().getVisibility();
        int targetVisibility = currentVisibility == View.GONE ? View.VISIBLE : View.GONE;

        _fragmentConsultantFilter.getView().setVisibility(targetVisibility);

        _fabFilter.setVisibility(currentVisibility);
    }

    /**
     * Opens the consultant details activity.
     */
    private void gotoConsultantDetails(int consultantId) {
        Intent intent = new Intent(this, ConsultantDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_CONSULTANT_ID, consultantId);

        startActivity(intent);
    }

    // Public methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_list);

        _lvMain = (ListView) findViewById(R.id.lvMain);
        _lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConsultantData consultant = (ConsultantData) parent.getItemAtPosition(position);
                gotoConsultantDetails(consultant.Id);
            }
        });

        _fabFilter = (FloatingActionButton) findViewById(R.id.fabFilter);
        _fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilterView();
            }
        });

        _fragmentConsultantFilter = (ConsultantFilterFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentFilter);
        _fragmentConsultantFilter.setListener(this);

        // Perform an initial update of the consultants list
        onFilteredConsultantsUpdated();

        // Inform of test mode at startup
        if (AppCtrl.getPrefsService().getTestMode()){
            Toast.makeText(this, "Test mode is ON", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register as data service listener
        AppCtrl.getDataService().setListener(this);
    }

    @Override
    protected void onPause() {
        AppCtrl.getDataService().setListener(null);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        // If the filter is visible, hide it
        if (_fragmentConsultantFilter.getView().getVisibility() != View.VISIBLE)
            super.onBackPressed();
        else {
            applyAndCloseFilter();
        }
    }

    // Methods (ConsultantFilterFragment.Listener)

    @Override
    public void onShouldClose() {
        // The consultant filter has signalled that it should be closed
        applyAndCloseFilter();
    }

    @Override
    public void onFilteredConsultantsUpdated() {
        _lvMain.setVisibility(View.INVISIBLE);
        try {
            ConsultantData[] consultantDatas = AppCtrl.getDataService().getFilteredConsultants();
            _lvMain.setAdapter(new ConsultantListAdapter(ConsultantListActivity.this, consultantDatas));
        } finally {
            _lvMain.setVisibility(View.VISIBLE);
        }
    }
}
