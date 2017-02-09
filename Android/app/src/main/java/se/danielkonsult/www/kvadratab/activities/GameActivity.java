package se.danielkonsult.www.kvadratab.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.services.data.ConsultantDataListener;

public class GameActivity extends BaseActivity {

    private ImageView _imgConsultant1;
    private ImageView _imgConsultant2;
    private ImageView _imgConsultant3;
    private ImageView _imgConsultant4;
    private TableLayout tableLayout;

    int score = 0;
    int highScore = 0;

    int randIndex;

    private TextView _tvVemArDet;
    private TextView _tvScore;
    private TextView _tvHighScore;

    private void setupView() {
        setContentView(R.layout.activity_game);
        _imgConsultant1 = (ImageView) findViewById(R.id.imgConsultant1);
        _imgConsultant1.setOnClickListener(createClickListener(_imgConsultant1, 0));
        _imgConsultant2 = (ImageView) findViewById(R.id.imgConsultant2);
        _imgConsultant2.setOnClickListener(createClickListener(_imgConsultant2, 1));
        _imgConsultant3 = (ImageView) findViewById(R.id.imgConsultant3);
        _imgConsultant3.setOnClickListener(createClickListener(_imgConsultant3, 2));
        _imgConsultant4 = (ImageView) findViewById(R.id.imgConsultant4);
        _imgConsultant4.setOnClickListener(createClickListener(_imgConsultant4, 3));
        _tvVemArDet = (TextView) findViewById(R.id.vemArDet);
        _tvScore = (TextView) findViewById(R.id.score);
        _tvHighScore = (TextView) findViewById(R.id.high_score);

        randIndex = (int) (Math.random() * 4);


    }

    @NonNull
    private View.OnClickListener createClickListener(final ImageView imageView, final int index) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == imageView.getId()) {
                    if (randIndex == index) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setBackgroundColor(Color.GREEN);
                                _tvScore.setText("Ditt poäng är " + ++score);
                                try{
                                    Thread.sleep(1000L);
                                }catch(Exception e){

                                }
                                randIndex = (int) (Math.random() * 4);
                                loadConsultantDetails(randIndex);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setBackgroundColor(Color.RED);
                                if(score > highScore){
                                    highScore = score;
                                    _tvHighScore.setText("Ditt nya rekord: " + highScore);
                                }
                                score = 0;
                                _tvScore.setText("Du förlorade och står på: 0");
                                try{
                                    Thread.sleep(3000L);
                                }catch(Exception e){

                                }
                                randIndex = (int) (Math.random() * 4);
                                loadConsultantDetails(randIndex);
                            }
                        });

                    }
                }

            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();

    }

    private void loadConsultantDetails(final int randIndex) {
        runInBackground(new Runnable() {
            @Override
            public void run() {


                showHourglass();

                final ConsultantData[] allConsultants = AppCtrl.getDataService().getAllConsultants();
                final int count = allConsultants.length;
                final List<Integer> randoms = new ArrayList<>();
                randoms.add((int) (Math.random() * count));
                randoms.add((int) (Math.random() * count));
                randoms.add((int) (Math.random() * count));
                randoms.add((int) (Math.random() * count));

                final List<String> readyList = new ArrayList<>();

                loadImage(_imgConsultant1, allConsultants, randoms.get(0), readyList);
                loadImage(_imgConsultant2, allConsultants, randoms.get(1), readyList);
                loadImage(_imgConsultant3, allConsultants, randoms.get(2), readyList);
                loadImage(_imgConsultant4, allConsultants, randoms.get(3), readyList);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int rand5 = (int) (Math.random() * 4);
                        ConsultantData consultantData = allConsultants[randoms.get(randIndex)];
                        _tvVemArDet.setText(consultantData.FirstName + " " + consultantData.LastName);
                        setImageVisibility(View.VISIBLE);
                    }
                });


            }
        });
    }

    private void setImageVisibility(int visibility) {
        _imgConsultant1.setVisibility(visibility);
        _imgConsultant2.setVisibility(visibility);
        _imgConsultant3.setVisibility(visibility);
        _imgConsultant4.setVisibility(visibility);
    }

    private void loadImage(final ImageView imageView, ConsultantData[] allConsultant, final int rand, final List<String> readyList) {
        AppCtrl.getDataService().getConsultantDetails(allConsultant[rand].Id, new ConsultantDataListener() {
            @Override
            public void onResult(final ConsultantData consultantData) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setImageVisibility(View.INVISIBLE);
                        imageView.setBackgroundColor(Color.WHITE);
                        imageView.setImageBitmap(AppCtrl.getImageService().getConsultantBitmapFromFile(consultantData.Id));
                    }
                });
                readyList.add(consultantData.FirstName + " " + consultantData.LastName);
                if (readyList.size() == 4) {
                    hideHourglass();
                }

            }

            @Override
            public void onError(final String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayError(errorMessage, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Display the error and then close the activity
                                finish();

                            }
                        });
                        readyList.add("Ett fel uppstod");
                        if (readyList.size() == 4) {
                            hideHourglass();
                        }
                    }
                });
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadConsultantDetails(randIndex);
    }

}
