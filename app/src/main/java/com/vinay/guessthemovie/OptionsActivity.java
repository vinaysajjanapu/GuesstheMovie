package com.vinay.guessthemovie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OptionsActivity extends AppCompatActivity {

    DBHelper db;
    Button btn_start;
    ProgressDialog pd;

    SharedPreferences score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_ativity);

        score=getSharedPreferences("score",MODE_WORLD_WRITEABLE);

        SharedPreferences.Editor e=score.edit();
        e.putInt("score",0);
        e.putInt("nQ",0);
        e.apply();

        db=new DBHelper(this);

        btn_start= (Button) findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (db.getAllMovieDetails().size() > 0) {

                    if (Utils.isOnline(getApplicationContext())) {
                        submitdatatoserver();
                        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else startActivity(new Intent(getApplicationContext(), MainActivity.class));

                } else {
                    if (Utils.isOnline(getApplicationContext())) {
                        submitdatatoserver();

                    } else Utils.internetAlert(OptionsActivity.this);
                }
            }});
        }

    private void submitdatatoserver() {

        pd=new ProgressDialog(this);

        pd.setMessage("sync in progress");

        pd.show();


       // Toast.makeText(getApplicationContext(), db.cleanDb()+"", Toast.LENGTH_SHORT).show();


        for(int i=1;i<=50;i++) {

        RequestQueue que = Volley.newRequestQueue(OptionsActivity.this);
    //http://whencutwini.16mb.com/GuessTheMovie/getMovies.php
//
            final int finalI = i;
            StringRequest s = new StringRequest(Request.Method.POST,
            "http://api.themoviedb.org/3/movie/top_rated?api_key=87827f3c119a9d103cb4f2e78112046f&page="+i,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject j = new JSONObject(response);

                        JSONArray ja = j.getJSONArray("results");

                       // pd.setMessage("add to local db");
                        long a = db.addMovieDetails(ja);
                        if(finalI ==49) {
                            Toast.makeText(getApplicationContext(), a + "  size:-" + db.getAllMovieDetails().size(), Toast.LENGTH_LONG).show();
                            pd.dismiss();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        pd.hide();
                    }


                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            pd.dismiss();
        }
    }) {
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> p = new HashMap<String, String>();

            return p;
        }
    };

    que.add(s);
}


       // pd.dismiss();

    }
}
