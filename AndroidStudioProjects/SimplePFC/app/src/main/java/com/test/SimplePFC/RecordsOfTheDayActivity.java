package com.test.SimplePFC;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordsOfTheDayActivity extends AppCompatActivity {

    private String date;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_of_the_today);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);

        //date設定
        Intent intent = getIntent();
        date = intent.getStringExtra("date");

        //アクションバーで戻る
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //日つけ取得してタイトル設定
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String today = format.format(calendar.getTime());

        if(!date.equals(today)){
            actionBar.setTitle(date + "の記録" );
        }

        //contextメニュー
        ListView lvRecorsOfToday = findViewById(R.id.lv_at_ActivityRecordsOfToday);
        lvRecorsOfToday.setOnItemClickListener(new ListItemClickListener());
        registerForContextMenu(lvRecorsOfToday);
    }

    @Override
    public void onStart(){
        super.onStart();
        //リスト作成
        createList();
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

    /*
    //contextメニュー
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_context, menu);
        menu.setHeaderTitle(R.string.deleteQ);
    }
    //contextメニュータップ時の処理
    @Override
    public boolean onContextItemSelected(MenuItem item){
        ListView dataList = findViewById(R.id.lv_at_ActivityRecordsOfToday);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Integer listPosition = info.position;
        Map<String, String> data = (Map<String, String>) dataList.getItemAtPosition(listPosition);
        String _id = (String) data.get("id");
        int id = Integer.parseInt(_id);

        int itemId = item.getItemId();
        switch (itemId){
            case R.id.deleteContextDelete:
                PFCRecordsDatabaseHelper helper = new PFCRecordsDatabaseHelper(RecordsOfTheDayActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    String sqlDelete = "DELETE FROM pfcRecords WHERE _id = ?";
                    SQLiteStatement stmt = db.compileStatement(sqlDelete);
                    stmt.bindLong(1,id);
                    stmt.executeUpdateDelete();
                }
                finally {
                    db.close();
                }
                createList();
                break;
            case R.id.deleteContextEdit:
                break;
        }

        return super.onContextItemSelected(item);
    }
*/

    //今日の記録リスト作成メソッド
    private void createList (){

        String[] dateAry =  {date};
        //String[] date = {dateString};

        //リストビューにはめるリスト作成
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> data;
        //データベース接続オブジェクト取得
        PFCRecordsDatabaseHelper helper = new PFCRecordsDatabaseHelper(RecordsOfTheDayActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sql = "SELECT * FROM pfcRecords WHERE date = ?";
            Cursor cursor = db.rawQuery(sql,dateAry);
            while (cursor.moveToNext()){
                //インデックス取得
                int idxId = cursor.getColumnIndex("_id");
                int idxD = cursor.getColumnIndex("date");
                int idxT = cursor.getColumnIndex("time");
                int idxP = cursor.getColumnIndex("protein");
                int idxF = cursor.getColumnIndex("fat");
                int idxC = cursor.getColumnIndex("carb");
                int idxComment = cursor.getColumnIndex("comment");
                // インデックスを元にpfcデータを取得する
                double p = cursor.getDouble(idxP);
                double f = cursor.getDouble(idxF);
                double c = cursor.getDouble(idxC);
                int cal = (int)(4 * p + 9 * f + 4 * c);
                Integer id = cursor.getInt(idxId);
                String comment = cursor.getString(idxComment);
                //データを全部String型にする
                String ist =id.toString();
                String commentst = comment.replace("・", "");
                String dst = cursor.getString(idxD);
                String tst = cursor.getString(idxT);
                String pst = p + "g";
                String fst = f + "g";
                String cst = c + "g";
                String calst = cal + "kcal";
                //Mapを作成してListに入れていく
                data = new HashMap<>();
                data.put("id",ist);
                data.put("comment",commentst);
                data.put("date",dst);
                data.put("time", tst);
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
        ListView lvTRecordsOfToday = findViewById(R.id.lv_at_ActivityRecordsOfToday);
        String[] from = {"cal", "time", "comment"};
        int[] to = {R.id.tv_cal_value_at_Design_DailyPFCRecordList, R.id.tv_time_at_Design_DailyPFCRecordList, R.id.tv_item_name_at_Design_DailyPFCRecords};
        SimpleAdapter adapter = new SimpleAdapter(RecordsOfTheDayActivity.this, dataList, R.layout.design_of_daily_pfc_record_list, from, to);
        lvTRecordsOfToday.setAdapter(adapter);
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);

            String itemIdst = item.get("id");
            String comment = item.get("comment");
            String protein = item.get("protein");
            String fat = item.get("fat");
            String carb = item.get("carb");
            String cal = item.get("cal");
            String time = item.get("time");

            RecordInfoDialogFragment recordInfoDialogFragment = new RecordInfoDialogFragment(itemIdst, comment, protein, fat, carb, cal, time);
            recordInfoDialogFragment.show(getSupportFragmentManager(), "RecordInfoDialogFragment");


        }
    }

}