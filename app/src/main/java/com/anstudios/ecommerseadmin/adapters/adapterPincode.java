package com.anstudios.ecommerseadmin.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.ecommerseadmin.Constants;
import com.anstudios.ecommerseadmin.R;
import com.anstudios.ecommerseadmin.models.modelPincode;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class adapterPincode extends RecyclerView.Adapter<adapterPincode.viewHolder> {

    private final Context context;
    private final ArrayList<modelPincode> arrayList;

    public adapterPincode(Context context, ArrayList<modelPincode> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.layout_pincodes_availiblity, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("We are getting all products.");
        progressDialog.setCanceledOnTouchOutside(false);
        holder.pincode.setText(arrayList.get(position).getPincode());
        holder.charge.setText(Constants.CURRENCY_SIGN.concat(arrayList.get(position).getDeliveryCharge()));
        holder.editBtn.setOnClickListener(v -> {
            try {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View vobj = LayoutInflater.from(context).inflate(R.layout.layout_add_pincodes, null);
                builder.setView(vobj);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                EditText pincodeDialog = vobj.findViewById(R.id.dialog_pincode_add);
                EditText priceDialog = vobj.findViewById(R.id.dialog_pincode_charge);
                TextView cancelBtn = vobj.findViewById(R.id.dialog_pincode_cancel_btn);
                TextView saveBtn = vobj.findViewById(R.id.dialog_pincode_save_btn);
                CheckBox codavail = vobj.findViewById(R.id.dialog_pincode_checkbox);
                pincodeDialog.setText(arrayList.get(position).getPincode());
                priceDialog.setText(Constants.CURRENCY_SIGN.concat(arrayList.get(position).getDeliveryCharge()));
                if (arrayList.get(position).getCodAvailable().equals("true")) {
                    codavail.setChecked(true);
                }
                codavail.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        codavail.setText(context.getString(R.string.availbale));
                    } else {
                        codavail.setText(context.getString(R.string.NOT_AVAILBLE));
                    }
                });
                saveBtn.setOnClickListener(v1 -> {
                    if (!pincodeDialog.getText().toString().isEmpty() &&
                            !priceDialog.getText().toString().isEmpty()) {
                        progressDialog.show();
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("deliveryCharge", priceDialog.getText().toString());
                        if (codavail.isChecked()) {
                            hashMap.put("codAvailable", "true");
                        } else {
                            hashMap.put("codAvailable", "false");
                        }
                        FirebaseDatabase.getInstance().getReference("pincodes")
                                .child(pincodeDialog.getText().toString())
                                .setValue(hashMap).addOnSuccessListener(aVoid -> {
                                    alertDialog.cancel();
                                    progressDialog.cancel();
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(context, "Blank fields cannot be processed", Toast.LENGTH_SHORT).show();
                    }

                });
                cancelBtn.setOnClickListener(v12 -> alertDialog.cancel());
                alertDialog.show();
            } catch (Exception exception) {
                Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
        if (arrayList.get(position).getCodAvailable().equals("true")) {
            holder.codAvailable.setChecked(true);
        }
        holder.codAvailable.setClickable(false);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        private final TextView pincode;
        private final TextView charge;
        private final CardView editBtn;
        private final CheckBox codAvailable;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            pincode = itemView.findViewById(R.id.layout_pincode_delivery_pincode);
            charge = itemView.findViewById(R.id.layout_pincode_delivery_charge);
            editBtn = itemView.findViewById(R.id.layout_pincode_edit_btn);
            codAvailable = itemView.findViewById(R.id.layout_pincode_cod_checkbox);
        }
    }
}
