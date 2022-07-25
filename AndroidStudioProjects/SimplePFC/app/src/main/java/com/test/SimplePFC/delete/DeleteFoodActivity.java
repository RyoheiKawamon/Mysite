package com.test.SimplePFC.delete;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.view.View;
import android.widget.Toast;

import com.test.SimplePFC.FoodListDatabaseHelper;
import com.test.SimplePFC.R;

public class DeleteFoodActivity extends AbstractDeleteActivity {
    /*
    String id;
    String foodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_food);

        Intent intent = getIntent();
        foodName = intent.getStringExtra("food_name");
        id = intent.getStringExtra("id");

        TextView tv = findViewById(R.id.tv_delete_question);
        String deleteQuestion = " 「" + foodName + "」を食べ物リストから削除しますか？" ;
        tv.setText(deleteQuestion);
    }
    public void onBackButtonClick(View view){
        finish();
    }*/
    @Override
    public void OnDeleteButtonClick(View view){
        int idint = Integer.parseInt(id);
        FoodListDatabaseHelper helper = new FoodListDatabaseHelper(DeleteFoodActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sqlDelete = "DELETE FROM foodList WHERE _id = ?";
            SQLiteStatement stmt = db.compileStatement(sqlDelete);
            stmt.bindLong(1,idint);
            stmt.executeUpdateDelete();
        }
        finally {
            db.close();
        }

        String msg = name + getString(R.string.deleted);
        Toast.makeText(DeleteFoodActivity.this, msg, Toast.LENGTH_LONG).show();

        finish();
    }
}