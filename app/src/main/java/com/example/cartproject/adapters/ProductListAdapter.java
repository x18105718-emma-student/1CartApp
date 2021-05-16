package com.example.cartproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cartproject.R;
import com.example.cartproject.models.Product;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private List<Product> productList;
    private ProductListClickListener clickListener;

    public ProductListAdapter(List<Product> productList, ProductListClickListener clickListener) {
        this.productList = productList;
        this.clickListener = clickListener;
    }

    public void updateData(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerfile_products, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductListAdapter.MyViewHolder holder, final int position) {
        holder.productName.setText(productList.get(position).getName());
        holder.productPrice.setText("Price: €" + productList.get(position).getPrice());
        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = productList.get(position);
                product.setTotalInCart(1);
                clickListener.onAddToCartClick(product);
                holder.addMoreLayout.setVisibility(View.VISIBLE);
                holder.addToCartButton.setVisibility(View.GONE);
                holder.textviewCount.setText(product.getTotalInCart() + "");
            }
        });
        holder.imageMinusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = productList.get(position);
                int total = product.getTotalInCart();
                total--;
                //if items in cart are less than 0
                if (total > 0) {
                    product.setTotalInCart(total);
                    clickListener.onUpdateCartClick(product);
                    holder.textviewCount.setText(total + "");
                } else {
                    holder.addMoreLayout.setVisibility(View.GONE);
                    holder.addToCartButton.setVisibility(View.VISIBLE);
                    product.setTotalInCart(total);
                    clickListener.onRemoveFromCartClick(product);
                }
            }
        });

        holder.imageAddQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = productList.get(position);
                int total = product.getTotalInCart();
                total++;
                if (total <= 10) {
                    product.setTotalInCart(total);
                    clickListener.onUpdateCartClick(product);
                    holder.textviewCount.setText(total + "");
                }
            }
        });

        Glide.with(holder.thumbImage)
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
        TextView addToCartButton;
        ImageView thumbImage;
        ImageView imageMinusQty; // change quantity minus
        ImageView imageAddQty;// change quantity plus
        TextView textviewCount;
        LinearLayout addMoreLayout;

        public MyViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.productName);
            productPrice = view.findViewById(R.id.productPrice);
            addToCartButton = view.findViewById(R.id.addToCartButton);
            thumbImage = view.findViewById(R.id.productImage);
            imageMinusQty = view.findViewById(R.id.imageMinus);
            imageAddQty = view.findViewById(R.id.imageAdd);
            textviewCount = view.findViewById(R.id.tvCount);

            addMoreLayout = view.findViewById(R.id.addMoreLayout);
        }
    }

    public interface ProductListClickListener {
        public void onAddToCartClick(Product product);

        public void onUpdateCartClick(Product product);

        public void onRemoveFromCartClick(Product product);
    }
}

