package com.example.fruitgrowingapplication.ManagementActivity.Manage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.R;

public class VerticalRecyclerAdapterForManage extends RecyclerView.Adapter<VerticalViewHolderForManage> {

    private int rows;

    @NonNull
    @Override
    public VerticalViewHolderForManage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_vertical_number, parent, false);
        return new VerticalViewHolderForManage(cellView);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolderForManage holder, int position) {
        holder.setRowNumber(position);

    }

    @Override
    public int getItemCount() {
        return rows;
    }


    public void addInformation(int rows) {
        this.rows = rows;
    }

}
