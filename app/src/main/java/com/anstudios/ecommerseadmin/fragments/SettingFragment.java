package com.anstudios.ecommerseadmin.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anstudios.ecommerseadmin.R;
import com.anstudios.ecommerseadmin.SplashScreen;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SettingFragment extends Fragment {
    private View view;
    private TextView contactEmail, contactAbout, contactStoreName, contactAddress;
    private TextView email, name;
    private ImageView editDialog;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        editDialog = view.findViewById(R.id.edit_dialog_btn);
        contactAbout = view.findViewById(R.id.settings_about);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait while we are fetching the data.");
        progressDialog.setCanceledOnTouchOutside(false);
        contactAddress = view.findViewById(R.id.settings_store_address);
        contactStoreName = view.findViewById(R.id.setiings_store_name);
        contactEmail = view.findViewById(R.id.setting_contact_email);
        email = view.findViewById(R.id.settings_email_address);
        name = view.findViewById(R.id.settings_admin_name);
        editDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View vobj = LayoutInflater.from(getContext()).inflate(R.layout.layout_edit_details, null);
                builder.setView(vobj);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                EditText dialogName = vobj.findViewById(R.id.dialog_edit_details_store_name);
                EditText dialogEmail = vobj.findViewById(R.id.dialog_edit_details_email);
                EditText dialogAddress = vobj.findViewById(R.id.dialog_edit_details_store_address);
                EditText dialogAbout = vobj.findViewById(R.id.dialog_edit_details_about);
                TextView dialogCancel = vobj.findViewById(R.id.dialog_edit_details_cancel_btn);
                TextView dialogSave = vobj.findViewById(R.id.dialog_edit_details_save_btn);
                dialogSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.show();
                        HashMap<String,String> hashMap=new HashMap<>();
                        hashMap.put("about",dialogAbout.getText().toString());
                        hashMap.put("storeAddress",dialogAddress.getText().toString());
                        hashMap.put("storeName",dialogName.getText().toString());
                        hashMap.put("storeEmail",dialogEmail.getText().toString());
                        FirebaseDatabase.getInstance().getReference("storeInfo")
                                .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.cancel();
                                alertDialog.cancel();
                                contactStoreName.setText(hashMap.get("storeName"));
                                contactEmail.setText(hashMap.get("storeEmail"));
                                contactAddress.setText(hashMap.get("storeAddress"));
                                contactAbout.setText(hashMap.get("about"));
                                Toast.makeText(getContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                alertDialog.show();
            }
        });
        getDataSettings();
        return view;
    }

    private void getDataSettings() {
        progressDialog.show();
        name.setText(SplashScreen.sharedPreferences.getString("name", "John Doe"));
        email.setText(SplashScreen.sharedPreferences.getString("email", "johndoe@gmail.com"));
        FirebaseDatabase.getInstance().getReference("storeInfo")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.cancel();
                        if (snapshot.exists()) {
                            HashMap<String, String> hashMap = (HashMap<String, String>) snapshot.getValue();
                            if (hashMap.containsKey("about")) {
                                contactAbout.setText(hashMap.get("about"));
                            }
                            if (hashMap.containsKey("storeAddress")) {
                                contactAddress.setText(hashMap.get("storeAddress"));
                            }
                            if (hashMap.containsKey("storeName")) {
                                contactStoreName.setText(hashMap.get("storeName"));
                            }
                            if (hashMap.containsKey("storeEmail")) {
                                contactEmail.setText(hashMap.get("storeEmail"));
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.cancel();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}