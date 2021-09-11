package com.anstudios.ecommerseadmin.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anstudios.ecommerseadmin.OrdersObject;
import com.anstudios.ecommerseadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private View view;
    private TextView about, totalSale, totalCustomers, totalEarnings, totalProducts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        about = view.findViewById(R.id.home_about);
        totalSale = view.findViewById(R.id.frag_home_total_sales);
        totalCustomers = view.findViewById(R.id.frag_home_total_customers);
        totalEarnings = view.findViewById(R.id.frag_home_total_earnings);
        totalProducts = view.findViewById(R.id.frag_home_total_products);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            // do UI changes before background work here
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                // do background work here
                try {
                    getStoreAbout();
                    fetchNumberOfUsers();
                    fetchNoOfProducts();
                    fetchSalesAndRevenue();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                handler.post(() -> {
                    // do UI changes after background work here
                });
            });
        });
        return view;
    }


    private void getStoreAbout(){
        FirebaseDatabase
                .getInstance().getReference("storeInfo").child("about")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String aboutStr= (String) snapshot.getValue();
                        about.setText(aboutStr);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void fetchSalesAndRevenue() {
        FirebaseDatabase.getInstance().getReference("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalOrdersInt = 0, totalEarningInt = 0;
                try {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        totalOrdersInt += (int) dataSnapshot.getChildrenCount();
                        for (DataSnapshot objectOrders : dataSnapshot.getChildren()) {
                            HashMap<String,String> hashMap= (HashMap<String, String>) objectOrders.getValue();
                            totalEarningInt += Integer.parseInt(hashMap.get("totalPrice"));
                        }
                        totalEarnings.setText(String.valueOf(totalEarningInt));
                    }
                    totalSale.setText(String.valueOf(totalOrdersInt));
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchNoOfProducts() {
        FirebaseDatabase.getInstance().getReference("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, HashMap<String, String>> hashMap = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                    assert hashMap != null;
                    totalProducts.setText(String.valueOf(hashMap.size()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchNumberOfUsers() {
        FirebaseDatabase.getInstance().getReference("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            if (snapshot.exists()) {
                                HashMap<String, HashMap<String, String>> hashMapHashMap = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                                assert hashMapHashMap != null;
                                totalCustomers.setText(String.valueOf(hashMapHashMap.size()));
                            }
                        } catch (Exception ignored) {
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}