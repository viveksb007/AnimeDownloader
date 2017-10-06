package com.browser.viveksb007.animedownloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.browser.viveksb007.animedownloader.fragments.HomeFragment;

/**
 * Created by viveksb007 on 3/10/17.
 */

public class NavigationActivity extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST = 1;

    public boolean hasWritePermission = false;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        final PreferenceManager preferenceManager = new PreferenceManager(this);
        if (preferenceManager.isFirstTime()) {
            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .title("Anime Downloader Disclaimer")
                    .icon(getResources().getDrawable(R.drawable.ic_info_outline_black_24dp))
                    .customView(R.layout.disclaimer, true)
                    .positiveText("Accept")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            preferenceManager.setFirstLaunch(false);
                            initialise();
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
            initialise();
        }
    }

    private void initialise() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST);
        } else
            hasWritePermission = true;
        homeFragment = HomeFragment.newInstance(this);
        addFragment(homeFragment);
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (homeFragment.getWebView().canGoBack()) {
            homeFragment.getWebView().goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_download:
                homeFragment.evaluateVideoLink(false);
                break;
            case R.id.menu_stream:
                homeFragment.evaluateVideoLink(true);
                break;
            case R.id.menu_how_to:
                showHowToDialog();
                break;
            case R.id.menu_credit:
                showCreditDialog();
                break;
            case R.id.menu_refresh:
                homeFragment.getWebView().reload();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCreditDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Credits")
                .customView(R.layout.credit_dialog, true)
                .positiveText("Okay")
                .build();
        TextView tvCredit = dialog.getCustomView().findViewById(R.id.tv_credit);
        if (tvCredit != null) {
            tvCredit.setMovementMethod(LinkMovementMethod.getInstance());
        }
        dialog.show();
    }

    private void showHowToDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("How To Stream/Download Anime")
                .customView(R.layout.how_to_guide, true)
                .positiveText("Got it")
                .build();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasWritePermission = true;
            } else {
                hasWritePermission = false;
                Toast.makeText(this, "Can't Download Anime", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
