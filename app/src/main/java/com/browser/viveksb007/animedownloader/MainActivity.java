package com.browser.viveksb007.animedownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appodeal.ads.Appodeal;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://kissanime.ru/";
    private static final String TEST_URL = "https://www.google.com";
    private static final String TEST_BETA_URL = "http://kissanime.ru/Anime/Naruto-Shippuuden-Dub/Episode-370?id=133775&s=beta";
    private static final String TEST_RAPID_URL = "http://kissanime.ru/Anime/Naruto-Shippuuden-Dub/Episode-370?id=133775&s=default";
    private WebView webView;
    private WebViewInterface webViewInterface;
    private String jsToInject;
    private String link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Appodeal.initialize(this, getString(R.string.app_key), Appodeal.INTERSTITIAL | Appodeal.BANNER);

        LinearLayout ll = findViewById(R.id.holder);
        MyWebView view = new MyWebView(this, TEST_URL);
        webView = view.getWebView();
        webViewInterface = view.getWebViewInterface();
        ll.addView(webView);
        jsToInject = readJSFromAssets("jsAndroid.js");
    }

    private void streamVideo() {
        // Creating an intent for some external video player for now. Would add video player support with ads later :P
        if (!"".equals(link)) {
            Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            videoIntent.setDataAndType(Uri.parse(link), "video/mp4");
            startActivity(videoIntent);
        }
    }

    private String readJSFromAssets(String file) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(this.getAssets().open(file));
            StringBuilder jsString = new StringBuilder();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                jsString.append(line);
                line = reader.readLine();
            }
            reader.close();
            inputStreamReader.close();
            return jsString.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void evaluateVideoLink(final boolean stream) {
        webView.loadUrl("javascript:" + jsToInject);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (webViewInterface.isIFrame()) {
                            // open Iframe link and scrape video link
                        } else {
                            link = webViewInterface.getLink();
                            if ("".equals(link)) {
                                Toast.makeText(MainActivity.this, "Link not loaded yet", Toast.LENGTH_SHORT).show();
                            } else {
                                if (stream) {
                                    streamVideo();
                                } else {
                                    downloadVideo();
                                }
                            }
                        }
                    }
                });
            }
        }, 500);
    }

    private void downloadVideo() {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(link));
        File destinationFile = new File(Environment.getExternalStorageDirectory(), String.valueOf(System.currentTimeMillis() + ".mp4"));
        downloadRequest.setDescription("Downloading Anime");
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        downloadRequest.setDestinationUri(Uri.fromFile(destinationFile));
        downloadManager.enqueue(downloadRequest);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
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
                evaluateVideoLink(false);
                break;
            case R.id.menu_stream:
                evaluateVideoLink(true);
                break;
            case R.id.menu_how_to:
                showHowToDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHowToDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("How To Guide")
                .customView(R.layout.how_to_guide, true)
                .positiveText("Got it")
                .build();
        dialog.show();
    }

    @Override
    protected void onResume() {
        webView.onResume();
        Appodeal.onResume(this, Appodeal.BANNER);
        Appodeal.show(this, Appodeal.BANNER_BOTTOM);
        super.onResume();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }
}
