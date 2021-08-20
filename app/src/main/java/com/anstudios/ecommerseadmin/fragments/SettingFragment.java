package com.anstudios.ecommerseadmin.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.anstudios.ecommerseadmin.R;

public class SettingFragment extends Fragment {
    private View view;
    private ImageView editDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        editDialog = view.findViewById(R.id.edit_dialog_btn);
        editDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View vobj = LayoutInflater.from(getContext()).inflate(R.layout.layout_edit_details, null);
                builder.setView(vobj);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        });
        return view;
    }
}