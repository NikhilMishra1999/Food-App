package com.example.nikhil.food4foodies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    Context context;
    List<menuitem> FavList;
    MegaClass m = new MegaClass();

    public FavouriteAdapter(Context context, List<menuitem> FavList) {
        this.context = context;
        this.FavList = FavList;
    }

    @NonNull
    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.fav_card, parent, false);
        FavouriteAdapter.ViewHolder viewHolder = new FavouriteAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FavouriteAdapter.ViewHolder holder, final int position) {

        final int[] counter = {0};
        final menuitem fav = FavList.get(position);
        holder.name.setText(fav.getName());
        Picasso.get().load(fav.getLink()).into(holder.img);
        holder.price.setText("₹"+fav.getPrice());
        final Double[] total_price = {0.0};

        final int prices = fav.getPrice();

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter[0]++;
                holder.count.setText(""+ counter[0]);
                total_price[0] += fav.getPrice();

                //adding items in cart
                cartitem item = new cartitem(fav.getName(),total_price[0],counter[0],fav.getLink());
                Log.d("add", item.getNam()+item.getCount()+item.getPrice());
                m.addItems(position,item);

            }
        });

        holder.less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter[0] > 0){
                    counter[0]--;
                    holder.count.setText(""+ counter[0]);
                    total_price[0]-= fav.getPrice();
                    cartitem item = new cartitem(fav.getName(),total_price[0],counter[0],fav.getLink());
                    Log.d("sub", (item != null ? item.getNam() : null) +item.getCount()+item.getPrice());
                    m.addItems(position,item);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return FavList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView img;
        Button less, more;
        TextView count;

        public ViewHolder(View itemView) {
            super(itemView);

            less =  itemView.findViewById(R.id.fav_minus);
            more = itemView.findViewById(R.id.fav_add);
            count = itemView.findViewById(R.id.fav_number);

            name = itemView.findViewById(R.id.fav_name);
            price = itemView.findViewById(R.id.fav_price);
            img = itemView.findViewById(R.id.fav_img);
        }
    }


}