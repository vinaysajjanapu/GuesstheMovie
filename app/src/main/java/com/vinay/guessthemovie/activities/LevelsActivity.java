package com.vinay.guessthemovie.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter;
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel;
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewBinder;
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewProvider;
import com.google.gson.Gson;
import com.vinay.guessthemovie.R;
import com.vinay.guessthemovie.utils.BetweenSpacesItemDecoration;
import com.vinay.guessthemovie.utils.MovieDb;
import com.vinay.guessthemovie.utils.RealmController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelsActivity extends AppCompatActivity {

    String lan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        /*MobileAds.initialize(this, getString(R.string.AdmobAppId));
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        lan = getIntent().getStringExtra("language");
        List<LevelModel> levelModels = new ArrayList<>();

        for (int i = 0; i < getIntent().getIntExtra("no_of_levels", 10) * 4; i++) {
            levelModels.add(new LevelModel(i));
        }
        RendererRecyclerViewAdapter mRecyclerViewAdapter = new RendererRecyclerViewAdapter();

        mRecyclerViewAdapter.registerRenderer(new ViewBinder<>(
                R.layout.item_level,
                LevelModel.class,
                this,
                (model, finder, payloads) -> finder
                        .find(R.id.iv_level_number, (ViewProvider<ImageView>) textView -> {
                            int score = RealmController.with(this).getScore(lan + (model.getNumber() + 1));
                            TextDrawable drawable;
                            switch (score) {
                                case 0:
                                    drawable = TextDrawable.builder()
                                            .buildRect(String.valueOf(model.getNumber() + 1), Color.RED);
                                    break;
                                case 5:
                                    drawable = TextDrawable.builder()
                                            .buildRect(String.valueOf(model.getNumber() + 1), Color.GREEN);
                                    break;
                                default:
                                    drawable = TextDrawable.builder()
                                            .buildRoundRect(String.valueOf(model.getNumber() + 1), Color.rgb(255, 223, 0), 5);
                            }
                            textView.setImageDrawable(drawable);
                        })
                        .find(R.id.tv_level_score, (ViewProvider<TextView>) tv -> {
                            tv.setText(RealmController.with(this).getScore(lan + (model.getNumber() + 1)) + "/" + 5);
                        })
                        .setOnClickListener(R.id.iv_level_number, v -> {
                            submitdatatoserver(lan, model.getNumber());
                            //startActivity(new Intent(LevelsActivity.this, MainActivity.class));
                        })
        ));

        mRecyclerViewAdapter.setItems(levelModels);

        final RecyclerView recyclerView = findViewById(R.id.rv_levels);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(mRecyclerViewAdapter);
        recyclerView.addItemDecoration(new BetweenSpacesItemDecoration(10, 10));
    }


    class LevelModel implements ViewModel {

        private int number;

        LevelModel(int number) {
            this.number = number;
        }

        int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    private void submitdatatoserver(String language, int page_number) {

        ProgressDialog pd = new ProgressDialog(this);

        pd.setMessage("setting up data...");

        pd.show();
        int pn = page_number / 4 + 1;

        RequestQueue que = Volley.newRequestQueue(LevelsActivity.this);
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
