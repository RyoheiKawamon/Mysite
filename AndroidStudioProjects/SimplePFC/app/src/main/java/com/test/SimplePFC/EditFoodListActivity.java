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

public class EditFoodListActivity extends AppCompatActivity {

    public static Intent intent;
    public static boolean isFromIADF;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food_list);

        constraintLayout = findViewById(R.id.constraintLayout_at_edit_food_activity);
        intent = getIntent();

        //ビュー取得
        EditText etFoodName = findViewById(R.id.et_food_name_at_ActivityEditFoodList);
        EditText etProtein = findViewById(R.id.et_p_value_at_ActivityEditFoodList);
        EditText etFat = findViewById(R.id.et_f_value_at_ActivityEditFoodList);
        EditText etCarb = findViewById(R.id.et_c_value_at_ActivityEditFoodList);

        //intentから値取得
        String foodName = intent.getStringExtra("food_name");
        String protein = intent.getStringExtra("protein");
        String fat = intent.getStringExtra("fat");
        String carb = intent.getStringExtra("carb");
         isFromIADF = intent.getBooleanExtra("fromIADF",false);

        //セット
        etFoodName.setText(foodName);
        etProtein.setText(protein);
        etFat.setText(fat);
        etCarb.setText(carb);

        if (isFromIADF){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("フードの編集");
        }
    }
    //登録ボタンタップ処理
    public void onRegisterButtonClick (View view){
        // 旧アイテム削除

        if(isFromIADF){
            deleteForUpdate();
        }

        //ビュー取得
        EditText etFoodName = findViewById(R.id.et_food_name_at_ActivityEditFoodList);
        EditText etAmount = findViewById(R.id.et_amount_at_ActivityEditFoodList);
        EditText etProtein = findViewById(R.id.et_p_value_at_ActivityEditFoodList);
        EditText etFat = findViewById(R.id.et_f_value_at_ActivityEditFoodList);
        EditText etCarb = findViewById(R.id.et_c_value_at_ActivityEditFoodList);
        //文字列取得
        String foodNamest = etFoodName.getText().toString();
        String ast = etAmount.getText().toString();
        String pst = etProtein.getText().toString();
        String fst = etFat.getText().toString();
        String cst = etCarb.getText().toString();

        //gあたりの量の値が0になっていないか判定
        if (ast.equals("")){
            ast = "100";
        }

        int a =Integer.parseInt(ast);

        switch (a){
            case 0:
               //不正な値が入力されていますダイアログ
                break;

            default:

                //アイテム名がの空判定
                if (foodNamest.equals("")){
                    foodNamest = "名無しのフード";
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


                //１００gあたりのpfc値に変換
                double b = 100 / (double)a;
                double p = ((double)Math.round((( Double.parseDouble(pst) ) * b)* 10))/10;
                double f = ((double)Math.round((( Double.parseDouble(fst) ) * b)* 10))/10;
                double c = ((double)Math.round((( Double.parseDouble(cst) ) * b)* 10))/10;


                //データベース接続オブジェクト取得
                FoodListDatabaseHelper helper = new FoodListDatabaseHelper(EditFoodListActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                try {
                    String sqlInsert = "INSERT INTO foodList (food_name, protein, fat, carb) VALUES (?, ?, ?, ?)";
                    SQLiteStatement stmt = db.compileStatement(sqlInsert);

                    stmt.bindString(1,foodNamest);
                    stmt.bindDouble(2,p);
                    stmt.bindDouble(3,f);
                    stmt.bindDouble(4,c);

                    stmt.executeInsert();
                }
                finally {
                    db.close();
                }
                //編集画面をfinish
                finish();

                break;
        }




    }
    //旧アイテムの削除
    private void deleteForUpdate(){
        String ID = intent.getStringExtra("id");
        int id = Integer.parseInt(ID);

        FoodListDatabaseHelper helper = new FoodListDatabaseHelper(EditFoodListActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sqlDelete = "DELETE FROM foodList WHERE _id = ?";
            SQLiteStatement stmt = db.compileStatement(sqlDelete);
            stmt.bindLong(1,id);
            stmt.executeUpdateDelete();
        }
        finally {
            db.close();
        }
    }

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
