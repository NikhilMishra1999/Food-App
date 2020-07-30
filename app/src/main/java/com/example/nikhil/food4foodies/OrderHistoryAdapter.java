package com.example.nikhil.food4foodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    Context context;
    List<cartitem> OrderList;
    List<String> OrderidList;

    public OrderHistoryAdapter(Context context, List<cartitem> OrderList, List<String> OrderidList) {
        this.context = context;
        this.OrderList = OrderList;
        this.OrderidList = OrderidList;

    }
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.order_card, parent, false);
        OrderHistoryAdapter.ViewHolder viewHolder = new OrderHistoryAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderHistoryAdapter.ViewHolder holder, int position) {
        cartitem order = OrderList.get(position);
        String orderid = OrderidList.get(0);
        holder.orderid.setText(orderid);
        holder.itemcount.setText(OrderList.size()+"");
    }

    @Override
    public int getItemCount() {
        return OrderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView fav,itemcount,item,orderid;
        Button cancelorder;

        public ViewHolder(View itemView) {
            super(itemView);
            fav = itemView.findViewById(R.id.favourite);
            cancelorder = itemView.findViewById(R.id.cancel);

            itemcount = itemView.findViewById(R.id.item);
            item = itemView.findViewById(R.id.itemname);
            orderid = itemView.findViewById(R.id.textView22);
        }
    }


}
