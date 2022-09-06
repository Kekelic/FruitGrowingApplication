package com.example.fruitgrowingapplication.ManagementActivity.Manage;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.R;

public class VerticalViewHolderForManage extends RecyclerView.ViewHolder {

    private final TextView tvTreeNumber;

    public VerticalViewHolderForManage(@NonNull View itemView) {
        super(itemView);
        tvTreeNumber = itemView.findViewById(R.id.tv_vertical_number);
    }

    public void setRowNumber(int position) {
        tvTreeNumber.setText(String.valueOf(position + 1));
    }
}
