package se.danielkonsult.www.kvadratab.activities;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.Constants;
import se.danielkonsult.www.kvadratab.helpers.Utils;
import se.danielkonsult.www.kvadratab.services.data.ConsultantDataListener;

public class ConsultantDetailsActivity extends BaseActivity {

    // Private variables

    private ScrollView _layoutMain;
    private ImageView _imgConsultant;
    private LinearLayout _layoutCompetences;
    private TextView _tvNameAndOffice;
    private LinearLayout _layoutDescription;
    private TextView _tvDescription;
    private LinearLayout _layoutOverview;
    private TextView _tvOverview;

    private int _consultantId;

    // Private methods

    private void setupView() {
        setContentView(R.layout.activity_consultant_details);

        _layoutMain = (ScrollView) findViewById(R.id.layoutMain);
        _layoutMain.setVisibility(View.GONE);

        _layoutCompetences = (LinearLayout) findViewById(R.id.layoutCompetences);
        _imgConsultant = (ImageView) findViewById(R.id.imgConsultant);
        _tvNameAndOffice = (TextView) findViewById(R.id.tvNameAndOffice);
        _layoutDescription = (LinearLayout) findViewById(R.id.layoutDescription);
        _tvDescription = (TextView) findViewById(R.id.tvDescription);
        _layoutOverview = (LinearLayout) findViewById(R.id.layoutOverview);
        _tvOverview = (TextView) findViewById(R.id.tvOverview);
    }

    private void loadConsultantDetails() {
        runInBackground(new Runnable() {
            @Override
            public void run() {
                showHourglass();
                AppCtrl.getDataService().getConsultantDetails(_consultantId, new ConsultantDataListener() {
                    @Override
                    public void onResult(final ConsultantData consultantData) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupConsultantData(consultantData);
                            }
                        });
                        hideHourglass();
                    }

                    @Override
                    public void onError(final String errorMessage) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideHourglass();
                                displayError(errorMessage, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Display the error and then close the activity
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();

        _consultantId = getIntent().getExtras().getInt(Constants.EXTRA_CONSULTANT_ID);
    }

    private void setupConsultantData(ConsultantData consultantData) {
        // Load the image as well and make sure it's square
        _imgConsultant.setImageBitmap(AppCtrl.getImageService().getConsultantBitmapFromFile(consultantData.Id));
        _tvNameAndOffice.setText(String.format("%s %s, %s", consultantData.FirstName, consultantData.LastName, consultantData.Office.Name));

        LayoutInflater inflater = getLayoutInflater();
        for (String competence : consultantData.CompetenceAreas){
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.competence_item, null);
            ((TextView)layout.findViewById(R.id.tvCompetenceText)).setText(competence);
            _layoutCompetences.addView(layout);
        }

        _layoutDescription.setVisibility(Utils.isStringNullOrEmpty(consultantData.Description) ? View.GONE : View.VISIBLE);
        _tvDescription.setText(consultantData.Description);
        _layoutOverview.setVisibility(Utils.isStringNullOrEmpty(consultantData.Overview) ? View.GONE : View.VISIBLE);
        _tvOverview.setText(consultantData.Overview);

        _layoutMain.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadConsultantDetails();
    }
}
