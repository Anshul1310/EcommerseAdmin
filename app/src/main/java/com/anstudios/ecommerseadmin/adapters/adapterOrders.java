package com.anstudios.ecommerseadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.ecommerseadmin.OrderDetails;
import com.anstudios.ecommerseadmin.OrdersObject;
import com.anstudios.ecommerseadmin.R;
import com.anstudios.ecommerseadmin.models.modelOrders;

import java.util.ArrayList;

public class adapterOrders extends RecyclerView.Adapter<adapterOrders.viewHolder> {

    private Context context;
    private ArrayList<OrdersObject> arrayList;

    public adapterOrders(Context context, ArrayList<OrdersObject> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.layout_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        String statusStr=arrayList.get(position).getStatus().replaceFirst(arrayList.get(position).getStatus().charAt(0)+"",(arrayList.get(position).getStatus().charAt(0)+"").toUpperCase());
        holder.status.setText(statusStr);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetails.class);
                intent.putExtra("OrdersObject", arrayList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        if(arrayList.get(position).getPaymentType().equals("cash")){
            holder.paymentType.setText("Pay on Delivery");
        }else{
            holder.paymentType.setText("Paid Online");
        }
        holder.price.setText(arrayList.get(position).getTotalPrice());
        holder.date.setText(arrayList.get(position).getTimeStamp());
        holder.orderId.setText("Order Id : #"+arrayList.get(position).getIndex());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView orderId;
        private TextView date;
        private TextView price;
        private TextView status;
        private TextView paymentType;
        private ConstraintLayout constraintLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.layout_orders_orderId);
            date = itemView.findViewById(R.id.layout_orders_date);
            status= itemView.findViewById(R.id.layout_order_status);
            price = itemView.findViewById(R.id.layout_orders_price);
            paymentType = itemView.findViewById(R.id.layout_orders_paymentType);
            constraintLayout=itemView.findViewById(R.id.ordersMainContainer);
        }
    }
}
