package com.browser.viveksb007.animedownloader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by viveksb007 on 3/10/17.
 */

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Context context = this;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final PreferenceManager preferenceManager = new PreferenceManager(context);
                if (preferenceManager.isFirstTime()) {
                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title("Anime Downloader Disclaimer")
                            .icon(getResources().getDrawable(R.drawable.ic_info_outline_black_24dp))
                            .customView(R.layout.disclaimer, true)
                            .positiveText("Accept")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    preferenceManager.setFirstLaunch(false);
                                    startMainActivity();
                                }
                            })
                            .negativeText("Close")
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finish();
                                }
                            })
                            .build();
                    View view = dialog.getCustomView();
                    assert view != null;
                    TextView tvDisclaimer = view.findViewById(R.id.tv_disclaimer);
                    if (tvDisclaimer != null) {
                        tvDisclaimer.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                    dialog.show();
                } else {
                    startMainActivity();
                }
            }
        }, 500);
    }

    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
