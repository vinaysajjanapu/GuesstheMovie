package com.vinay.guessthemovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by salimatte on 17-01-2018.
 */

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.btn_start).setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(), OptionsActivity.class));
            finish();
        });
    }
}
