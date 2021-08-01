package com.shamlatech.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;


import com.shamlatech.school_management.LoginActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.services.MyWebCilent;

import java.util.HashMap;
import java.util.Map;


public class MyWebView extends AppCompatActivity implements MyWebCilent.PageListener {
    WebView webView;
    ImageButton backBtn;
    TextView baseUrl;
    View completeBtn;
    String url;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        webView = findViewById(R.id.webview);
        backBtn = findViewById(R.id.back_btn);
        baseUrl = findViewById(R.id.baseurl);
        completeBtn = findViewById(R.id.complete_btn);
        url = (String) getIntent().getExtras().get("url");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebCilent(this));
        webView.loadUrl(url);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.canGoBack();
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public void onPageFinished(WebView webView, String url) {
        Log.i("url", url);
        //check if url is original loaded url'
        //else show complete button
        if (!url.equalsIgnoreCase(this.url)) {
            if (completeBtn.getVisibility() != View.VISIBLE) {
                completeBtn.setVisibility(View.VISIBLE);
            }
        }

    }
}
