package com.browser.viveksb007.animedownloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://kissanime.ru/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout ll = (LinearLayout) findViewById(R.id.holder);
        MyWebView view = new MyWebView(this, URL);
        ll.addView(view.getWebView());
    }

}
