package com.browser.viveksb007.animedownloader.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.browser.viveksb007.animedownloader.MyWebView;
import com.browser.viveksb007.animedownloader.R;
import com.browser.viveksb007.animedownloader.WebViewInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by viveksb007 on 3/10/17.
 */

public class HomeFragment extends Fragment {

    private static final String URL = "http://kissanime.ru/";
    private static final String TEST_URL = "https://www.google.com";
    private static final String TEST_BETA_URL = "http://kissanime.ru/Anime/Naruto-Shippuuden-Dub/Episode-370?id=133775&s=beta";
    private static final String TEST_RAPID_URL = "http://kissanime.ru/Anime/Naruto-Shippuuden-Dub/Episode-370?id=133775&s=default";
    private WebView webView;
    private WebViewInterface webViewInterface;
    private String jsToInject;
    private String link = "";
    private Bundle webViewBundle;
    private MyWebView myWebView;
    private ArrayList<String> urlLoaded = new ArrayList<>();

    public static HomeFragment newInstance(Context context) {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webViewBundle = new Bundle();
        myWebView = new MyWebView(getActivity(), URL, urlLoaded);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayout ll = view.findViewById(R.id.holder);
        webView = myWebView.getWebView();
        webViewInterface = myWebView.getWebViewInterface();
        ll.addView(webView);
        jsToInject = readJSFromAssets("jsAndroid.js");
        if (webViewBundle != null && !webViewBundle.isEmpty()) {
            webView.restoreState(webViewBundle);
            webViewBundle.clear();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            InputStreamReader inputStreamReader = new InputStreamReader(getActivity().getAssets().open(file));
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

    public void evaluateVideoLink(final boolean stream) {
        webView.loadUrl("javascript:" + jsToInject);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (webViewInterface.isIFrame()) {
                            // open Iframe link and scrape video link
                        } else {
                            link = webViewInterface.getLink();
                            if ("".equals(link)) {
                                Toast.makeText(getActivity(), "Link not loaded yet", Toast.LENGTH_SHORT).show();
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

    private String obtainFileName() {
        String currentURL = myWebView.currentUrl;
        int initialMarker = currentURL.indexOf("Anime/");
        int finalMarker = currentURL.indexOf("?");
        String fileName = currentURL.substring(initialMarker + 6, finalMarker);
        fileName = fileName.replaceAll("/", "_");
        return fileName;
    }

    public WebView getWebView() {
        return webView;
    }

    private void downloadVideo() {
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(link));
        File destinationFile = new File(Environment.getExternalStorageDirectory(), obtainFileName() + ".mp4");
        downloadRequest.setDescription("Downloading Anime");
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        downloadRequest.setDestinationUri(Uri.fromFile(destinationFile));
        downloadManager.enqueue(downloadRequest);
    }

    @Override
    public void onPause() {
        webView.onPause();
        if (webView != null && webViewBundle.isEmpty()) {
            webView.saveState(webViewBundle);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        webView.onResume();
        super.onResume();
    }

    public ArrayList<String> getUrlLoaded() {
        return urlLoaded;
    }

}
