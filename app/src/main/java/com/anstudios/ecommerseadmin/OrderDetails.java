package com.anstudios.ecommerseadmin;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.ecommerseadmin.adapters.adapterEditDetails;
import com.anstudios.ecommerseadmin.models.modelEditDetails;

import java.util.ArrayList;

public class OrderDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<modelEditDetails> arrayList;
    private adapterEditDetails adapter;
    private ConstraintLayout orderDetailsBtn, orderProductsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        recyclerView=findViewById(R.id.order_details_recycler);
        orderDetailsBtn=findViewById(R.id.orderDetailsBtn);
        orderProductsBtn=findViewById(R.id.order_produts_btn);
        arrayList=new ArrayList<>();
        adapter=new adapterEditDetails(this,arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        orderProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDetailsBtn.setBackgroundColor(ContextCompat.getColor(OrderDetails.this, R.color.white));
                orderProductsBtn.setBackgroundColor(ContextCompat.getColor(OrderDetails.this, R.color.apptheme));
            }
        });
        orderDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDetailsBtn.setBackgroundColor(ContextCompat.getColor(OrderDetails.this, R.color.apptheme));
                orderProductsBtn.setBackgroundColor(ContextCompat.getColor(OrderDetails.this, R.color.white));
            }
        });
    }
}