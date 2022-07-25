package com.test.SimplePFC;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private String date;
    public AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Google　Mobile　Ads　SDK　の初期化
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adRequest = new AdRequest.Builder().build();
        //広告ロード
        AdInterstitial.loadAd(this,adRequest);

        //アクションバーを非表示にする
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    @Override
    protected void onStart(){
        super.onStart();

        //日付け取得
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        date = format.format(calendar.getTime());

        //数値表示
        showNumbers();
    }

    //プラスボタン
    public void OnToRecordButtonClick(View view){
        //Intent intent = new Intent(MainActivity.this, ManualInput.class);
        Intent intent = new Intent(MainActivity.this, InputActivity.class);
        startActivity(intent);
    }
    //今日の記録ボタン
    public void  onRecordsOfTodayButtonClick(View view){
        Intent intent = new Intent(MainActivity.this, RecordsOfTheDayActivity.class);
        intent.putExtra("date",date);
        startActivity(intent);
    }
    //過去の記録ボタン
    public void OnDairyButtonClick(View view){
        Intent intent = new Intent(MainActivity.this, DairyActivity.class);
        startActivity(intent);
    }


    //数値表示メソッド
    public void showNumbers(){
        //今日の各値の合計を算出する
        SumMaker sumMaker = new SumMaker(MainActivity.this, date);
        //それぞれの数値をテキストビューにセット
        TextView tvProteinValue = findViewById(R.id.tv_protein_value_at_ActivityMain);
        tvProteinValue.setText(sumMaker.getPSum() + "g");
        TextView tvFatValue = findViewById(R.id.tv_fat_value_at_ActivityMain);
        tvFatValue.setText(sumMaker.getFSum()+ "g");
        TextView tvCarbValue = findViewById(R.id.tv_carb_value_at_ActivityMain);
        tvCarbValue.setText(sumMaker.getCSum()+ "g");
        TextView tvCal = findViewById(R.id.tv_cal_value_at_ActivityMain);
        tvCal.setText(sumMaker.getCalSum()+ "kcal");
    }

}