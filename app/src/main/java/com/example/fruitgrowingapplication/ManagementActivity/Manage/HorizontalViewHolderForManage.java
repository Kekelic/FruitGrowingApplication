package com.example.fruitgrowingapplication.ManagementActivity.Manage;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.R;

public class HorizontalViewHolderForManage extends RecyclerView.ViewHolder {

    private final TextView tvTreeNumber;

    public HorizontalViewHolderForManage(@NonNull View itemView) {
        super(itemView);
        tvTreeNumber = itemView.findViewById(R.id.tv_horizontal_number);
    }

    public void setColumnNumber(int position) {
        tvTreeNumber.setText(String.valueOf(position + 1));
    }
}
