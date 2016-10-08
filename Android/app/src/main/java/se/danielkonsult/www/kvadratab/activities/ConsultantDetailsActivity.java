package se.danielkonsult.www.kvadratab.activities;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.Constants;
import se.danielkonsult.www.kvadratab.services.data.ConsultantDataListener;

public class ConsultantDetailsActivity extends BaseActivity {

    // Private variables

    private ScrollView _layoutMain;
    private ImageView _imgConsultant;
    private TextView _tvNameAndOffice;
    private ListView _lvCompetences;
    private TextView _tvDescription;
    private TextView _tvOverview;

    private int _consultantId;

    // Private methods

    private void setupView() {
        setContentView(R.layout.activity_consultant_details);

        _layoutMain = (ScrollView) findViewById(R.id.layoutMain);
        _layoutMain.setVisibility(View.GONE);

        _imgConsultant = (ImageView) findViewById(R.id.imgConsultant);
        _tvNameAndOffice = (TextView) findViewById(R.id.tvNameAndOffice);
        _lvCompetences = (ListView) findViewById(R.id.lvCompetences);
        _tvDescription = (TextView) findViewById(R.id.tvDescription);
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
        _tvDescription.setText(consultantData.Description);
        _tvOverview.setText(consultantData.Overview);

        _layoutMain.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadConsultantDetails();
    }
}
