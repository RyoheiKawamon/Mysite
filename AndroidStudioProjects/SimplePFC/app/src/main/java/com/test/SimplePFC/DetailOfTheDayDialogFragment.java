package com.test.SimplePFC;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;


public class DetailOfTheDayDialogFragment extends DialogFragment {
    public Intent intent;
    private String p;
    private String f;
    private String c;
    private String cal;
    private String date;


    public DetailOfTheDayDialogFragment(String p, String f, String c, String cal, String date){
        super();
        this.p = p;
        this.f = f;
        this.c = c;
        this.cal = cal;
        this.date = date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(date);
        builder.setMessage(cal +"\n" + "たんぱく質 : " + p + "\n" + "脂質　　　 : " + f + "\n" + "炭水化物 　: " + c );


        builder.setPositiveButton(R.string.detail, new DialogButtonClickListener());


        AlertDialog dialog = builder.create();
        return dialog;
    }

    private class DialogButtonClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){

            if(which == DialogInterface.BUTTON_POSITIVE){
                  //この日の詳細へ

                Intent intent = new Intent(getContext(), RecordsOfTheDayActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);

            }
        }
    }
}
