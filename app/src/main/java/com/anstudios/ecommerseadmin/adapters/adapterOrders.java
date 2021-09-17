package com.anstudios.ecommerseadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.ecommerseadmin.Constants;
import com.anstudios.ecommerseadmin.OrderDetails;
import com.anstudios.ecommerseadmin.OrdersObject;
import com.anstudios.ecommerseadmin.R;

import java.util.ArrayList;

public class adapterOrders extends RecyclerView.Adapter<adapterOrders.viewHolder> {

    private final Context context;
    private final ArrayList<OrdersObject> arrayList;
    private final ArrayList<String> orderId;
    private final ArrayList<String> customerUid;

    public adapterOrders(ArrayList<String> orderId, Context context, ArrayList<OrdersObject> arrayList, ArrayList<String> customerUid) {
        this.context = context;
        this.customerUid = customerUid;
        this.orderId = orderId;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.layout_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        try {
            String statusStr = arrayList.get(position).getStatus().replaceFirst(arrayList.get(position).getStatus().charAt(0) + "", (arrayList.get(position).getStatus().charAt(0) + "").toUpperCase());
            holder.status.setText(statusStr);
            holder.constraintLayout.setOnClickListener(v -> {
                Intent intent = new Intent(context, OrderDetails.class);
                intent.putExtra("OrdersObject", arrayList.get(position));
                intent.putExtra("customerUid", customerUid.get(position));
                intent.putExtra("orderId", orderId.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
            if (arrayList.get(position).getPaymentType().equals("cash")) {
                holder.paymentType.setText(context.getString(R.string.PAYONDELIEVRY));
            } else {
                holder.paymentType.setText(context.getString(R.string.PAIDONLINE));
            }
            Toast.makeText(context, arrayList.get(position).getPaymentType(), Toast.LENGTH_SHORT).show();
            holder.price.setText(Constants.CURRENCY_SIGN.concat(arrayList.get(position).getTotalPrice()));
            holder.date.setText(arrayList.get(position).getTimeStamp());
            holder.orderId.setText("Order Id : #" + arrayList.get(position).getIndex());
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        private final TextView orderId;
        private final TextView date;
        private final TextView price;
        private final TextView status;
        private final TextView paymentType;
        private final ConstraintLayout constraintLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.layout_orders_orderId);
            date = itemView.findViewById(R.id.layout_orders_date);
            status = itemView.findViewById(R.id.layout_order_status);
            price = itemView.findViewById(R.id.layout_orders_price);
            paymentType = itemView.findViewById(R.id.layout_orders_paymentType);
            constraintLayout = itemView.findViewById(R.id.ordersMainContainer);
        }
    }
}
