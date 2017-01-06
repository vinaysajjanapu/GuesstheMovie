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
    Button btn_start,btn_submit;
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
        btn_submit= (Button) findViewById(R.id.btn_sync);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utils.isOnline(getApplicationContext()))
                {
                    submitdatatoserver();
                }
                else Utils.internetAlert(OptionsActivity.this);
            }
        });
    }

    private void submitdatatoserver() {

        pd=new ProgressDialog(this);

        pd.setMessage("sync in progress");

        pd.show();

        RequestQueue que=Volley.newRequestQueue(OptionsActivity.this);

        StringRequest s=new StringRequest(Request.Method.POST, "http://whencutwini.16mb.com/GuessTheMovie/getMovies.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                try {
                    JSONObject j=new JSONObject(response);

                    JSONArray ja=j.getJSONArray("data");

                    pd.setMessage("add to local db");
                    long a= db.addMovieDetails(ja);
                    Toast.makeText(getApplicationContext(),a+"",Toast.LENGTH_LONG).show();
                    pd.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
           protected  Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> p=new HashMap<String, String>();

                return p;
            }
        };

          que.add(s);

    }
}
