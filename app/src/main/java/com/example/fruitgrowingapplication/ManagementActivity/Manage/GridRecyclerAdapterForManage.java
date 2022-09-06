package com.example.fruitgrowingapplication.ManagementActivity.Manage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.Database.AppDatabase;
import com.example.fruitgrowingapplication.Database.Tree;
import com.example.fruitgrowingapplication.ManagementActivity.Manage.Gallery.GalleryDialog;
import com.example.fruitgrowingapplication.R;

public class GridRecyclerAdapterForManage extends RecyclerView.Adapter<GridViewHolderForManage> {

    private int columns;
    private int rows;
    private String orchardName;
    private final TreeDataDialogListener treeDataDialogListener;
    private final AppDatabase database;

    public GridRecyclerAdapterForManage(Context context, TreeDataDialogListener treeDataDialogListener) {
        this.treeDataDialogListener = treeDataDialogListener;
        database = AppDatabase.getInstance(context);
    }


    @NonNull
    @Override
    public GridViewHolderForManage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_tree, parent, false);
        return new GridViewHolderForManage(cellView);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolderForManage holder, int position) {
        Tree tree = database.treeDao().getTree(database.orchardDao().getID(orchardName), position);
        holder.setTrees(tree);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (database.treeDao().isVisible(database.orchardDao().getID(orchardName), position)) {
                    createTreeDataDialog(v, position);
                }

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (database.treeDao().isVisible(database.orchardDao().getID(orchardName), position)) {
                    if (v.getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            v.getContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        createGalleryDialog(v, position);
                    } else {
                        Toast.makeText(v.getContext(), R.string.message_for_denied_action, Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }

    private void createTreeDataDialog(View view, int position) {
        DialogFragment dialogFragment = new TreeDataDialog(treeDataDialogListener);
        Bundle args = new Bundle();
        args.putString("OrchardNameKey", orchardName);
        args.putInt("PositionKey", position);
        dialogFragment.setArguments(args);
        AppCompatActivity activity = ((AppCompatActivity) view.getContext());
        dialogFragment.show(activity.getSupportFragmentManager(), "Tree data");
    }

    private void createGalleryDialog(View view, int position) {
        DialogFragment galleryDialogFragment = new GalleryDialog(orchardName, position);
        AppCompatActivity activity = ((AppCompatActivity) view.getContext());
        galleryDialogFragment.show(activity.getSupportFragmentManager(), "Gallery");
    }

    @Override
    public int getItemCount() {
        return rows * columns;
    }


    public void addInformation(String orchardName, int rows, int columns) {
        this.orchardName = orchardName;
        this.rows = rows;
        this.columns = columns;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
