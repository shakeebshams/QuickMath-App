package com.shakeeb.shams.quickmath;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

import com.example.shams.quickmaths.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private Button goButton;
    private ArrayList<Integer> answers = new ArrayList<>();
    private int locationOfCorrect;
    private TextView resultTextView;
    private int score = 0;
    private int numOfQuestions = 0;
    private TextView scoreTextView;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private TextView sumText;
    private TextView timerTextView;
    private Button playAgainButton;
    private ConstraintLayout gameLayout;
    private ImageView gtImageView;
    private TextView creditsTextView;
    private GridLayout gridLayout;
    private MediaPlayer mediaPlayer;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;


    public void playAgain(View view) {
        score = 0;
        numOfQuestions = 0;
        timerTextView.setText("30s");
        button0.setEnabled(true);
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        resultTextView.setText("Start!");
        newQuestion();
        scoreTextView.setText(Integer.toString(score) + "/" + Integer.toString(numOfQuestions));

        playAgainButton.setVisibility(View.INVISIBLE);
        new CountDownTimer(30100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String secondsLeft = String.valueOf(millisUntilFinished/1000) + "s";
                timerTextView.setText(secondsLeft);
            }

            @Override
            public void onFinish() {
                resultTextView.setText("Time Up!");
                playAgainButton.setVisibility(View.VISIBLE);
                button0.setEnabled(false);
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
                mediaPlayer.start();
                //mInterstitialAd.loadAd(new AdRequest.Builder().build());
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }

            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    public void chooseAns(View view) {
        if (Integer.toString(locationOfCorrect).equals(view.getTag().toString())) {
            resultTextView.setText("Correct!");
            score++;
        } else {
            resultTextView.setText("Wrong :(");
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
            }
        }
        numOfQuestions++;
        scoreTextView.setText(Integer.toString(score) + "/" + Integer.toString(numOfQuestions));
        newQuestion();
    }

    public void start(View view) {
        goButton.setVisibility(View.INVISIBLE);
        creditsTextView.setVisibility(View.INVISIBLE);
        gtImageView.setVisibility(View.INVISIBLE);
        gameLayout.setVisibility(View.VISIBLE);
        playAgain(timerTextView);
    }

    public void newQuestion() {
        Random rand = new Random();
        int firstNum = rand.nextInt(51);
        int secondNum = rand.nextInt(51);

        sumText.setText(Integer.toString(firstNum) + " + " + Integer.toString(secondNum));

        locationOfCorrect = rand.nextInt(4);
        answers.clear();
        for (int i = 0; i < 4; i++) {
            if (i == locationOfCorrect) {
                answers.add(firstNum + secondNum);
            } else {
                int wrongAns = rand.nextInt(101);
                while (wrongAns == (firstNum + secondNum)) {
                    wrongAns = rand.nextInt(101);
                }
                answers.add(wrongAns);
            }
        }
        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        MobileAds.initialize(this, "ca-app-pub-8569084124971795~3412395587");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8569084124971795/4857624605");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        scoreTextView = findViewById(R.id.scoreTextView);
        sumText = findViewById(R.id.sumText);

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        playAgainButton = findViewById(R.id.playAgainButton);

        gameLayout = findViewById(R.id.gameLayout);
        gameLayout.setVisibility(View.INVISIBLE);

        resultTextView = findViewById(R.id.resultTextView);
        timerTextView = findViewById(R.id.timerTextView);

        goButton = findViewById(R.id.goButton);
        goButton.setVisibility(View.VISIBLE);

        gtImageView = findViewById(R.id.gtImageView);
        gtImageView.setVisibility(View.VISIBLE);

        creditsTextView = findViewById(R.id.creditsTextView);
        creditsTextView.setVisibility(View.VISIBLE);

        mediaPlayer = MediaPlayer.create(this, R.raw.airhorn);

        //gridLayout = findViewById(R.id.gridLayout);



    }
}
