package com.example.fruitgrowingapplication.ManagementActivity.Manage.Gallery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fruitgrowingapplication.R;

public class PictureDialog extends DialogFragment {

    private final Uri imageUri;
    private ImageView ivImage;


    public PictureDialog(Uri imageUri) {
        this.imageUri = imageUri;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_picture, null);
        initializeUI(view);
        builder.setView(view);
        return builder.create();
    }

    private void initializeUI(View view) {
        ivImage = view.findViewById(R.id.iv_image);
        ivImage.setImageURI(imageUri);
        if (ivImage.getDrawable() == null) {
            ivImage.setImageResource(R.drawable.ic_image);
        }
    }

}
