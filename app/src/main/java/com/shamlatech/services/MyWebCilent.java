package com.shamlatech.services;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebCilent extends WebViewClient {
    PageListener pageListener;
    public MyWebCilent(PageListener pageListener){
        this.pageListener=pageListener;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view,String url) {
        if(Uri.parse(url).getHost().contains("ipay")) return false;
        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        pageListener.onPageFinished(view,url);
    }

    public interface PageListener{
        void onPageFinished(WebView webView, String url);
    }
}
