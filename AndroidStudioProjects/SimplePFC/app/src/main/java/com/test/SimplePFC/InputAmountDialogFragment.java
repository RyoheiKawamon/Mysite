package com.test.SimplePFC;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InputAmountDialogFragment extends DialogFragment {

    public Intent intent;

    public String id;
    private String foodName;
    private String cal;

    private double pPerH;
    private double fPerH;
    private double cPerH;

    public InputAmountDialogFragment(String id, String foodName, String p, String f, String c, String cal){
        super();
        this.id = id;
        this.foodName = foodName;
        this.pPerH = Double.parseDouble(p);
        this.fPerH = Double.parseDouble(f);
        this.cPerH = Double.parseDouble(c);
        this.cal = cal;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        LayoutInflater i = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = i.inflate(R.layout.dialog_fragment_input_amount, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(content);
        builder.setPositiveButton(R.string.to_record, new DialogButtonClickListener());
        builder.setNeutralButton(R.string.back, new DialogButtonClickListener());

        //食べ物の名前とカロリーの情報をセット
        TextView tvFoodName = content.findViewById(R.id.tv_food_name_at_DialogFragmentInputAmount);
        TextView tvCal = content.findViewById(R.id.tv_cal_value_at_DialogFragmentInputAmount);
        tvFoodName.setText(foodName);
        tvCal.setText(cal);

        AlertDialog dialog = builder.create();

        return dialog;
    }

    private class DialogButtonClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){

            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    EditText etg = ((Dialog) dialog).findViewById(R.id.et_amount_at_DialogFragmentInputAmount);
                    String gst = etg.getText().toString();
                    //入力がない時、例外処理をここに追加する
                    if (gst.equals("")){
                        gst = "0";
                    }

                    // コメントセット
                    String comment = foodName + " (" + gst + "g)";

                    // １００gに対する比を算出
                    double g = Double.parseDouble(gst);
                    double amount = g / 100;

                    //pfcの値を算出 pfcの少数第2の位を四捨五入
                    double p = ((double)Math.round(amount * pPerH * 10))/10;
                    double f = ((double)Math.round(amount * fPerH * 10))/10;
                    double c = ((double)Math.round(amount * cPerH * 10))/10;

                    //


                    //テーブルpfcRecordにinsert

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
                        stmt.bindString(6,comment);

                        stmt.executeInsert();
                    }
                    finally {
                        db.close();
                    }

                    //今日の記録に遷移
                    intent = new Intent(getContext(), RecordsOfTheDayActivity.class);
                    intent.putExtra("date", date);
                    startActivity(intent);
                    String msg = comment + getString(R.string.addToTodaysRecord);
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                    //インタースティシャル広告表示
                    try {
                        AdInterstitial.show(getActivity());
                    }finally {
                        break;
                    }

                case DialogInterface.BUTTON_NEUTRAL:

            }
        }
    }
}
