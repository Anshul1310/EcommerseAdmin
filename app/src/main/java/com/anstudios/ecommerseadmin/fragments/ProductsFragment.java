package com.anstudios.ecommerseadmin.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.ecommerseadmin.AddProduct;
import com.anstudios.ecommerseadmin.R;
import com.anstudios.ecommerseadmin.adapters.adaptersProducts;
import com.anstudios.ecommerseadmin.models.modelProducts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class ProductsFragment extends Fragment {

    private ProgressDialog progressDialog;
    private adaptersProducts adapterBakery, adapterFruits, adapterOils, adapterMeat, adapterBeverages, adapterDairyProducts;
    private ArrayList<modelProducts> arrayListFruits, arrayListMeats, arrayListBakery, arrayListBeverages, arrayListOils, arrayListDairy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        RecyclerView recyclerViewFruits = view.findViewById(R.id.product_recycler_fuits_veges);
        RecyclerView recyclerViewBakery = view.findViewById(R.id.product_recycler_bakery_snacks);
        RecyclerView recyclerViewBeverages = view.findViewById(R.id.product_recycler_beverages);
        RecyclerView recyclerViewMeats = view.findViewById(R.id.product_recycler_meat_fish);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("We are getting alll products.");
        progressDialog.setCanceledOnTouchOutside(false);
        RecyclerView recyclerViewOils = view.findViewById(R.id.product_recycler_oils_ghee);
        RecyclerView recyclerViewDairy = view.findViewById(R.id.product_recycler_dairy_products);

        ImageView addBakery = view.findViewById(R.id.bakery_snacks_add_btn);
        ImageView addOils = view.findViewById(R.id.oils_ghee_add_btn);
        ImageView addMeats = view.findViewById(R.id.meats_fishes_add_btn);
        ImageView addBeverages = view.findViewById(R.id.beverages_add_btn);
        ImageView addFruitsVeges = view.findViewById(R.id.fruits_veges_add_btn);
        ImageView addDairy = view.findViewById(R.id.dairy_products_add_btn);

        addBeverages.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddProduct.class);
            intent.putExtra("category", "beverages");
            startActivity(intent);
        });

        addFruitsVeges.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddProduct.class);
            intent.putExtra("category", "fruitsAndVeges");
            startActivity(intent);
        });

        addBakery.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddProduct.class);
            intent.putExtra("category", "bakery");
            startActivity(intent);
        });

        addOils.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddProduct.class);
            intent.putExtra("category", "oilsAndGhee");
            startActivity(intent);
        });

        addMeats.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddProduct.class);
            intent.putExtra("category", "meats");
            startActivity(intent);
        });

        addDairy.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddProduct.class);
            intent.putExtra("category", "dairyProducts");
            startActivity(intent);
        });


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

        adapterBakery = new adaptersProducts(getContext(), arrayListBakery);
        adapterFruits = new adaptersProducts(getContext(), arrayListFruits);
        adapterBeverages = new adaptersProducts(getContext(), arrayListBeverages);
        adapterOils = new adaptersProducts(getContext(), arrayListOils);
        adapterMeat = new adaptersProducts(getContext(), arrayListMeats);
        adapterDairyProducts = new adaptersProducts(getContext(), arrayListDairy);
        recyclerViewFruits.setAdapter(adapterFruits);
        recyclerViewBakery.setAdapter(adapterBakery);
        recyclerViewOils.setAdapter(adapterOils);
        recyclerViewMeats.setAdapter(adapterMeat);
        recyclerViewBeverages.setAdapter(adapterBeverages);
        recyclerViewDairy.setAdapter(adapterDairyProducts);
        getProductsFromDb();
        return view;
    }


    private void getProductsFromDb() {
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    HashMap<String, HashMap<String, String>> hashMap = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                    HashMap<String, String> child;
                    for (int i = 0; i < hashMap.size(); i++) {
                        child = hashMap.get(hashMap.keySet().toArray()[i]);
                        if (child.get("category").equals("fruitsAndVeges")) {
                            arrayListFruits.add(new modelProducts(child.get("category"), child.get("smallImage"), child.get("bigImage"), child.get("title"), child.get("description"), child.get("price"), child.get("measuringUnit"), hashMap.keySet().toArray()[i] + ""));
                        } else if (child.get("category").equals("bakery")) {
                            arrayListBakery.add(new modelProducts(child.get("category"), child.get("smallImage"), child.get("bigImage"), child.get("title"), child.get("description"), child.get("price"), child.get("measuringUnit"), hashMap.keySet().toArray()[i] + ""));
                        } else if (child.get("category").equals("oilsAndGhee")) {
                            arrayListOils.add(new modelProducts(child.get("category"), child.get("smallImage"), child.get("bigImage"), child.get("title"), child.get("description"), child.get("price"), child.get("measuringUnit"), hashMap.keySet().toArray()[i] + ""));
                        } else if (child.get("category").equals("dairyProducts")) {
                            arrayListDairy.add(new modelProducts(child.get("category"), child.get("smallImage"), child.get("bigImage"), child.get("title"), child.get("description"), child.get("price"), child.get("measuringUnit"), hashMap.keySet().toArray()[i] + ""));
                        } else if (child.get("category").equals("meat")) {
                            arrayListMeats.add(new modelProducts(child.get("category"), child.get("smallImage"), child.get("bigImage"), child.get("title"), child.get("description"), child.get("price"), child.get("measuringUnit"), hashMap.keySet().toArray()[i] + ""));
                        } else if (child.get("category").equals("beverages")) {
                            arrayListBeverages.add(new modelProducts(child.get("category"), child.get("smallImage"), child.get("bigImage"), child.get("title"), child.get("description"), child.get("price"), child.get("measuringUnit"), hashMap.keySet().toArray()[i] + ""));
                        }
                        adapterBakery.notifyDataSetChanged();
                        adapterBeverages.notifyDataSetChanged();
                        adapterDairyProducts.notifyDataSetChanged();
                        adapterFruits.notifyDataSetChanged();
                        adapterOils.notifyDataSetChanged();
                        adapterMeat.notifyDataSetChanged();
                        progressDialog.cancel();

                    }
                } catch (Exception exception) {
                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}