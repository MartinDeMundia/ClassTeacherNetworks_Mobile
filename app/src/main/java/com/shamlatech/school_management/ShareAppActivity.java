package com.shamlatech.school_management;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.shamlatech.utils.Vars;

public class ShareAppActivity extends BaseFragment {
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    SharedPreferences sharedpreferences;
    Activity activity;
    private WebView mWebview ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_share_app, container, false);
        sharedpreferences = activity.getSharedPreferences(Vars.MyPref, Context.MODE_PRIVATE);
        ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setChooserTitle("Share URL")
                .setText("https://play.google.com/store/apps/details?id=bbr.classteacher.app")
                .startChooser();

        WebView webView;
        webView = view.findViewById(R.id.help_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.classteacher.school");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) activity).declareAppBar1("Share App", false);
        ((BaseActivity) activity).UpdateAppBarColor(R.color.colorTabPrimary);
        sharedpreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onDestroy() {
        sharedpreferences.unregisterOnSharedPreferenceChangeListener(listener);
        super.onDestroy();
    }

}



