package com.example.fruitgrowingapplication.HomeActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.Database.AppDatabase;
import com.example.fruitgrowingapplication.R;

import java.io.IOException;

public class OrchardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected TextView tvOrchardName;
    private final ImageView ivDelete, ivGallery, ivCamera;
    private final OrchardClickListener orchardClickListener;
    private final RelativeLayout rlCellOrchard;

    public OrchardViewHolder(@NonNull View itemView, OrchardClickListener orchardClickListener) {
        super(itemView);

        this.orchardClickListener = orchardClickListener;

        tvOrchardName = itemView.findViewById(R.id.tv_orchard_name);
        rlCellOrchard = itemView.findViewById(R.id.rl_cell_orchard);

        ivDelete = itemView.findViewById(R.id.iv_delete);
        ivDelete.setOnClickListener(this);

        ivGallery = itemView.findViewById(R.id.iv_gallery);
        ivGallery.setOnClickListener(this);

        ivCamera = itemView.findViewById(R.id.iv_camera);
        ivCamera.setOnClickListener(this);


    }

    public void setOrchardData(String orchardName) {
        tvOrchardName.setText(orchardName);
        try {
            Uri imageURI = AppDatabase.getInstance(itemView.getContext()).orchardDao().getOrchardImageURI(orchardName);
            if (imageURI != null) {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(itemView.getContext().getContentResolver(), imageURI);
                Drawable drawable = new BitmapDrawable(itemView.getResources(), bitmap);
                rlCellOrchard.setBackground(drawable);
            } else {
                rlCellOrchard.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.color.white));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == ivDelete.getId()) {
            orchardClickListener.onDeleteClick(getAdapterPosition(), rlCellOrchard);
        } else if (v.getId() == ivGallery.getId()) {
            orchardClickListener.onImageClick(tvOrchardName.getText().toString(), rlCellOrchard);
        } else if (v.getId() == ivCamera.getId()) {
            orchardClickListener.onCameraClick(tvOrchardName.getText().toString(), rlCellOrchard);
        }

    }


}
