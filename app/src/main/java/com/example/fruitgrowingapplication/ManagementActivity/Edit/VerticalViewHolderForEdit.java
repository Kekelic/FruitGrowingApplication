package com.example.fruitgrowingapplication.ManagementActivity.Edit;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.R;

public class VerticalViewHolderForEdit extends RecyclerView.ViewHolder {

    private final TextView tvTreeNumber;

    public VerticalViewHolderForEdit(@NonNull View itemView) {
        super(itemView);
        tvTreeNumber = itemView.findViewById(R.id.tv_vertical_number);
    }

    public void setRowNumber(int position) {
        if (getItemViewType() == 1)
            tvTreeNumber.setText(String.valueOf(position));
    }
}
