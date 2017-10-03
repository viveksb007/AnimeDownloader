package com.browser.viveksb007.animedownloader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.appodeal.ads.Appodeal;

/**
 * Created by viveksb007 on 3/10/17.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Appodeal.initialize(this, getString(R.string.app_key), Appodeal.INTERSTITIAL | Appodeal.BANNER);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, NavigationActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        Appodeal.onResume(this, Appodeal.BANNER);
        Appodeal.show(this, Appodeal.BANNER_BOTTOM);
        super.onResume();
    }

}
