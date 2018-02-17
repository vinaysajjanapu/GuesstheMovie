package com.vinay.guessthemovie.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.gson.Gson;
import com.vinay.guessthemovie.R;
import com.vinay.guessthemovie.utils.MovieDb;

import java.util.HashMap;
import java.util.Map;

public class FinishActivity extends AppCompatActivity {

    int score;
    TextView tv_summary, tv_mainscore;
    ImageView[] star;
    LinearLayout starsHolder;
    ImageButton replay;
    int percent_score, nStars;
    GoogleApiClient apiClient;
    String language;
    int level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .enableAutoManage(this, connectionResult -> {
                    Log.e("playgames", "Could not connect to Play games services");
                    finish();
                }).build();
        score = getIntent().getIntExtra("score", 0);
        language = getIntent().getStringExtra("lan");
        level = getIntent().getIntExtra("lv", 0);
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
            submitdatatoserver(language, level - 1);
        });

        findViewById(R.id.button_next_level).setOnClickListener(v -> {
            submitdatatoserver(language, level);
        });
    }

    public void showLeaderboard(View view) {
        String which;
        switch (language) {
            case "en":
                which = getString(R.string.leaderboard_english_score);
                break;
            case "hi":
                which = getString(R.string.leaderboard_hindi_score);
                break;
            case "te":
                which = getString(R.string.leaderboard_telugu_score);
                break;
            case "ta":
                which = getString(R.string.leaderboard_tamil_score);
                break;
            case "ml":
                which = getString(R.string.leaderboard_malayalam_score);
                break;
            case "kn":
                which = getString(R.string.leaderboard_kannada_score);
                break;
            default:
                which = getString(R.string.leaderboard_english_score);
        }
        startActivityForResult(
                Games.Leaderboards.getLeaderboardIntent(apiClient,
                        which), 0);
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

    private void submitdatatoserver(String language, int page_number) {

        ProgressDialog pd = new ProgressDialog(this);

        pd.setMessage("setting up data...");

        pd.show();
        int pn = page_number / 4 + 1;

        RequestQueue que = Volley.newRequestQueue(FinishActivity.this);
        StringRequest s = new StringRequest(Request.Method.POST,
                "https://api.themoviedb.org/3/discover/movie?api_key=7e8f60e325cd06e164799af1e317d7a7&with_original_language=" + language + "&page=" + pn /*+ "&primary_release_date.gte=2000"*/,
                response -> {
                    //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    try {
                        pd.dismiss();
                        Gson gson = new Gson();
                        MovieDb movieDb = gson.fromJson(response, MovieDb.class);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class)
                                .putExtra("det", response)
                                .putExtra("lan", language)
                                .putExtra("lv", page_number + 1)
                                .putExtra("QNO", (page_number % 4) * 5));
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        pd.hide();
                    }
                }, error -> {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            pd.dismiss();
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<String, String>();
                return p;
            }
        };
        que.add(s);
    }
}
