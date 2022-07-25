package com.test.SimplePFC.ui.main;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.test.SimplePFC.AdInterstitial;
import com.test.SimplePFC.PFCRecordsDatabaseHelper;
import com.test.SimplePFC.R;
import com.test.SimplePFC.RecordsOfTheDayActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ManualInputFragment extends Fragment {

    private View viewIM;
    private ConstraintLayout constraintLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceSate){
        viewIM = inflater.inflate(R.layout.fragment_manual_input, container, false);
        Button addButton = viewIM.findViewById(R.id.btn_add_value_at_FragmentManualInput);
        addButton.setOnClickListener(new addButtonListener());

        //エディットテキストのフォーカス先を設定
        EditText etProtein = viewIM.findViewById(R.id.et_protein_value_at_FragmentManualInput);
        EditText etFat = viewIM.findViewById(R.id.et_fat_value_at_FragmentManualInput);
        EditText etCarb = viewIM.findViewById(R.id.et_carb_value_at_FragmentManualInput);
        etProtein.setNextFocusDownId(R.id.et_fat_value_at_FragmentManualInput);
        etFat.setNextFocusDownId(R.id.et_carb_value_at_FragmentManualInput);
        etCarb.setNextFocusDownId(R.id.et_memo_at_FragmentManualInput);

        //背景タップでキーボード隠す
        constraintLayout = viewIM.findViewById(R.id.constraintLayout_at_FragmentManualInput);
        constraintLayout.setOnTouchListener(new TouchListener());

        return viewIM;

    }

    public void onResume(){
        super.onResume();
        //インタースティシャル広告

    }
    

private class addButtonListener implements View.OnClickListener{
        @Override
    public void onClick(View view){
            //ビュー取得
            EditText etProtein = viewIM.findViewById(R.id.et_protein_value_at_FragmentManualInput);
            EditText etFat = viewIM.findViewById(R.id.et_fat_value_at_FragmentManualInput);
            EditText etCarb = viewIM.findViewById(R.id.et_carb_value_at_FragmentManualInput);
            EditText etMemo = viewIM.findViewById(R.id.et_memo_at_FragmentManualInput);
            String pst = etProtein.getText().toString();
            String fst = etFat.getText().toString();
            String cst = etCarb.getText().toString();
            String memo = etMemo.getText().toString();
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
            //int型に変換
            double p = ((double)Math.round(Double.parseDouble(pst) * 10))/10;
            double f = ((double)Math.round(Double.parseDouble(fst) * 10))/10;
            double c = ((double)Math.round(Double.parseDouble(cst) * 10))/10;

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
                stmt.bindString(6,memo);

                stmt.executeInsert();
            }
            finally {
                db.close();
            }
            //textEditを空に更新
            etProtein.setText(""); etFat.setText(""); etCarb.setText("");
            //今日の記録に遷移
            Intent intent = new Intent(getContext(), RecordsOfTheDayActivity.class);
            intent.putExtra("date", date);
            startActivity(intent);

            //インタースティシャル広告表示
            int i = 0;
            switch (i){
                default:
                    try {
                        AdInterstitial.show(getActivity());
                    }finally {
                        break;
                    }
            }


        }

}

private class TouchListener implements View.OnTouchListener{
        @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
            // キーボードを隠す
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(constraintLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            // 背景にフォーカスを移す
            constraintLayout.requestFocus();
            return true;
        }
}

}
