package com.vinay.guessthemovie;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Finish extends AppCompatActivity {

    SharedPreferences score;
    TextView tv_summary, tv_mainscore;
    ImageView[] star;
    LinearLayout starsHolder;
    ImageButton replay;
    int percent_score,nStars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        getSupportActionBar().hide();

        score = getSharedPreferences("score",MODE_WORLD_WRITEABLE);
        tv_summary = (TextView)findViewById(R.id.summary);
        tv_mainscore = (TextView)findViewById(R.id.main_score);
        starsHolder = (LinearLayout)findViewById(R.id.star_holder);
        replay = (ImageButton)findViewById(R.id.button_replay);
        star = new ImageView[3];

        tv_summary.setText("Total questions : " + score.getInt("nQ",0) + " \n Questions Correct : " + score.getInt("score",0) );
        tv_mainscore.setText(score.getInt("score",0)+" / " + score.getInt("nQ",0) );

        percent_score = (int)((((float)score.getInt("score",0))/((float)score.getInt("nQ",0)))*100);

        if (percent_score==100){
            nStars = 3;
        }else if (percent_score>90){
            nStars = 2;
        }else if (percent_score>80){
            nStars = 1;
        }else {
            nStars = 0;
        }

        LinearLayout.LayoutParams lp_star = new LinearLayout.LayoutParams(MainActivity.screenWidth/10, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int l = 0; l < nStars; l++) {
            star[l] = new ImageView(this);
            star[l].setLayoutParams(lp_star);
            star[l].setPadding(5,5,5,5);
            star[l].setImageResource(R.drawable.star);
            starsHolder.addView(star[l]);
        }

    }
}
