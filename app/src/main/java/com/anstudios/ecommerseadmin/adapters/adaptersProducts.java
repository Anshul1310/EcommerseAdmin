package com.anstudios.ecommerseadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.ecommerseadmin.R;
import com.anstudios.ecommerseadmin.models.modelProducts;

import java.util.ArrayList;

public class adaptersProducts extends RecyclerView.Adapter<adaptersProducts.viewHolder> {

    private Context context;
    private ArrayList<modelProducts> arrayList;

    public adaptersProducts(Context context, ArrayList<modelProducts> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.layout_products, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        public viewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
