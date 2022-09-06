package com.example.fruitgrowingapplication.HomeActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.ManagementActivity.TreeManagementActivity;
import com.example.fruitgrowingapplication.R;

import java.util.ArrayList;
import java.util.List;

public class OrchardRecyclerAdapter extends RecyclerView.Adapter<OrchardViewHolder> {


    private final List<String> orchardNames = new ArrayList<>();
    private final OrchardClickListener orchardClickListener;
    private final OrchardNameDialogListener orchardNameDialogListener;


    public OrchardRecyclerAdapter(OrchardClickListener orchardClickListener, OrchardNameDialogListener orchardNameDialogListener) {
        this.orchardClickListener = orchardClickListener;
        this.orchardNameDialogListener = orchardNameDialogListener;
    }

    @NonNull
    @Override
    public OrchardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_orchard, parent, false);
        return new OrchardViewHolder(cellView, orchardClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrchardViewHolder holder, int position) {
        holder.setOrchardData(orchardNames.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDisplayOfTreesActivity(v, position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openOrchardNameDialog(v, position);
                return true;
            }
        });
    }

    private void openDisplayOfTreesActivity(View view, int position) {
        Context context = view.getContext();
        Intent intent = new Intent(context, TreeManagementActivity.class);
        intent.putExtra("nameKey", orchardNames.get(position));
        context.startActivity(intent);
    }

    private void openOrchardNameDialog(View view, int position) {
        DialogFragment dialogFragment = new OrchardNameDialog(orchardNameDialogListener);
        Bundle args = new Bundle();
        args.putString("orchardNameDialogKey", orchardNames.get(position));
        args.putInt("adapterPositionKey", position);
        dialogFragment.setArguments(args);
        AppCompatActivity activity = ((AppCompatActivity) view.getContext());
        dialogFragment.show(activity.getSupportFragmentManager(), "data entry");
    }

    @Override
    public int getItemCount() {
        return orchardNames.size();
    }

    public void setOrchardNames(List<String> orchardNames) {
        this.orchardNames.clear();
        this.orchardNames.addAll(orchardNames);
        notifyDataSetChanged();
    }

    public void setOrchardName(int position, String orchardName) {
        this.orchardNames.set(position, orchardName);
        notifyItemChanged(position);
    }

    public void addNewCell(String orchardName) {
        int position = 0;
        orchardNames.add(position, orchardName);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, orchardNames.size());
    }

    public void removeCell(int position) {
        if (orchardNames.size() > position && position >= 0) {
            orchardNames.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, orchardNames.size());
        }
    }

    public List<String> getOrchardNames() {
        return this.orchardNames;
    }
}
