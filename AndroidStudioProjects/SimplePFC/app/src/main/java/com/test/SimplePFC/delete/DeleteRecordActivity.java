package com.test.SimplePFC.delete;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.view.View;
import android.widget.Toast;

import com.test.SimplePFC.PFCRecordsDatabaseHelper;

public class DeleteRecordActivity extends AbstractDeleteActivity {

    @Override
    public void OnDeleteButtonClick(View view){
        int idint = Integer.parseInt(id);
        PFCRecordsDatabaseHelper helper = new PFCRecordsDatabaseHelper(DeleteRecordActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sqlDelete = "DELETE FROM pfcRecords WHERE _id = ?";
            SQLiteStatement stmt = db.compileStatement(sqlDelete);
            stmt.bindLong(1,idint);
            stmt.executeUpdateDelete();
        }
        finally {
            db.close();
        }

        String msg = name + "を"+ table + "から削除しました";
        Toast.makeText(DeleteRecordActivity.this, msg, Toast.LENGTH_LONG).show();

        finish();

    }

}