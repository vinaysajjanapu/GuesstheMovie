package com.vinay.guessthemovie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by salimatte on 21-01-2018.
 */

public class HintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hint);

        MobileAds.initialize(this, getString(R.string.AdmobAppId));
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("28E0DB4FBF1A52ABC4781B3D45FD2C8E").build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("kuchbiliko", "loaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("kuchbiliko", i + "");
            }
        });

        TextView tv_hint = findViewById(R.id.tv_ht);
        tv_hint.setText(getIntent().getStringExtra("hint"));
    }
}
