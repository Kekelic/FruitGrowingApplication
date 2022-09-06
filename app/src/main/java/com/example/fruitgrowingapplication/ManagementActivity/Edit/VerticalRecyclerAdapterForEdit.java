package com.example.fruitgrowingapplication.ManagementActivity.Edit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.R;

public class VerticalRecyclerAdapterForEdit extends RecyclerView.Adapter<VerticalViewHolderForEdit> {

    private int rows;
    private GridRecyclerAdapterForEdit gridAdapter;

    @NonNull
    @Override
    public VerticalViewHolderForEdit onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cellView;
        if (viewType == 0) {
            cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_vertical_add, parent, false);
        } else {
            cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_vertical_number, parent, false);
        }
        return new VerticalViewHolderForEdit(cellView);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolderForEdit holder, int position) {
        holder.setRowNumber(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    addAboveRow();
                } else if (position == rows - 1) {
                    addBottomRow();
                }
            }
        });

    }

    private void addAboveRow() {
        gridAdapter.addAboveRow(-1);
    }

    private void addBottomRow() {
        gridAdapter.addBottomRow(-1);
    }

    @Override
    public int getItemCount() {
        return rows;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == rows - 1) {
            return 0;
        } else
            return 1;
    }


    public void setRows(int rows) {
        this.rows = rows;
    }

    public void addDataOnTheEnd() {
        this.rows++;
        notifyItemInserted(rows - 2);
        notifyItemChanged(rows - 1);

    }

    public void addDataOnTheBeginning() {
        this.rows++;
        notifyItemInserted(1);
        notifyItemRangeChanged(1, rows - 1);

    }

    public void setFriendlyGridAdapter(GridRecyclerAdapterForEdit gridAdapter) {
        this.gridAdapter = gridAdapter;
    }
}
