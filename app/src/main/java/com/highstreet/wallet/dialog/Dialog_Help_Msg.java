package com.highstreet.wallet.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.highstreet.wallet.R;

public class Dialog_Help_Msg extends DialogFragment {

    public static Dialog_Help_Msg newInstance(Bundle bundle) {
        Dialog_Help_Msg frag = new Dialog_Help_Msg();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view  = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_help_msg, null);
        TextView title = view.findViewById(R.id.dialog_title);
        TextView msg = view.findViewById(R.id.dialog_msg);
        title.setText(getArguments().getString("title"));
        msg.setText(getArguments().getString("msg"));

        Button btn_negative = view.findViewById(R.id.btn_nega);
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }
}