package se.danielkonsult.www.kvadratab.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.helpers.Constants;

public class ConsultantDetailsActivity extends AppCompatActivity {

    // Private variables
    
    private ImageView _imgConsultant;
    private ListView _lvCompetences;
    private TextView _tvDescription;
    private TextView _tvOverview;

    private String _consultantId;

    // Private methods

    private void setupView() {
        setContentView(R.layout.activity_consultant_details);
        _imgConsultant = (ImageView) findViewById(R.id.imgConsultant);
        _lvCompetences = (ListView) findViewById(R.id.lvCompetences);
        _tvDescription = (TextView) findViewById(R.id.tvDescription);
        _tvOverview = (TextView) findViewById(R.id.tvOverview);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _consultantId = getIntent().getExtras().getString(Constants.EXTRA_CONSULTANT_ID);

        setupView();
    }
}
