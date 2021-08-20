package com.anstudios.ecommerseadmin.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anstudios.ecommerseadmin.R;
import com.anstudios.ecommerseadmin.adapters.adapterPincode;
import com.anstudios.ecommerseadmin.models.modelPincode;

import java.util.ArrayList;


public class PincodeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<modelPincode> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_pincode, container, false);
        recyclerView=view.findViewById(R.id.pincode_recycler_view);
        arrayList=new ArrayList<>();
        arrayList.add(new modelPincode("","",""));
        arrayList.add(new modelPincode("","",""));
        arrayList.add(new modelPincode("","",""));
        arrayList.add(new modelPincode("","",""));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterPincode adapter=new adapterPincode(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        return view;
    }
}