package com.anstudios.ecommerseadmin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.ecommerseadmin.OrdersObject;
import com.anstudios.ecommerseadmin.R;
import com.anstudios.ecommerseadmin.adapters.adapterOrders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class OrdersFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ConstraintLayout dispatchedBtn, allBtn, onTheWayBtn, preparingBtn, deliveredBtn;
    private ArrayList<OrdersObject> arrayList;
    private adapterOrders adapter;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_orders, container, false);
        progressBar=view.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        arrayList = new ArrayList<>();
        linearLayout=view.findViewById(R.id.fragment_orders_status_layout);
        recyclerView = view.findViewById(R.id.ordersRecycler);
        adapter = new adapterOrders(getContext(), arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        getMyOrders("all");

        preparingBtn = view.findViewById(R.id.ordersPreparingBtn);
        dispatchedBtn = view.findViewById(R.id.orderDispatchBtn);
        onTheWayBtn = view.findViewById(R.id.orders_onTheWayBtn);
        deliveredBtn = view.findViewById(R.id.ordersDeliveredBtn);
        allBtn = view.findViewById(R.id.ordersAllBtn);

        allBtn.setOnClickListener(v -> {
            getMyOrders("all");
            allBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.apptheme));
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));

        });
        onTheWayBtn.setOnClickListener(v -> {
            getMyOrders("onTheWay");
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.apptheme));
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        });
        deliveredBtn.setOnClickListener(v -> {
            getMyOrders("delivered");
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.apptheme));
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        });
        dispatchedBtn.setOnClickListener(v -> {
            getMyOrders("dispatched");
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.apptheme));
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        });
        preparingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyOrders("preparing");
                preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.apptheme));
                allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
                dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
                deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
                onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            }
        });
        return view;
    }

    private void getMyOrders(String status) {
        arrayList.clear();
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        FirebaseDatabase.getInstance().getReference("orders")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot objectOrders : dataSnapshot.getChildren()) {
                                OrdersObject ordersObject = objectOrders.getValue(OrdersObject.class);
                                if (status.equals("all")) {
                                    arrayList.add(ordersObject);
                                } else {
                                    if (ordersObject.getStatus().equals(status)) {
                                        arrayList.add(ordersObject);
                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if(arrayList.size()==0){
                            linearLayout.setVisibility(View.VISIBLE);
                        }else{
                            linearLayout.setVisibility(View.INVISIBLE);
                        }
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}