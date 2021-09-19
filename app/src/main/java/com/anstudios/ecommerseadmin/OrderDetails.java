package com.anstudios.ecommerseadmin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.ecommerseadmin.adapters.adapterEditDetails;
import com.anstudios.ecommerseadmin.models.modelEditDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class OrderDetails extends AppCompatActivity {

    String status;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView order_deatilsTxt;
    private TextView addressCustomer;
    private TextView phoneNumber;
    private TextView orderId;
    private OrdersObject ordersObject;
    private EditText customerMessage;
    private TextView orderedProductsTxt;
    private TextView statusOfOrder;
    private ConstraintLayout orderDetails;
    private ConstraintLayout orderDetailsBtn, orderProductsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        try {
            recyclerView = findViewById(R.id.order_details_recycler);
            orderDetailsBtn = findViewById(R.id.orderDetailsBtn);
            progressBar = findViewById(R.id.oredr_details_progress_bar);
            TextView paymentMode = findViewById(R.id.order_details_paymentMode);
            orderId = findViewById(R.id.order_details_orderId);
            customerMessage = findViewById(R.id.customer_mesage);
            orderedProductsTxt = findViewById(R.id.order_products_txt);
            orderDetails = findViewById(R.id.order_details_layout);
            addressCustomer = findViewById(R.id.order_details_address);
            phoneNumber = findViewById(R.id.mobile_order_details);
            TextView price = findViewById(R.id.order_details_price);
            statusOfOrder = findViewById(R.id.orderDetails_status);
            TextView date = findViewById(R.id.order_details_date);
            order_deatilsTxt = findViewById(R.id.orderedDetailsTxt);
            ordersObject = (OrdersObject) getIntent().getSerializableExtra("OrdersObject");
            statusOfOrder.setText(ordersObject.getStatus());
            date.setText(ordersObject.getTimeStamp());
            price.setText(Constants.CURRENCY_SIGN.concat(ordersObject.getTotalPrice()));
            paymentMode.setText(ordersObject.getPaymentType());
            orderId.setText("Order Id: #" + ordersObject.getIndex());
            orderProductsBtn = findViewById(R.id.order_produts_btn);
            ArrayList<modelEditDetails> arrayList = new ArrayList<>();
            adapterEditDetails adapter = new adapterEditDetails(this, arrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            orderProductsBtn.setOnClickListener(v -> {
                recyclerView.setVisibility(View.VISIBLE);
                orderDetails.setVisibility(View.INVISIBLE);
                orderedProductsTxt.setTextColor(ContextCompat.getColor(OrderDetails.this, R.color.white));
                order_deatilsTxt.setTextColor(ContextCompat.getColor(OrderDetails.this, R.color.apptheme));
                orderDetailsBtn.setBackgroundColor(ContextCompat.getColor(OrderDetails.this, R.color.white));
                orderProductsBtn.setBackgroundColor(ContextCompat.getColor(OrderDetails.this, R.color.apptheme));
            });
            orderDetailsBtn.setOnClickListener(v -> {
                recyclerView.setVisibility(View.INVISIBLE);
                orderedProductsTxt.setTextColor(ContextCompat.getColor(OrderDetails.this, R.color.apptheme));
                orderDetails.setVisibility(View.VISIBLE);
                order_deatilsTxt.setTextColor(ContextCompat.getColor(OrderDetails.this, R.color.white));
                orderDetailsBtn.setBackgroundColor(ContextCompat.getColor(OrderDetails.this, R.color.apptheme));
                orderProductsBtn.setBackgroundColor(ContextCompat.getColor(OrderDetails.this, R.color.white));
            });
            HashMap<String, HashMap<String, String>> hashMap = ordersObject.getProducts();
            try {
                for (int i = 0; i < hashMap.size(); i++) {
                    HashMap<String, String> child = hashMap.get(hashMap.keySet().toArray()[i]);
                    arrayList.add(new modelEditDetails(child.get("smallImage"), child.get("title"), child.get("measuringUnit"), child.get("quantity"), child.get("price")));
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        getAddress();
    }

    public void sendMessageToCustomer(View view) {
        if (!customerMessage.getText().toString().equals("")) {
            sendAndUploadNotification(customerMessage.getText().toString());
        } else {
            Toast.makeText(this, "The Message is blank", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendAndUploadNotification(String body) {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> hashMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | HH:mm", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        hashMap.put("title", body);
        hashMap.put("date", currentDateandTime);
        FirebaseDatabase.getInstance().getReference("notifications").child(getIntent().getStringExtra("customerUid"))
                .push().setValue(hashMap).addOnSuccessListener(aVoid -> {
            progressBar.setVisibility(View.INVISIBLE);
            try {
                FirebaseDatabase.getInstance().getReference("users")
                        .child(getIntent().getStringExtra("customerUid"))
                        .child("fcmToken")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                progressBar.setVisibility(View.VISIBLE);
                                String fcmToke = (String) snapshot.getValue();
                                JSONObject notificationObj = new JSONObject();
                                RequestQueue mRequestQue = Volley.newRequestQueue(OrderDetails.this);
                                JSONObject json = new JSONObject();
                                try {
                                    json.put("to", fcmToke);
                                    notificationObj.put("title", "Order Updates");
                                    notificationObj.put("body", body);
                                    //replace notification with data when went send data
                                    json.put("notification", notificationObj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                String URL = "https://fcm.googleapis.com/fcm/send";
                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                                        json,
                                        response -> successMethod(),
                                        error -> failMethod()
                                ) {
                                    @Override
                                    public Map<String, String> getHeaders() {
                                        Map<String, String> header = new HashMap<>();
                                        header.put("content-type", "application/json");
                                        header.put("authorization", "key=AAAAc0PT8sk:APA91bEDZS6mG28c5wQxOilIx66A2fD_uqavz1OCBeH3TUU0KfSa_5JjrL8R3HALkzg7ZB__-KjaSejtWMcghY03Q_EJfXs9jS8BUxdFmMcRL4sPazldZeqMtqO05ugp9U09GtNkb692");
                                        return header;
                                    }
                                };

                                mRequestQue.add(request);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            } catch (Exception e) {
                Toast.makeText(OrderDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(OrderDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void successMethod() {
        progressBar.setVisibility(View.INVISIBLE);
        customerMessage.setText("");
        Toast.makeText(OrderDetails.this, "Sent Successfully.", Toast.LENGTH_SHORT).show();
    }

    private void failMethod() {
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(OrderDetails.this, "There was an error.", Toast.LENGTH_SHORT).show();
    }

    public void editOrderStatusBtn(View view) {
        try {
            AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetails.this);
            View vobj = LayoutInflater.from(OrderDetails.this).inflate(R.layout.layout_edit_order_status, null);
            builder.setView(vobj);
            alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            RadioButton radPreparing = vobj.findViewById(R.id.radioButton_editPrepearingBtn);
            RadioButton radOnThWay = vobj.findViewById(R.id.radioButton_onTheWayBtn);
            RadioButton radDelivered = vobj.findViewById(R.id.radioButton_deliveredBtn);
            RadioButton radDispatch = vobj.findViewById(R.id.radioButton_dispatchedBtn);
            if (statusOfOrder.getText().toString().equals("Preparing") || statusOfOrder.getText().toString().equals("preparing")) {
                radPreparing.setChecked(true);
            } else if (statusOfOrder.getText().toString().equals("dispatched") || statusOfOrder.getText().toString().equals("Dispatched")) {
                radDispatch.setChecked(true);
            } else if (statusOfOrder.getText().toString().equals("delivered") || statusOfOrder.getText().toString().equals("Delivered")) {
                radDelivered.setChecked(true);
            } else if (statusOfOrder.getText().toString().equals("onTheWay") || statusOfOrder.getText().toString().equals("preparing")) {
                radOnThWay.setChecked(true);
            }
//
            radDelivered.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    radDispatch.setChecked(false);
                    radPreparing.setChecked(false);
                    radDelivered.setChecked(true);
                    status = "delivered";
                    radOnThWay.setChecked(false);
                }

            });
            radDispatch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    radDispatch.setChecked(true);
                    radPreparing.setChecked(false);
                    status = "dispatched";
                    radDelivered.setChecked(false);
                    radOnThWay.setChecked(false);
                }

            });
            radOnThWay.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    radDispatch.setChecked(false);
                    radDelivered.setChecked(false);
                    radPreparing.setChecked(false);
                    radOnThWay.setChecked(true);
                    status = "onTheWay";
                }

            });
            radPreparing.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    radDispatch.setChecked(false);
                    radDelivered.setChecked(false);
                    status = "preparing";
                    radPreparing.setChecked(true);
                    radOnThWay.setChecked(false);
                }
            });

            CardView savebtn = vobj.findViewById(R.id.saveChanges_orderStatus_btn);
            savebtn.setOnClickListener(v -> {
                FirebaseDatabase.getInstance().getReference("orders")
                        .child(getIntent().getStringExtra("customerUid"))
                        .child(getIntent().getStringExtra("orderId"))
                        .child("status").setValue(status).addOnSuccessListener(aVoid -> {
                    alertDialog.cancel();
                    statusOfOrder.setText(status);
                    sendAndUploadNotification("Your products of " + orderId.getText().toString() + " is " + status);
                    Toast.makeText(OrderDetails.this, "Changed Successfully", Toast.LENGTH_SHORT).show();
                });
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private void getAddress() {
        FirebaseDatabase.getInstance().getReference("users").child(getIntent().getStringExtra("customerUid"))
                .child("deliveryAddress").child(ordersObject.getAddressId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) snapshot.getValue();
                    String temp = hashMap.get("addressLine1").concat(", " + Objects.requireNonNull(hashMap.get("addressLine2")))
                            .concat(", " + hashMap.get("city")).concat(", " + Objects.requireNonNull(hashMap.get("pincode")));
                    addressCustomer.setText(temp);
                    phoneNumber.setText(hashMap.get("phoneNumber"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void closeOrderDetailsScreen(View view) {
        startActivity(new Intent(OrderDetails.this, MainActivity.class));
    }
}