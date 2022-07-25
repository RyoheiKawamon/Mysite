package com.test.SimplePFC;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.test.SimplePFC.delete.DeleteMyItemActivity;

public class ItemInfoDialogFragment extends DialogFragment {

    public Intent intent;
    public String id;
    private String itemName;
    private String p;
    private String f;
    private String c;
    private String cal;


    public ItemInfoDialogFragment(String id, String itemName, String p, String f, String c, String cal){
        super();
        this.id = id;
        this.itemName = itemName;
        this.p = p;
        this.f = f;
        this.c = c;
        this.cal = cal;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("・" + itemName);
        builder.setMessage("\n" + cal +"\n" + "たんぱく質 : " + p + "\n" + "脂質　　　 : " + f + "\n" + "炭水化物 　: " + c + "\n");


        builder.setPositiveButton(R.string.edit, new DialogButtonClickListener());
        builder.setNeutralButton(R.string.delete_from_my_list, new DialogButtonClickListener());

        AlertDialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.RED);
        }
    }

    private class DialogButtonClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){


            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    intent = new Intent(getContext(), EditMyListActivity.class);
                    intent.putExtra("item_name", itemName);
                    intent.putExtra("protein", p);
                    intent.putExtra("fat", f);
                    intent.putExtra("carb", c);
                    intent.putExtra("cal", cal);
                    intent.putExtra("id", id);
                    intent.putExtra("fromInfoDialogFragment",true);
                    startActivity(intent);

                    break;

                case DialogInterface.BUTTON_NEUTRAL:

                    intent = new Intent(getContext(), DeleteMyItemActivity.class);
                    intent.putExtra("name", itemName);
                    intent.putExtra("id", id);
                    intent.putExtra("table", "マイリスト");

                    startActivity(intent);

                    break;
            }
        }
    }
}
