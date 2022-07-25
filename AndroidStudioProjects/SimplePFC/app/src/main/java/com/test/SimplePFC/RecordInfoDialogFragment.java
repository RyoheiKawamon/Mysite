package com.test.SimplePFC;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.test.SimplePFC.delete.DeleteRecordActivity;

public class RecordInfoDialogFragment extends DialogFragment {

    public Intent intent;
    public String id;
    private String itemName;
    private String p;
    private String f;
    private String c;
    private String cal;
    private String time;


    public RecordInfoDialogFragment(String id, String itemName, String p, String f, String c, String cal, String time){
        super();
        this.id = id;
        this.itemName = itemName;
        this.p = p;
        this.f = f;
        this.c = c;
        this.cal = cal;
        this.time = time;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("・" + itemName);
        builder.setMessage( time + "\n" + "\n"  + cal +"\n" + "たんぱく質 : " + p + "\n" + "脂質　　　 : " + f + "\n" + "炭水化物 　: " + c + "\n" );


        builder.setPositiveButton(R.string.addToMyList, new DialogButtonClickListener());
        builder.setNeutralButton(R.string.delete_from_the_record, new DialogButtonClickListener());


        AlertDialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.delete_red));
            //alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.YELLOW);
            //alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.textColor));
        }
    }

    private class DialogButtonClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){


            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //マイリスト登録画面へ遷移　gを消す
                    intent = new Intent(getContext(), EditMyListActivity.class);
                    intent.putExtra("item_name", itemName);
                    intent.putExtra("protein", p.replace("g", ""));
                    intent.putExtra("fat", f.replace("g", ""));
                    intent.putExtra("carb", c.replace("g", ""));
                    startActivity(intent);
                    break;

                case DialogInterface.BUTTON_NEUTRAL:

                    //削除画面へ遷移
                    intent = new Intent(getContext(), DeleteRecordActivity.class);
                    intent.putExtra("name", itemName);
                    intent.putExtra("id", id);
                    intent.putExtra("table", "今日の記録");

                    startActivity(intent);


                    break;
            }
        }
    }
}