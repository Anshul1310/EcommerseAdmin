package com.anstudios.ecommerseadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.ecommerseadmin.AddProduct;
import com.anstudios.ecommerseadmin.Constants;
import com.anstudios.ecommerseadmin.R;
import com.anstudios.ecommerseadmin.models.modelProducts;
import com.squareup.picasso.Picasso;

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
        holder.itemTitle.setText(arrayList.get(position).getName());
        holder.price.setText(Constants.CURRENCY_SIGN.concat(arrayList.get(position).getPrice()));
        holder.measuringUnit.setText("Price for 1 " + arrayList.get(position).getMeasuringUnit());
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddProduct.class);
                intent.putExtra("productId", arrayList.get(position).getProductId());
                intent.putExtra("title", arrayList.get(position).getName());
                intent.putExtra("description", arrayList.get(position).getDescription());
                intent.putExtra("smallImage", arrayList.get(position).getSmallImage());
                intent.putExtra("bigImage", arrayList.get(position).getBigImage());
                intent.putExtra("category", arrayList.get(position).getCategory());
                intent.putExtra("price", arrayList.get(position).getPrice());
                intent.putExtra("measuringUnit", arrayList.get(position).getMeasuringUnit());
                context.startActivity(intent);
            }
        });
        Picasso.get().load(arrayList.get(position).getSmallImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private final TextView itemTitle;
        private final TextView measuringUnit;
        private final TextView price;
        private final ImageView editBtn;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cart_image);
            itemTitle = itemView.findViewById(R.id.cart_title);
            measuringUnit = itemView.findViewById(R.id.cart_price_rate);
            price = itemView.findViewById(R.id.cart_item_price);
            editBtn = itemView.findViewById(R.id.cart_edit_button);
        }
    }
}
