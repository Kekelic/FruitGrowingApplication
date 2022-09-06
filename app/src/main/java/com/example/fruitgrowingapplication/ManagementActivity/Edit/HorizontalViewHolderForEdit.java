package com.example.fruitgrowingapplication.ManagementActivity.Edit;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.R;

public class HorizontalViewHolderForEdit extends RecyclerView.ViewHolder {

    private final TextView tvTreeNumber;

    public HorizontalViewHolderForEdit(@NonNull View itemView) {
        super(itemView);
        tvTreeNumber = itemView.findViewById(R.id.tv_horizontal_number);
    }

    public void setColumnNumber(int position) {
        if (getItemViewType() == 1)
            tvTreeNumber.setText(String.valueOf(position));
    }

}
