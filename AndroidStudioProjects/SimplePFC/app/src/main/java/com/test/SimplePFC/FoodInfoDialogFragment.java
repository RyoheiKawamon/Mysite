package com.test.SimplePFC;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.test.SimplePFC.delete.DeleteFoodActivity;

public class FoodInfoDialogFragment extends DialogFragment {

    public Intent intent;
    public String id;
    private String foodName;
    private String p;
    private String f;
    private String c;
    private String cal;

    public FoodInfoDialogFragment(String id, String foodName, String p, String f, String c, String cal){
        super();
        this.id = id;
        this.foodName = foodName;
        this.p = p;
        this.f = f;
        this.c = c;
        this.cal = cal;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        LayoutInflater i = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = i.inflate(R.layout.dialog_fragment_food_info, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(content);
        builder.setPositiveButton(R.string.edit, new DialogButtonClickListener());
        builder.setNeutralButton(R.string.delete, new DialogButtonClickListener());

        //pfc情報をセット
        TextView tvFoodName = content.findViewById(R.id.tv_food_name_at_DialogFragmentFoodInfo);
        TextView tvCal = content.findViewById(R.id.tv_cal_value_at_DialogFragmentFoodInfo);
        TextView tvProtein = content.findViewById(R.id.tv_protein_value_at_DialogFragmentFoodInfo);
        TextView tvFat = content.findViewById(R.id.tv_fat_value_at_DialogFragmentFoodInfo);
        TextView tvCarb = content.findViewById(R.id.tv_carb_value_at_DialogFragmentFoodInfo);

        tvFoodName.setText(foodName);
        tvCal.setText(cal);
        tvProtein.setText(p + "g");
        tvFat.setText(f + "g");
        tvCarb.setText(c + "g");

        //


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

                    intent = new Intent(getContext(), EditFoodListActivity.class);
                    intent.putExtra("food_name", foodName);
                    intent.putExtra("protein", p);
                    intent.putExtra("fat", f);
                    intent.putExtra("carb", c);
                    intent.putExtra("cal", cal);
                    intent.putExtra("id", id);
                    intent.putExtra("fromIADF",true);
                    startActivity(intent);

                    break;

                case DialogInterface.BUTTON_NEUTRAL:

                    intent = new Intent(getContext(), DeleteFoodActivity.class);
                    intent.putExtra("name", foodName);
                    intent.putExtra("id", id);
                    intent.putExtra("table", "フードリスト");
                    startActivity(intent);

                    break;

            }
        }
    }
}
