package com.test.SimplePFC.delete;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.test.SimplePFC.R;

public abstract class AbstractDeleteActivity extends AppCompatActivity {

    String id;
    String name;
    String table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract_delete);

        Intent intent = getIntent();
        //遷移もとでこの３つを設定する
        name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");
        table = intent.getStringExtra("table");

        TextView tv = findViewById(R.id.tv_delete_question);
        String deleteQuestion = " 「" + name + "」を" + table + "から削除しますか？";
        tv.setText(deleteQuestion);

    }

    public void OnBackButtonClick(View view) {
        finish();
    }

    public abstract void OnDeleteButtonClick(View view);
    //削除の実行
}