package com.vinay.guessthemovie.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.gson.Gson;
import com.vinay.guessthemovie.fragments.GameFragment;
import com.vinay.guessthemovie.R;
import com.vinay.guessthemovie.utils.MovieDb;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GameFragment.OnFragmentInteractionListener {

    public static int screenWidth, screenHeight;
    DisplayMetrics metrics;
    GoogleApiClient apiClient;
    List<MovieDb.ResultsBean> m_details;
    int index = 0;
    public static int score, nQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .enableAutoManage(this, connectionResult -> {
                    Log.e("playgames", "Could not connect to Play games services");
                    finish();
                }).build();
        Gson gson = new Gson();
        MovieDb helpDb = gson.fromJson(getIntent().getStringExtra("det"), MovieDb.class);
        int SNo = getIntent().getIntExtra("QNO", 0);
        m_details = helpDb.getResults().subList(SNo, SNo + 5);
        Collections.shuffle(m_details);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main, new GameFragment(m_details.get(index)))
                .commit();
    }


    @Override
    public void onFragmentInteraction() {
        index++;
        if (index < 5) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_l, R.anim.slide_r);
            ft.replace(R.id.activity_main, new GameFragment(m_details.get(index)), "gameFragment");
            ft.commit();
        } else {
            Games.Leaderboards.submitScore(apiClient,
                    getString(R.string.leaderboard_telugu_score),
                    score);
            startActivity(new Intent(getApplicationContext(), FinishActivity.class).putExtra("score", score));
            finish();
        }
    }
}

