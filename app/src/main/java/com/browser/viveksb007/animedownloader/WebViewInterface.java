package com.browser.viveksb007.animedownloader;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by viveksb007 on 3/10/17.
 */

public class WebViewInterface {

    private String link = "";
    private String iFrameLink = "";
    private boolean isIFrame = false;

    WebViewInterface() {
    }

    @JavascriptInterface
    public void sendLink(String link) {
        Log.v("Video Link", link);
        this.link = link;
        isIFrame = false;
    }

    @JavascriptInterface
    public void iFrameLink(String iFrameLink) {
        Log.v("Iframe Link", iFrameLink);
        this.iFrameLink = iFrameLink;
        isIFrame = true;
    }

    public String getLink() {
        return link;
    }

    public String getiFrameLink() {
        return iFrameLink;
    }

    public boolean isIFrame() {
        return isIFrame;
    }

}
