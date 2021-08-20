package com.anstudios.ecommerseadmin.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anstudios.ecommerseadmin.R;
import com.anstudios.ecommerseadmin.adapters.adapterOrders;
import com.anstudios.ecommerseadmin.models.modelOrders;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<modelOrders> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_orders, container, false);
        recyclerView=view.findViewById(R.id.ordersRecycler);
        arrayList=new ArrayList<>();
        arrayList.add(new modelOrders());
        arrayList.add(new modelOrders());
        arrayList.add(new modelOrders());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterOrders adapter=new adapterOrders(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        return view;
    }
}