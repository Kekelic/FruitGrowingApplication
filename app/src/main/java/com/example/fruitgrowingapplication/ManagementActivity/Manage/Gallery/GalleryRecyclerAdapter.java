package com.example.fruitgrowingapplication.ManagementActivity.Manage.Gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.Database.AppDatabase;
import com.example.fruitgrowingapplication.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    private List<Uri> images = new ArrayList<>();

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_picture, parent, false);
        return new GalleryViewHolder(cellView);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        holder.setImage(images.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPictureDialog(v, position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createRemoveAlterDialog(v, position);
                return true;
            }
        });
    }

    private void createPictureDialog(View view, int position) {
        DialogFragment dialogFragment = new PictureDialog(images.get(position));
        AppCompatActivity activity = ((AppCompatActivity) view.getContext());
        dialogFragment.show(activity.getSupportFragmentManager(), "Gallery");
    }

    private void createRemoveAlterDialog(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.title_for_deleting_picture);
        builder.setMessage(R.string.message_for_deleting_picture);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePicture(view, position);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void deletePicture(View view, int position) {
        AppDatabase database = AppDatabase.getInstance(view.getContext());
        database.pictureDao().deletePicture(database.pictureDao().getPicture(images.get(position)));
        removeCell(position);
    }

    @Override
    public int getItemCount() {
        return this.images.size();
    }


    public void setImages(List<Uri> images) {
        this.images.clear();
        this.images = images;
        notifyDataSetChanged();
    }

    public void addNewCell(Uri imageUri) {
        int position = 0;
        this.images.add(position, imageUri);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, images.size());
    }

    public void removeCell(int position) {
        if (images.size() > position && position >= 0) {
            images.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, images.size());
        }
    }

}
