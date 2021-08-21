package com.anstudios.ecommerseadmin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.ecommerseadmin.R;
import com.anstudios.ecommerseadmin.adapters.adaptersProducts;
import com.anstudios.ecommerseadmin.models.modelProducts;

import java.util.ArrayList;


public class ProductsFragment extends Fragment {

    private View view;
    private RecyclerView recyclerViewFruits, recyclerViewMeats, recyclerViewOils, recyclerViewBakery, recyclerViewDairy, recyclerViewBeverages;
    private ArrayList<modelProducts> arrayListFruits, arrayListMeats, arrayListBakery, arrayListBeverages, arrayListOils, arrayListDairy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_products, container, false);
        recyclerViewFruits = view.findViewById(R.id.product_recycler_fuits_veges);
        recyclerViewBakery = view.findViewById(R.id.product_recycler_bakery_snacks);
        recyclerViewBeverages = view.findViewById(R.id.product_recycler_beverages);
        recyclerViewMeats = view.findViewById(R.id.product_recycler_meat_fish);
        recyclerViewOils = view.findViewById(R.id.product_recycler_oils_ghee);
        recyclerViewDairy = view.findViewById(R.id.product_recycler_dairy_products);

        arrayListFruits = new ArrayList<>();
        arrayListBakery = new ArrayList<>();
        arrayListMeats = new ArrayList<>();
        arrayListBeverages = new ArrayList<>();
        arrayListOils = new ArrayList<>();
        arrayListDairy = new ArrayList<>();


        recyclerViewFruits.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewOils.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewBakery.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMeats.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDairy.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewBeverages.setLayoutManager(new LinearLayoutManager(getContext()));

        arrayListBakery.add(new modelProducts());
        arrayListBakery.add(new modelProducts());
        arrayListBakery.add(new modelProducts());
        arrayListBakery.add(new modelProducts());
        arrayListBakery.add(new modelProducts());

        adaptersProducts adapter = new adaptersProducts(getContext(), arrayListBakery);
        recyclerViewFruits.setAdapter(adapter);
        recyclerViewBakery.setAdapter(adapter);
        recyclerViewOils.setAdapter(adapter);
        recyclerViewMeats.setAdapter(adapter);
        recyclerViewBeverages.setAdapter(adapter);
        recyclerViewDairy.setAdapter(adapter);

        return view;
    }
}