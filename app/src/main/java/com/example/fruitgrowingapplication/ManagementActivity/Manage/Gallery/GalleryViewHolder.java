package com.example.fruitgrowingapplication.ManagementActivity.Manage.Gallery;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.R;

public class GalleryViewHolder extends RecyclerView.ViewHolder {

    ImageView ivCellPicture;

    public GalleryViewHolder(@NonNull View itemView) {
        super(itemView);
        ivCellPicture = itemView.findViewById(R.id.iv_cell_picture);
    }

    public void setImage(Uri imageUri) {
        ivCellPicture.setImageURI(imageUri);
        if (ivCellPicture.getDrawable() == null) {
            ivCellPicture.setImageResource(R.drawable.ic_image);
        }
    }
}
