package com.test.SimplePFC;

import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.test.SimplePFC.ui.main.SectionsPagerAdapter;
import com.test.SimplePFC.databinding.ActivityInputBinding;

public class InputActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityInputBinding binding = ActivityInputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);



    }

    public void onRestart(){
        super.onRestart();
        //インターステティシャル広告
        AdRequest adRequest = new AdRequest.Builder().build();
        AdInterstitial.loadAd(this, adRequest);
    }


}