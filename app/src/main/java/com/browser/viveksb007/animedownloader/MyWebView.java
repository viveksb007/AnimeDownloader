package com.browser.viveksb007.animedownloader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebView {

    private WebView webView;
    private WebSettings webSettings;
    private static int API = android.os.Build.VERSION.SDK_INT;

    @SuppressWarnings("deprecation")
    public MyWebView(Activity activity, String url) {
        webView = new WebView(activity);
        webView.setDrawingCacheBackgroundColor(0x00000000);
        webView.setFocusableInTouchMode(true);
        webView.setFocusable(true);
        webView.setAnimationCacheEnabled(false);
        webView.setDrawingCacheEnabled(true);
        webView.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
        if (API > 15) {
            webView.setBackground(null);
            webView.getRootView().setBackground(null);
        } else {
            webView.getRootView().setBackgroundDrawable(null);
        }
        webView.setWillNotCacheDrawing(false);
        webView.setAlwaysDrawnWithCacheEnabled(true);
        webView.setScrollbarFadingEnabled(true);
        webView.setSaveEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new MyWebClient());
        webSettings = webView.getSettings();
        initializeSettings(webView.getSettings(), activity);
        initializePreferences();
        webView.loadUrl(url);
    }

    @SuppressLint({"SetJavaScriptEnabled", "InlinedApi"})
    @SuppressWarnings("deprecation")
    public synchronized void initializePreferences() {
        webSettings.setGeolocationEnabled(false);
        webSettings.setPluginState(PluginState.OFF);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (API >= android.os.Build.VERSION_CODES.KITKAT) {
            webSettings.setLayoutAlgorithm(LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        }
        webSettings.setBlockNetworkImage(false);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextZoom(100);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    public void initializeSettings(WebSettings settings, Context context) {
        if (API < 18) {
            settings.setAppCacheMaxSize(Long.MAX_VALUE);
        }
        if (API < 17) {
            settings.setEnableSmoothTransition(true);
        }
        if (API > 16) {
            settings.setMediaPlaybackRequiresUserGesture(true);
        }
        if (API < 19) {
            settings.setDatabasePath(context.getCacheDir() + "/databases");
        }
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(context.getCacheDir().toString());
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setGeolocationDatabasePath(context.getFilesDir().toString());
        settings.setAllowFileAccess(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowContentAccess(true);
        settings.setDefaultTextEncodingName("utf-8");
        if (API > 16) {
            settings.setAllowFileAccessFromFileURLs(false);
            settings.setAllowUniversalAccessFromFileURLs(false);
        }
    }

    public WebView getWebView() {
        return webView;
    }

    public class MyWebClient extends WebViewClient {

        MyWebClient() {
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            /*
            if (mAdBlock.isAd(url)) {
                ByteArrayInputStream EMPTY = new ByteArrayInputStream("".getBytes());
                return new WebResourceResponse("text/plain", "utf-8", EMPTY);
            }
            */
            return null;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    }
}
