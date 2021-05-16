package com.example.cartproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cartproject.R;
import com.example.cartproject.models.Product;

import java.util.List;

public class PlaceOrderAdapter extends RecyclerView.Adapter<PlaceOrderAdapter.MyViewHolder> {

    private List<Product> productList;


    public PlaceOrderAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public void updateData(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaceOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_order_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOrderAdapter.MyViewHolder holder, int position) {
        holder.productName.setText(productList.get(position).getName());
        holder.productPrice.setText("Price: €" + String.format("%.2f", productList.get(position).getPrice() * productList.get(position).getTotalInCart()));
        holder.productQty.setText("Item Quantity: " + productList.get(position).getTotalInCart());
        Glide.with(holder.thumbImage) // using android glide for image display
                .load(productList.get(position).getUrl())
                .into(holder.thumbImage);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView productQty;
        ImageView thumbImage;

        public MyViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.productName);
            productPrice = view.findViewById(R.id.productPrice);
            productQty = view.findViewById(R.id.productQty);
            thumbImage = view.findViewById(R.id.productImage);
        }
    }
}

