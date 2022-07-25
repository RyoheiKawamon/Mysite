package com.test.SimplePFC;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SumMaker {
    private double pSum = 0;
    private double fSum = 0;
    private double cSum = 0;
    private int calSum = 0;

    public SumMaker(Context context, String date){
        String[] dateAry =  { date };
        //データベース接続
        PFCRecordsDatabaseHelper helper = new PFCRecordsDatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sql = "SELECT SUM(protein), SUM(fat), SUM(carb), date FROM pfcRecords WHERE date = ? GROUP BY date";
            Cursor cursor = db.rawQuery(sql,dateAry);
            while (cursor.moveToNext()){
                //インデックス取得
                int idxP = cursor.getColumnIndex("SUM(protein)");
                int idxF = cursor.getColumnIndex("SUM(fat)");
                int idxC = cursor.getColumnIndex("SUM(carb)");
                // インデックスを元にpfcデータを取得する
                pSum = ((double)Math.round(cursor.getDouble(idxP) * 10))/10;
                fSum = ((double)Math.round(cursor.getDouble(idxF) * 10))/10;
                cSum = ((double)Math.round(cursor.getDouble(idxC) * 10))/10;
            }
        }
        finally {
            db.close();
        }
        calSum = (int)(4 * pSum + 9 * fSum + 4 * cSum);
    }
    public double getPSum(){
        return pSum;
    }
    public double getFSum(){
        return fSum;
    }
    public double getCSum(){
        return cSum;
    }
    public int getCalSum(){
        return calSum;
    }
}
