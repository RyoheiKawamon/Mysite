package com.test.SimplePFC;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DairyActivity extends AppCompatActivity {

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy);

        //Google　Mobile　Ads　SDK　の初期化
        /*MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

         */
        //バナー広告
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);

        //アクションバーで戻る
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //リスト表示
        createDairy();

        ListView lv = findViewById(R.id.lv_at_ActivityDiary);
        lv.setOnItemClickListener(new listItemClickListener());
    }
    @Override
    public void onStart(){
        super.onStart();
        //リスト表示
        createDairy();
    }
    //アクションバー戻る
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //リスト作成表示
    public void createDairy(){
        //リストビューにはめるリスト作成
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> data;
        //データベース接続
        PFCRecordsDatabaseHelper helper = new PFCRecordsDatabaseHelper(DairyActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sql = "SELECT SUM(protein), SUM(fat), SUM(carb), date FROM pfcRecords GROUP BY date";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                //インデックス取得
                int idxD = cursor.getColumnIndex("date");
                int idxP = cursor.getColumnIndex("SUM(protein)");
                int idxF = cursor.getColumnIndex("SUM(fat)");
                int idxC = cursor.getColumnIndex("SUM(carb)");
                // インデックスを元にpfcデータを取得する
                double p = ((double)Math.round(cursor.getDouble(idxP) * 10))/10;
                double f = ((double)Math.round(cursor.getDouble(idxF) * 10))/10;
                double c = ((double)Math.round(cursor.getDouble(idxC) * 10))/10;
                int cal = (int)(4 * p + 9 * f + 4 * c);
                //データを全部String型にする
                String dst = cursor.getString(idxD);
                String pst = p + "g";
                String fst = f + "g";
                String cst = c + "g";
                String calst = cal + "kcal";
                //Mapを作成してListに入れていく
                data = new HashMap<>();
                data.put("date",dst);
                data.put("protein",pst);
                data.put("fat", fst);
                data.put("carb", cst);
                data.put("cal", calst);
                //listに入れる
                dataList.add(data);
            }

        }
        finally {
            db.close();
        }
        //リストビューにデータを当てはめる
        ListView lvTodaysRecord = findViewById(R.id.lv_at_ActivityDiary);
        String[] from = {"cal", "date"};
        int[] to = {R.id.tv_cal_value_at_design_of_diary_list, R.id.tv_time_at_design_of_diary_list};
        SimpleAdapter adapter = new SimpleAdapter(DairyActivity.this, dataList, R.layout.design_of_diary_list, from, to);
        lvTodaysRecord.setAdapter(adapter);
    }

    //
    private class listItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
            String p = item.get("protein");
            String f = item.get("fat");
            String c = item.get("carb");
            String cal = item.get("cal");
            String date = item.get("date");

            DetailOfTheDayDialogFragment detailOfTheDayDialogFragment = new DetailOfTheDayDialogFragment(p, f, c, cal, date);
            detailOfTheDayDialogFragment.show(getSupportFragmentManager(), "DetailOfTheDayDialogFragment");

        }
    }
}