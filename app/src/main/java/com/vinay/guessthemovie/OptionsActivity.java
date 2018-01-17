package com.vinay.guessthemovie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener {

    DBHelper db;
    Button btn_telugu, btn_eng, btn_hin;
    ProgressDialog pd;
    static int i=1;

    SharedPreferences score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_ativity);

        score = getSharedPreferences("score", MODE_PRIVATE);

        SharedPreferences.Editor e = score.edit();
        e.putInt("score", 0);
        e.putInt("nQ", 0);
        e.apply();

        db = new DBHelper(this);

        btn_telugu = (Button) findViewById(R.id.btn_Telugu);
        btn_eng = (Button) findViewById(R.id.btn_English);
        btn_hin = (Button) findViewById(R.id.btn_Hindi);
        Button btn_tam = (Button) findViewById(R.id.btn_Tamil);

        btn_eng.setOnClickListener(this);
        btn_tam.setOnClickListener(this);
        btn_hin.setOnClickListener(this);
        btn_telugu.setOnClickListener(this);
    }

    private void submitdatatoserver(String language) {

        pd = new ProgressDialog(this);

        pd.setMessage("setting up data...");

        pd.show();

        RequestQueue que = Volley.newRequestQueue(OptionsActivity.this);
        String url = " https://api.themoviedb.org/3/discover/movie?api_key=7e8f60e325cd06e164799af1e317d7a7&primary_release_year=2017&sort_by=vote_average.desc&&page=1";
        StringRequest s = new StringRequest(Request.Method.POST,
                "https://api.themoviedb.org/3/discover/movie?api_key=7e8f60e325cd06e164799af1e317d7a7&with_original_language=" + language + "&page=1&primary_release_date.gte=2014",
                response -> {
                    //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    try {
                        pd.dismiss();
                        Gson gson = new Gson();
                        TeluguDb teluguDb = gson.fromJson(response, TeluguDb.class);
                        startActivity(new Intent(getApplicationContext(), LevelsActivity.class)
                                .putExtra("no_of_levels", teluguDb.getTotal_pages()  > 1000 ? 1000 : teluguDb.getTotal_pages() )
                                .putExtra("language", language));
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_English:
                submitdatatoserver("en");
                break;
            case R.id.btn_Hindi:
                submitdatatoserver("hi");
                break;
            case R.id.btn_Telugu:
                submitdatatoserver("te");
                break;
            case R.id.btn_Tamil:
                submitdatatoserver("ta");
                break;
        }
    }
}
