package com.vinay.guessthemovie.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vinay.guessthemovie.R;

public class FinishActivity extends AppCompatActivity {

    int score;
    TextView tv_summary, tv_mainscore;
    ImageView[] star;
    LinearLayout starsHolder;
    ImageButton replay;
    int percent_score, nStars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        score = getIntent().getIntExtra("score", 0);
        tv_summary = findViewById(R.id.summary);
        tv_mainscore = findViewById(R.id.main_score);
        starsHolder = findViewById(R.id.star_holder);
        replay = findViewById(R.id.button_replay);
        star = new ImageView[3];

        tv_summary.setText("Total questions : " + 5 + " \n Questions Correct : " + score);
        tv_mainscore.setText(score + " / " + 5);

        float a = (float) score;
        float b = 5.00f;

        percent_score = (int) (100 * (a / b));

        if (percent_score == 100) {
            nStars = 3;
        } else if (percent_score > 60) {
            nStars = 2;
        } else if (percent_score > 10) {
            nStars = 1;
        } else {
            nStars = 0;
        }

        LinearLayout.LayoutParams lp_star = new LinearLayout.LayoutParams(MainActivity.screenWidth / 10, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int l = 0; l < 3; l++) {
            star[l] = new ImageView(this);
            star[l].setLayoutParams(lp_star);
            star[l].setPadding(5, 5, 5, 5);
            if (l < nStars) {
                star[l].setImageResource(R.drawable.star);
            } else {
                star[l].setImageResource(R.drawable.star_hollow);
            }
            starsHolder.addView(star[l]);
        }

        replay.setOnClickListener(view -> {
            overridePendingTransition(R.anim.slide_l1, R.anim.slide_r1);
            finish();
        });


    }

    @Override
    public void onBackPressed() {
//        ResetScore();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
