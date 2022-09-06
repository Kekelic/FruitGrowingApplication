package com.example.fruitgrowingapplication.ManagementActivity.Manage.Gallery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.Database.AppDatabase;
import com.example.fruitgrowingapplication.Database.Picture;
import com.example.fruitgrowingapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class GalleryDialog extends DialogFragment {

    private static final int IMAGE_PICK_CODE = 101;
    private static final int CAMERA_CODE = 102;
    private final String orchardName;
    private final int position;
    private final AppDatabase database = AppDatabase.getInstance(getContext());
    private Bitmap currentBitmap = null;
    private Uri currentImageUri;
    private ImageView ivGalleryCamera, ivGalleryImage;
    private RecyclerView recyclerGallery;
    private GalleryRecyclerAdapter galleryRecyclerAdapter;


    public GalleryDialog(String orchardName, int position) {
        this.orchardName = orchardName;
        this.position = position;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_gallery, null);

        setupCameraButton(view);
        setupGalleryButton(view);
        setupRecycler(view);
        setupRecyclerData();

        builder.setView(view);
        return builder.create();
    }

    private void setupRecycler(View view) {
        recyclerGallery = view.findViewById(R.id.recycler_gallery);
        recyclerGallery.setLayoutManager(new GridLayoutManager(getContext(), 2));
        galleryRecyclerAdapter = new GalleryRecyclerAdapter();
        recyclerGallery.setAdapter(galleryRecyclerAdapter);
    }

    private void setupRecyclerData() {
        int orchardID = database.orchardDao().getID(orchardName);
        int treeID = database.treeDao().getTreeID(orchardID, position);
        galleryRecyclerAdapter.setImages(database.pictureDao().getPictureUris(treeID));
    }


    private void setupCameraButton(View view) {
        ivGalleryCamera = view.findViewById(R.id.iv_gallery_camera);
        ivGalleryCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    private void takePicture() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                currentImageUri = getImageUriNewerVersion();
            } else {
                currentImageUri = getImageUri();
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri);
            startActivityForResult(intent, CAMERA_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setupGalleryButton(View view) {
        ivGalleryImage = view.findViewById(R.id.iv_gallery_image);
        ivGalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Uri getImageUriNewerVersion() {
        ContentResolver resolver = getContext().getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "FruitGrowingPictures");
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    private Uri getImageUri() throws IOException {
        String fileName = "" + System.currentTimeMillis();
        File storageDirectory = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
        return FileProvider.getUriForFile(getContext(), "com.example.fruitgrowingapplication.fileprovider", imageFile);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            Uri imageURI = data.getData();
            try {
                currentBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageURI);
                saveToExternalStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_CODE) {
            try {
                currentBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), currentImageUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Picture picture = new Picture(database.treeDao().getTreeID(database.orchardDao().getID(orchardName), position), currentImageUri);
                    database.pictureDao().insertPicture(picture);
                    galleryRecyclerAdapter.addNewCell(currentImageUri);
                } else {
                    saveToExternalStorage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveToExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            savePictureNewerVersion();
        } else {
            savePicture();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void savePictureNewerVersion() {
        try {
            OutputStream fos;
            Uri imageUri = getImageUriNewerVersion();
            ContentResolver resolver = getContext().getContentResolver();
            fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            currentBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Objects.requireNonNull(fos);
            Picture picture = new Picture(database.treeDao().getTreeID(database.orchardDao().getID(orchardName), position), imageUri);
            database.pictureDao().insertPicture(picture);
            galleryRecyclerAdapter.addNewCell(imageUri);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void savePicture() {
        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/FruitGrowingPictures");
        if (!dir.exists()) {
            dir.mkdir();
        }
        String filename = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, filename);
        try {
            outputStream = new FileOutputStream(outFile);
            currentBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Picture picture = new Picture(database.treeDao().getTreeID(database.orchardDao().getID(orchardName), position), Uri.fromFile(outFile));
            database.pictureDao().insertPicture(picture);
            galleryRecyclerAdapter.addNewCell(Uri.fromFile(outFile));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
