package com.example.fruitgrowingapplication.ManagementActivity.Manage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.R;

public class HorizontalRecyclerAdapterForManage extends RecyclerView.Adapter<HorizontalViewHolderForManage> {

    private int columns;

    @NonNull
    @Override
    public HorizontalViewHolderForManage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_horizontal_number, parent, false);
        return new HorizontalViewHolderForManage(cellView);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolderForManage holder, int position) {
        holder.setColumnNumber(position);
    }

    @Override
    public int getItemCount() {
        return columns;
    }


    public void addInformation(int columns) {
        this.columns = columns;
    }
}
