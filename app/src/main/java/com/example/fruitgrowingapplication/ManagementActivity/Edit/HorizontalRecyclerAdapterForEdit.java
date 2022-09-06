package com.example.fruitgrowingapplication.ManagementActivity.Edit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.R;

public class HorizontalRecyclerAdapterForEdit extends RecyclerView.Adapter<HorizontalViewHolderForEdit> {

    private int columns;
    private GridRecyclerAdapterForEdit gridAdapter;

    @NonNull
    @Override
    public HorizontalViewHolderForEdit onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cellView;
        if (viewType == 0) {
            cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_horizontal_add, parent, false);
        } else {
            cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_horizontal_number, parent, false);
        }
        return new HorizontalViewHolderForEdit(cellView);

    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolderForEdit holder, int position) {
        holder.setColumnNumber(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    addLeftColumn();
                } else if (position == columns - 1) {
                    addRightColumn();
                }
            }
        });
    }

    private void addLeftColumn() {
        gridAdapter.addLeftColumn(-1);
    }

    private void addRightColumn() {
        gridAdapter.addRightColumn(-1);
    }

    @Override
    public int getItemCount() {
        return columns;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == columns - 1) {
            return 0;
        } else
            return 1;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void addDataOnTheBeginning() {
        this.columns++;
        notifyItemInserted(1);
        notifyItemRangeChanged(1, columns - 1);
    }

    public void addDataOnTheEnd() {
        this.columns++;
        notifyItemInserted(columns - 2);
        notifyItemChanged(columns - 1);
    }

    public void setFriendlyGridAdapter(GridRecyclerAdapterForEdit gridAdapter) {
        this.gridAdapter = gridAdapter;
    }
}
