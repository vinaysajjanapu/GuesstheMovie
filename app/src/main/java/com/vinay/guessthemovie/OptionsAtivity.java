package com.vinay.guessthemovie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class OptionsAtivity extends AppCompatActivity {

    Button btn_start,btn_submit;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_ativity);


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
submitdatatoserver();
            }
        });
    }

    private void submitdatatoserver() {

        pd.setMessage("sync in progress");

        RequestQueue que=Volley.newRequestQueue(OptionsAtivity.this);

        StringRequest s=new StringRequest( )



    }
}
