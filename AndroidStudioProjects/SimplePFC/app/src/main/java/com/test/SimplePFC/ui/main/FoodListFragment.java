package com.test.SimplePFC.ui.main;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.test.SimplePFC.EditFoodListActivity;
import com.test.SimplePFC.FoodInfoDialogFragment;
import com.test.SimplePFC.FoodListDatabaseHelper;
import com.test.SimplePFC.InputAmountDialogFragment;
import com.test.SimplePFC.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodListFragment extends Fragment {
    private View viewFL;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceSate){
        viewFL = inflater.inflate(R.layout.fragment_food_list, container, false);

        ListView listView = viewFL.findViewById(R.id.lv_food_list_at_FragmentFoodList);
        listView.setOnItemClickListener(new ListItemClickListener());
        listView.setOnItemLongClickListener(new ListItemLongClickListener());


        return viewFL;
    }

    @Override
    public void onStart(){
        super.onStart();
        createFoodList();
    }

    //リスト作成表示
    public void createFoodList(){
        //リストビューにはめるリスト作成
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> data;
        //マイリスト追加ボタン
        data = new HashMap<>();
        data.put("id", "0");
        data.put("food_name","新しいフードを登録");
        data.put("cal", "      +      ");
        //listに入れる
        dataList.add(data);
        //
        //データベース接続
        FoodListDatabaseHelper helper = new FoodListDatabaseHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sql = "SELECT * FROM foodList";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                //インデックス取得
                int idxID = cursor.getColumnIndex("_id");
                int idxIN = cursor.getColumnIndex("food_name");
                int idxP = cursor.getColumnIndex("protein");
                int idxF = cursor.getColumnIndex("fat");
                int idxC = cursor.getColumnIndex("carb");
                // インデックスを元にpfcデータを取得する
                Integer ID = cursor.getInt(idxID);
                double p = cursor.getDouble(idxP);
                double f = cursor.getDouble(idxF);
                double c = cursor.getDouble(idxC);
                int cal = (int)(4 * p + 9 * f + 4 * c);
                //データを全部String型にする
                String idst = ID.toString();
                String foodNamest = cursor.getString(idxIN);
                String pst = String.valueOf(p);
                String fst = String.valueOf(f);
                String cst = String.valueOf(c);
                String calst = cal + "kcal";
                //Mapを作成してListに入れていく
                data = new HashMap<>();
                data.put("id", idst);
                data.put("food_name",foodNamest);
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
        ListView myList = viewFL.findViewById(R.id.lv_food_list_at_FragmentFoodList);
        String[] from = {"cal", "food_name"};
        int[] to = {R.id.tv_cal_value_at_Design_MyList, R.id.tv_item_name_at_Design_MyList};
        SimpleAdapter adapter = new SimpleAdapter(getContext(), dataList, R.layout.design_of_my_list, from, to);
        myList.setAdapter(adapter);
    }


    // クリックで食べた量の入力画面表示
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){

            Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
            String itemIdst = item.get("id");
            int _id = Integer.parseInt(itemIdst);

            switch (_id){
                case 0:
                    Intent intent = new Intent(getContext(), EditFoodListActivity.class);
                    startActivity(intent);

                    break;
                default:

                    String foodName = item.get("food_name");
                    String protein = item.get("protein");
                    String fat = item.get("fat");
                    String carb = item.get("carb");
                    String cal = item.get("cal");

                    InputAmountDialogFragment inputAmountDialogFragment = new InputAmountDialogFragment(itemIdst,foodName, protein,fat,carb,cal);
                    inputAmountDialogFragment.show(getActivity().getSupportFragmentManager(), "InputAmountDialogFragment");
                    break;
            }


        }
    }
    //ロングクリックでフード情報を表示
    private class  ListItemLongClickListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){

            Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
            String itemIdst = item.get("id");
            int _id = Integer.parseInt(itemIdst);

            switch (_id){
                case 0:
                    Intent intent = new Intent(getContext(), EditFoodListActivity.class);
                    startActivity(intent);

                    break;
                default:

                    String foodName = item.get("food_name");
                    String protein = item.get("protein");
                    String fat = item.get("fat");
                    String carb = item.get("carb");
                    String cal = item.get("cal");

                    FoodInfoDialogFragment foodInfoDialogFragment = new FoodInfoDialogFragment(itemIdst,foodName, protein,fat,carb,cal);
                    foodInfoDialogFragment.show(getActivity().getSupportFragmentManager(), "FoodInfoDialogFragment");
                    break;
            }

            return true;
        }
    }
}

