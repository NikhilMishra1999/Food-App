package com.example.nikhil.food4foodies;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    Context context;
    List<menuitem> MenuList;
    List<cartitem> CartList;
    MegaClass m = new MegaClass();
    DatabaseReference dataReference;
    FirebaseAuth mFirebaseAuth;



    public MenuAdapter(Context context, List<menuitem> MenuList) {
        this.context = context;
        this.MenuList = MenuList;
    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.menu_card, parent, false);
        MenuAdapter.ViewHolder viewHolder = new MenuAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuAdapter.ViewHolder holder, final int position) {

        mFirebaseAuth = FirebaseAuth.getInstance();
        final String userId = mFirebaseAuth.getCurrentUser().getUid();
        final int[] counter = {0};
        final menuitem menu = MenuList.get(position);
        holder.name.setText(menu.getName());
        Picasso.get().load(menu.getLink()).into(holder.img);
        holder.price.setText("₹"+menu.getPrice());
        final Double[] total_price = {0.0};

        final int prices = menu.getPrice();

//        holder.addtocart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.addtocart.setVisibility(View.INVISIBLE);
//                holder.less.setVisibility(View.VISIBLE);
//                holder.more.setVisibility(View.VISIBLE);
//                holder.count.setVisibility(View.VISIBLE);
//
//                int flag = (counter[0]+1);
//                holder.count.setText(""+flag);
//                total_price[0] +=menu.getPrice();
//                cartitem item = new cartitem(menu.getName(),total_price[0],counter[0]);
//                Log.d("message", item.getNam());
//                m.addItems(position,item);
//            }
//        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter[0]++;
                holder.count.setText(""+ counter[0]);
                total_price[0] +=menu.getPrice();
                //adding items in cart
                cartitem item = new cartitem(menu.getName(),total_price[0],counter[0],menu.getLink());
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
                    total_price[0]-=menu.getPrice();
                    cartitem item = new cartitem(menu.getName(),total_price[0],counter[0],menu.getLink());
                    Log.d("sub", (item != null ? item.getNam() : null) +item.getCount()+item.getPrice());
                    m.addItems(position,item);
                    //Log.d("message", m.getItems().get(position).getNam());
                }
            }
        });

        holder.fav.setTag(1);
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if ((Integer) holder.fav.getTag() == 1) {
                    holder.fav.setImageResource(R.drawable.heart);
                    holder.fav.setTag(2);
                    dataReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("favourites");
                    //String id = dataReference.push().getKey();
                    final Map<String, Object> itemfav = new HashMap<>();
                    itemfav.put("name", menu.getName());
                    itemfav.put("link", menu.getLink());
                    itemfav.put("price",menu.getPrice());
                    dataReference.child(String.valueOf(position)).setValue(itemfav);

                } else {
                    holder.fav.setImageResource(R.drawable.ic_favorite_black);
                    holder.fav.setTag(1);

                    mFirebaseAuth = FirebaseAuth.getInstance();
                    dataReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("favourites");
                    dataReference.child(String.valueOf(position)).setValue(null);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return MenuList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView img,fav;
        Button addtocart,less, more;
        TextView count;

        public ViewHolder(View itemView) {
            super(itemView);

            addtocart =  itemView.findViewById(R.id.cart);
            less =  itemView.findViewById(R.id.minus);
            more = itemView.findViewById(R.id.add);
            count = itemView.findViewById(R.id.number);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            img = itemView.findViewById(R.id.imageView);
            fav = itemView.findViewById(R.id.heart);
        }
    }


}