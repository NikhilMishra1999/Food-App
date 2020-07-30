package com.example.nikhil.food4foodies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    HashMap<Integer,cartitem> cartList;
    MegaClass m = new MegaClass();

    public CartAdapter(Context context, HashMap<Integer,cartitem> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.cart_card, parent, false);
        CartAdapter.ViewHolder viewHolder = new CartAdapter.ViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ArrayList<Integer> arr = getKeysForgettingNumberOfVehicles();
        final cartitem cartitem = cartList.get(arr.get(position));
        holder.cartitemname.setText(cartitem != null ? cartitem.getNam() : null);
        holder.cartprice.setText("₹"+ (cartitem != null ? cartitem.getPrice() : null));
        holder.count.setText(""+ (cartitem != null ? cartitem.getCount()+" Item" : null));
        Picasso.get().load(cartitem.getLink()).into(holder.cartimage);


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cartitemname, cartprice;
        ImageView cartimage;
        Button less, more;

        TextView count;

        public ViewHolder(View itemView) {
            super(itemView);

            count = itemView.findViewById(R.id.quant);

            cartitemname = itemView.findViewById(R.id.itemname);
            cartprice = itemView.findViewById(R.id.totalprice);
            cartimage = itemView.findViewById(R.id.cartitemimage);
        }
    }

    ArrayList<Integer> getKeysForgettingNumberOfVehicles(){
        ArrayList<Integer> keys = new ArrayList<>();
        for(int key:new MegaClass().getItems().keySet()){
            keys.add(key);
        }
        return keys;
    }

}