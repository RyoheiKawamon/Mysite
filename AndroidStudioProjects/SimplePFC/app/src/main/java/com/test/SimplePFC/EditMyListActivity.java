package com.test.SimplePFC;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditMyListActivity extends AppCompatActivity {

    public static String ItemName;
    public static int protein;
    public static int fat;
    public static int carb;
    public static Intent intent;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_list);

        constraintLayout = findViewById(R.id.constraintLayout_at_edit_my_list_activity);

        intent = getIntent();

        //ビュー取得
        EditText etMyItem = findViewById(R.id.et_my_item_at_ActivityEditMyList);
        EditText etProtein = findViewById(R.id.et_p_value_at_ActivityEditMyList);
        EditText etFat = findViewById(R.id.et_f_value_at_ActivityEditMyList);
        EditText etCarb = findViewById(R.id.et_c_value_at_ActivityEditMyList);
        TextView tvTitle = findViewById(R.id.tv_title_at_ActivityEditMyList);

        //intentから値取得
        String itemName = intent.getStringExtra("item_name");
        String protein = intent.getStringExtra("protein");
        String fat = intent.getStringExtra("fat");
        String carb = intent.getStringExtra("carb");
        boolean fromInfoDialogFragment = intent.getBooleanExtra("fromInfoDialogFragment",false);

        //セット
        etMyItem.setText(itemName);
        etProtein.setText(protein);
        etFat.setText(fat);
        etCarb.setText(carb);
        //タイトルセット
        if (fromInfoDialogFragment){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("マイアイテムの編集");
        }


    }
    //登録ボタンタップ処理
    public void onRegisterButtonClick (View view){
        // 旧アイテム削除

        if(intent.getBooleanExtra("fromInfoDialogFragment", false)){
           deleteForUpdate();
        }

        //ビュー取得
        EditText etMyItem = findViewById(R.id.et_my_item_at_ActivityEditMyList);
        EditText etProtein = findViewById(R.id.et_p_value_at_ActivityEditMyList);
        EditText etFat = findViewById(R.id.et_f_value_at_ActivityEditMyList);
        EditText etCarb = findViewById(R.id.et_c_value_at_ActivityEditMyList);
        //文字列取得
        String ist = etMyItem.getText().toString();
        String pst = etProtein.getText().toString();
        String fst = etFat.getText().toString();
        String cst = etCarb.getText().toString();

        //アイテム名がの空判定
        if (ist.equals("")){
            ist = "名無しのアイテム";
        }

        //入力欄の空の場合、０を補充する。
        if (pst.equals("")){
            pst = "0";
        }
        if (fst.equals("")){
            fst = "0";
        }
        if (cst.equals("")){
            cst = "0";
        }
        //double型に変換
        double p = ((double)Math.round(Double.parseDouble(pst) * 10))/10;
        double f = ((double)Math.round(Double.parseDouble(fst) * 10))/10;
        double c = ((double)Math.round(Double.parseDouble(cst) * 10))/10;

        //データベース接続オブジェクト取得
        MyListDatabaseHelper helper = new MyListDatabaseHelper(EditMyListActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            String sqlInsert = "INSERT INTO myList (item_name, protein, fat, carb) VALUES (?, ?, ?, ?)";
            SQLiteStatement stmt = db.compileStatement(sqlInsert);

            stmt.bindString(1,ist);
            stmt.bindDouble(2,p);
            stmt.bindDouble(3,f);
            stmt.bindDouble(4,c);

            stmt.executeInsert();
        }
        finally {
            db.close();
        }

        String msg = ist + getString(R.string.executed_add_to_my_list);
        Toast.makeText(EditMyListActivity.this, msg, Toast.LENGTH_LONG).show();

        //編集画面をfinish
        finish();
    }

    //旧アイテムの削除
    private void deleteForUpdate(){
        String ID = intent.getStringExtra("id");
        int id = Integer.parseInt(ID);

        MyListDatabaseHelper helper = new MyListDatabaseHelper(EditMyListActivity.this);
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
    }

    //バックボタン
    public void onBackButtonClick(View view){
        finish();
    }

    //背景タッチでキーボード隠す
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // キーボードを隠す
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(constraintLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // 背景にフォーカスを移す
        constraintLayout.requestFocus();

        return true;
    }
}