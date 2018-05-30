package com.diplomna.diplomna.Models;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diplomna.diplomna.R;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView
            .Adapter<MyRecyclerViewAdapter
            .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<ListingData> mDataset;
    private Context mContext;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView prodName;
        TextView dealerName;
        TextView ppUnit;
        TextView units;

        ImageView image;

        public DataObjectHolder(View itemView) {
            super(itemView);
            prodName = itemView.findViewById(R.id.productName);
            dealerName = itemView.findViewById(R.id.dealerName);
            ppUnit = itemView.findViewById(R.id.pricePerUnit);
            units = itemView.findViewById(R.id.units);
            image = itemView.findViewById(R.id.productImage);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(Context mContext, ArrayList<ListingData> myDataset) {
        mDataset = myDataset;
        mContext = mContext;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_single_listing_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.prodName.setText(mDataset.get(position).getProductName());
        holder.dealerName.setText(mDataset.get(position).getDealerName());
        holder.ppUnit.setText(mDataset.get(position).getPricePerUnit());
        holder.units.setText(mDataset.get(position).getUnits());
        Glide.with(mContext).load(mDataset.get(position).getImage()).into(holder.image);
    }

    public void addItem(ListingData dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}

