package com.test.SimplePFC.delete;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.view.View;
import android.widget.Toast;

import com.test.SimplePFC.MyListDatabaseHelper;

public class DeleteMyItemActivity extends AbstractDeleteActivity {

    @Override
    public void OnDeleteButtonClick(View view){
        int idint = Integer.parseInt(id);
        MyListDatabaseHelper helper = new MyListDatabaseHelper(DeleteMyItemActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sqlDelete = "DELETE FROM myList WHERE _id = ?";
            SQLiteStatement stmt = db.compileStatement(sqlDelete);
            stmt.bindLong(1,idint);
            stmt.executeUpdateDelete();
        }
        finally {
            db.close();
        }

        String msg = name + "を"+ table + "から削除しました";
        Toast.makeText(DeleteMyItemActivity.this, msg, Toast.LENGTH_LONG).show();

        finish();

    }
}
