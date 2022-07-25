package com.test.SimplePFC.ui.main;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.test.SimplePFC.AdInterstitial;
import com.test.SimplePFC.InputActivity;
import com.test.SimplePFC.MainActivity;
import com.test.SimplePFC.PFCRecordsDatabaseHelper;
import com.test.SimplePFC.MyListDatabaseHelper;
import com.test.SimplePFC.EditMyListActivity;
import com.test.SimplePFC.ItemInfoDialogFragment;
import com.test.SimplePFC.R;
import com.test.SimplePFC.RecordsOfTheDayActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyListFragment extends Fragment {
    private View viewML;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceSate){
        viewML = inflater.inflate(R.layout.fragment_my_list, container, false);

        //リストビュー
        ListView listView = viewML.findViewById(R.id.lv_my_list);
        //クリックリスナー
        listView.setOnItemClickListener(new ListItemClickListener());
        //ロングクリックリスナー
        listView.setOnItemLongClickListener(new ListItemLongClickListener());


        return viewML;

    }

    @Override public  void  onResume(){
        super.onResume();
        createMyList();
    }

    //リスト作成表示
    public void createMyList(){
        //リストビューにはめるリスト作成
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> data;
        //マイリスト追加ボタン
        data = new HashMap<>();
        data.put("id", "0");
        data.put("item_name","新しいアイテムを登録");
        data.put("cal", "      +      ");
        //listに入れる
        dataList.add(data);
        //データベース接続
        MyListDatabaseHelper helper = new MyListDatabaseHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sql = "SELECT * FROM myList";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                //インデックス取得
                int idxID = cursor.getColumnIndex("_id");
                int idxIN = cursor.getColumnIndex("item_name");
                int idxP = cursor.getColumnIndex("protein");
                int idxF = cursor.getColumnIndex("fat");
                int idxC = cursor.getColumnIndex("carb");
                // インデックスを元にpfcデータを取得する
                Integer ID = cursor.getInt(idxID);
                double p = cursor.getDouble(idxP);
                double f = cursor.getDouble(idxF);
                double c = cursor.getDouble(idxC);
                int cal = (int) (4 * p + 9 * f + 4 * c);
                //データを全部String型にする
                String idst = ID.toString();
                String itemNamest = cursor.getString(idxIN);
                String pst = p + "g";
                String fst = f + "g";
                String cst = c + "g";
                String calst = cal + "kcal";
                //Mapを作成してListに入れていく
                data = new HashMap<>();
                data.put("id", idst);
                data.put("item_name",itemNamest);
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
        ListView myList = viewML.findViewById(R.id.lv_my_list);
        String[] from = {"cal", "item_name"};
        int[] to = {R.id.tv_cal_value_at_Design_MyList, R.id.tv_item_name_at_Design_MyList};
        SimpleAdapter adapter = new SimpleAdapter(getContext(), dataList, R.layout.design_of_my_list, from, to);
        myList.setAdapter(adapter);
    }


    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
            String idst = item.get("id");
            int _id = Integer.parseInt(idst);

            switch (_id){
                case 0 :
                    Intent intent = new Intent(getContext(), EditMyListActivity.class);
                    startActivity(intent);
                    break;

                default :

                    String itemst = item.get("item_name");
                    String pst = item.get("protein");
                    String fst = item.get("fat");
                    String cst = item.get("carb");

                    //pst、fst,cstには単位gがついているので取り除く
                    double p = Double.parseDouble(pst.replace("g", ""));
                    double f = Double.parseDouble(fst.replace("g", ""));
                    double c = Double.parseDouble(cst.replace("g", ""));
                    //日付け、時間取得
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    String date = dateFormat.format(calendar.getTime());

                    dateFormat = new SimpleDateFormat("H:mm");
                    String time = dateFormat.format(calendar.getTime());

                    //データベース接続オブジェクト取得
                    PFCRecordsDatabaseHelper helper = new PFCRecordsDatabaseHelper(getContext());
                    SQLiteDatabase db = helper.getWritableDatabase();

                    try {
                        String sqlInsert = "INSERT INTO pfcRecords (date, time, protein, fat, carb, comment) VALUES (?, ?, ?, ?, ?, ?)";
                        SQLiteStatement stmt = db.compileStatement(sqlInsert);

                        stmt.bindString(1,date);
                        stmt.bindString(2,time);
                        stmt.bindDouble(3,p);
                        stmt.bindDouble(4,f);
                        stmt.bindDouble(5,c);
                        stmt.bindString(6,itemst);

                        stmt.executeInsert();
                    }
                    finally {
                        db.close();
                    }

                    //今日の記録に遷移
                    Intent intent1 = new Intent(getContext(), RecordsOfTheDayActivity.class);
                    intent1.putExtra("date",date);
                    startActivity(intent1);

                    String msg = itemst + getString(R.string.addToTodaysRecord);
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                    //インタースティシャル広告表示
                    try {
                        AdInterstitial.show(getActivity());
                    }finally {
                        break;
                    }


            }



        }
    }

    //ダイアログバージョン
    private class ListItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);

            String itemIdst = item.get("id");
            int itemId = Integer.parseInt(itemIdst);

            switch (itemId){
                case 0:
                    break;

                default:
                    String itemName = item.get("item_name");
                    String protein = item.get("protein");
                    String fat = item.get("fat");
                    String carb = item.get("carb");
                    String cal = item.get("cal");

                    ItemInfoDialogFragment itemInfoDialogFragment = new ItemInfoDialogFragment(itemIdst, itemName, protein, fat, carb, cal);
                    itemInfoDialogFragment.show(getActivity().getSupportFragmentManager(), "InfoDialogFragment");

            }


            return true;
        }
    }
}


