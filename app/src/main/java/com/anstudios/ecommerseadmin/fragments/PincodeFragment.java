package com.anstudios.ecommerseadmin.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.ecommerseadmin.R;
import com.anstudios.ecommerseadmin.adapters.adapterPincode;
import com.anstudios.ecommerseadmin.models.modelPincode;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class PincodeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private CardView addProductBtn;
    private ArrayList<modelPincode> arrayList;
    private ProgressDialog progressDialog;
    private adapterPincode adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pincode, container, false);
        recyclerView = view.findViewById(R.id.pincode_recycler_view);
        arrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("We are getting alll products.");
        progressDialog.setCanceledOnTouchOutside(false);
        addProductBtn = view.findViewById(R.id.add_pincode_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new adapterPincode(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        getDataPincode();
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View vobj = LayoutInflater.from(getContext()).inflate(R.layout.layout_add_pincodes, null);
                    builder.setView(vobj);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    EditText pincode = vobj.findViewById(R.id.dialog_pincode_add);
                    EditText price = vobj.findViewById(R.id.dialog_pincode_charge);
                    CheckBox codavail = vobj.findViewById(R.id.dialog_pincode_checkbox);
                    TextView cancelBtn = vobj.findViewById(R.id.dialog_pincode_cancel_btn);
                    TextView saveBtn = vobj.findViewById(R.id.dialog_pincode_save_btn);
                    codavail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                codavail.setText("Available");
                            } else {
                                codavail.setText("Not Available");
                            }
                        }
                    });
                    saveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!pincode.getText().toString().isEmpty() &&
                                    !price.getText().toString().isEmpty()) {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("deliveryCharge", price.getText().toString());
                                if (codavail.isChecked()) {
                                    hashMap.put("codAvailable", "true");
                                } else {
                                    hashMap.put("codAvailable", "false");
                                }
                                FirebaseDatabase.getInstance().getReference("pincodes")
                                        .child(pincode.getText().toString())
                                        .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        alertDialog.cancel();
                                        Toast.makeText(getContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                                        getDataPincode();
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Blank fields cannot be processed.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });
                    alertDialog.show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }


    private void getDataPincode() {
        progressDialog.show();
        arrayList.clear();
        FirebaseDatabase.getInstance().getReference("pincodes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, HashMap<String, String>> main = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                    HashMap<String, String> child;
                    for (int i = 0; i < main.size(); i++) {
                        child = main.get(main.keySet().toArray()[i]);
                        arrayList.add(new modelPincode(main.keySet().toArray()[i] + "", child.get("codAvailable"), child.get("deliveryCharge")));
                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.cancel();
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