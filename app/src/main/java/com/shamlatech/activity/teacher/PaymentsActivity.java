package com.shamlatech.activity.teacher;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.shamlatech.adapter.PaymentsPagerAdapter;
import com.shamlatech.fragment.NewPayment;
import com.shamlatech.fragment.Transactions;
import com.shamlatech.school_management.R;

import java.util.ArrayList;
import java.util.List;

public class PaymentsActivity extends AppCompatActivity {
    ImageView imgAppBack1;
    TabLayout tabLayout;
    ViewPager viewPager;
    PaymentsPagerAdapter pagerAdapter;
    List<Fragment> fragments=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        imgAppBack1=findViewById(R.id.imgAppBack1);
        imgAppBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        setUpTabs();
    }

    private void setUpTabs() {

        fragments.clear();
        fragments.add(new NewPayment());
        fragments.add(new Transactions());
        pagerAdapter=new PaymentsPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
