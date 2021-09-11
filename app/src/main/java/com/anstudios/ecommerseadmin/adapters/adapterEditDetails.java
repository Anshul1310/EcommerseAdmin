package com.anstudios.ecommerseadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.ecommerseadmin.R;
import com.anstudios.ecommerseadmin.models.modelEditDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapterEditDetails extends RecyclerView.Adapter<adapterEditDetails.viewHolder> {

    private final Context context;
    private final ArrayList<modelEditDetails> arrayList;

    public adapterEditDetails(Context context, ArrayList<modelEditDetails> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.layout_edit_details,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.measuringUnit.setText(arrayList.get(position).getMeasuringUnit());
        holder.price.setText(arrayList.get(position).getPrice());
        Picasso.get().load(arrayList.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView title;
        private final TextView measuringUnit;
        private final TextView price;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.layout_editDetails_image);
            title=itemView.findViewById(R.id.layout_editDetails_title);
            measuringUnit=itemView.findViewById(R.id.layout_editDetails_measuringUnit);
            price=itemView.findViewById(R.id.layout_editDetails_price);
        }
    }
}
