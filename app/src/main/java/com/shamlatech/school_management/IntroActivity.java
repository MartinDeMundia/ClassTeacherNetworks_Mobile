package com.shamlatech.school_management;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shamlatech.adapter.Intro_Adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shamlatech.utils.Vars.IntroBack;
import static com.shamlatech.utils.Vars.IntroContent;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    Intro_Adapter intro_Adapter;
    ViewPager pagerIntro;
    List<String> listIntroContent = new ArrayList<>();
    List<Integer> listIntroImage = new ArrayList<>();
    Button btnActivate, btnLogin;
    private LinearLayout viewIntroPagerCountDots;
    private ImageView[] imgIntroDots;
    public static final int REQUEST_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        listIntroContent = Arrays.asList(IntroContent);
        listIntroImage = Arrays.asList(IntroBack);

        btnActivate = findViewById(R.id.btnActivate);
        btnLogin = findViewById(R.id.btnLogin);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET},
                    REQUEST_PERMISSION);
        }


        pagerIntro = findViewById(R.id.pagerIntro);
        viewIntroPagerCountDots = findViewById(R.id.viewIntroPagerCountDots);
        intro_Adapter = new Intro_Adapter(this, listIntroContent, listIntroImage);
        pagerIntro.setAdapter(intro_Adapter);
        pagerIntro.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < listIntroContent.size(); i++) {
                    imgIntroDots[i].setImageDrawable(getResources().getDrawable(R.drawable.intro_dot_inactive));
                }
                imgIntroDots[position].setImageDrawable(getResources().getDrawable(R.drawable.intro_dot_active));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupIntroPagerViewController();

        btnActivate.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void setupIntroPagerViewController() {
        if (listIntroContent.size() != 0) {
            imgIntroDots = new ImageView[listIntroContent.size()];
            for (int i = 0; i < listIntroContent.size(); i++) {
                imgIntroDots[i] = new ImageView(this);

                imgIntroDots[i].setImageDrawable(getResources().getDrawable(R.drawable.intro_dot_inactive));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(15, 0, 15, 0);
                viewIntroPagerCountDots.addView(imgIntroDots[i], params);
            }
            imgIntroDots[0].setImageDrawable(getResources().getDrawable(R.drawable.intro_dot_active));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnActivate:
                startActivity(new Intent(IntroActivity.this, RoleSelectionActivity.class).putExtra("path", "activate"));

                break;
            case R.id.btnLogin:
                startActivity(new Intent(IntroActivity.this, RoleSelectionActivity.class).putExtra("path", "login"));
                break;
        }
    }
}
