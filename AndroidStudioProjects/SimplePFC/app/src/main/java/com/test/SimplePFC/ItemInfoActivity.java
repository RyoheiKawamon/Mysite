package com.test.SimplePFC;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ItemInfoActivity extends AppCompatActivity {

    private static String itemName;
    private static int id;
    public static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        intent = getIntent();


        String itemName = intent.getStringExtra("item_name");
        String protein = intent.getStringExtra("protein");
        String fat = intent.getStringExtra("fat");
        String carb = intent.getStringExtra("carb");
        String cal = intent.getStringExtra("cal");
        String ID =  intent.getStringExtra("id");

        ItemInfoActivity.itemName = itemName;
        id = Integer.parseInt(ID);

        TextView tvItemName = findViewById(R.id.tv_item_name_at_ActivityMyItemInfo);
        TextView tvProtein = findViewById(R.id.tv_protein_value_at_ActivityMyItemInfo);
        TextView tvFat = findViewById(R.id.tv_fat_value_at_ActivityMyItemInfo);
        TextView tvCarb = findViewById(R.id.tv_carb_value_at_ActivityMyItemInfo);
        TextView tvCal = findViewById(R.id.tv_cal_value_at_ActivityMyItemInfo);

        tvItemName.setText(itemName);
        tvProtein.setText(protein + "g");
        tvFat.setText(fat + "g");
        tvCarb.setText(carb + "g");
        tvCal.setText(cal);

    }
    public void onRestart(){
        super.onRestart();
        if (Flag.getFinishing()){
            finish();
        }

    }

    //戻るボタン
    public void onBackButtonClick(View view){
        finish();
    }
    //編集するボタン
    public void onEditButtonClick(View view){
        intent.setClass(ItemInfoActivity.this, EditMyListActivity.class);
        intent.putExtra("fromMyItemInfo", true);
        startActivity(intent);
    }
    public void onDeleteFromMyList (View view){
        MyListDatabaseHelper helper = new MyListDatabaseHelper(ItemInfoActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sqlDelete = "DELETE FROM myList WHERE _id = ?";
            SQLiteStatement stmt = db.compileStatement(sqlDelete);
            stmt.bindLong(1,id);
            stmt.executeUpdateDelete();
        }
        finally {
            db.close();
        }

        //削除しましたトースト
        String msg = itemName + getString(R.string.deleted);
        Toast.makeText(ItemInfoActivity.this, msg, Toast.LENGTH_LONG).show();

        finish();
    }
}