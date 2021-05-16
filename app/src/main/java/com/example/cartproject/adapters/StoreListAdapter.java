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
import com.example.cartproject.models.StoreModel;

import java.util.List;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.MyViewHolder> {

    private List<StoreModel> storeModelList;
    private StoreListClickListener clickListener;

    public StoreListAdapter(List<StoreModel> storeModelList, StoreListClickListener clickListener) {
        this.storeModelList = storeModelList;
        this.clickListener = clickListener;
    }

    public void updateData(List<StoreModel> StoreModelList) {
        this.storeModelList = storeModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StoreListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerfile, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreListAdapter.MyViewHolder holder, final int position) {
        holder.storeName.setText(storeModelList.get(position).getName());
        holder.storeDesc.setText(storeModelList.get(position).getDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(storeModelList.get(position));
            }
        });
        Glide.with(holder.storeImage)
                .load(storeModelList.get(position).getImage())
                .into(holder.storeImage);

    }

    @Override
    public int getItemCount() {
        return storeModelList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView storeName, storeDesc;
        ImageView storeImage;

        public MyViewHolder(View view) {
            super(view);
            storeName = view.findViewById(R.id.storeName);
            storeDesc = view.findViewById(R.id.storeDesc);
            storeImage = view.findViewById(R.id.storeImage);

        }
    }

    public interface StoreListClickListener {
        public void onItemClick(StoreModel storeModel);
    }
}