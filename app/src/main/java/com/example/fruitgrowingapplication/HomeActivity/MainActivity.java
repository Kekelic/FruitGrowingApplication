package com.example.fruitgrowingapplication.HomeActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.Database.AppDatabase;
import com.example.fruitgrowingapplication.Database.Orchard;
import com.example.fruitgrowingapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OrchardClickListener, DataEntryDialogListener, OrchardNameDialogListener {

    private static final int STARTING_PERMISSIONS_CODE = 99;
    private static final int IMAGE_PICK_CODE = 100;
    private static final int IMAGE_PICK_PERMISSION_CODE = 101;
    private static final int CAMERA_CODE = 102;
    private static final int CAMERA_PERMISSION_CODE = 103;
    private static final int IMAGE_WRITE_PERMISSION_CODE = 104;
    private EditText etOrchardName;
    private RecyclerView orchardRecyclerView;
    private OrchardRecyclerAdapter orchardRecyclerAdapter;
    private String currentOrchardName;
    private RelativeLayout rlCellOrchard;
    private BitmapDrawable bitmapDrawable;
    private Uri currentImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocate();
        setContentView(R.layout.activity_main);
        setupRecycler();
        setupRecyclerData();
        etOrchardName = findViewById(R.id.et_orchard_name);
        setupLanguageButton();
        setupCreateButton();
        askForPermissions();
    }


    private void setupRecycler() {
        orchardRecyclerView = findViewById(R.id.orchard_recycle_view);
        orchardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orchardRecyclerAdapter = new OrchardRecyclerAdapter(this, this);
        orchardRecyclerView.setAdapter(orchardRecyclerAdapter);
    }

    private void setupRecyclerData() {
        AppDatabase database = AppDatabase.getInstance(getBaseContext());
        orchardRecyclerAdapter.setOrchardNames(database.orchardDao().getOrchardNames());
    }

    private void setupLanguageButton() {
        ImageView ivLanguage = findViewById(R.id.iv_activity_image);
        ivLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });
    }

    private void showChangeLanguageDialog() {
        final String[] languages = {getString(R.string.english), getString(R.string.croatian)};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.title_for_change_language_dialog);
        builder.setSingleChoiceItems(languages, getCheckedLanguage(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0 && getCheckedLanguage() != 0) {
                    setLocale("en");
                    recreate();
                }
                if (which == 1 && getCheckedLanguage() != 1) {
                    setLocale("hr");
                    recreate();
                }
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int getCheckedLanguage() {
        String language = getResources().getConfiguration().locale.getLanguage();
        if (language.equals("en")) {
            return 0;
        } else if (language.equals("hr"))
            return 1;
        else return -1;
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", language);
        editor.apply();
    }

    private void loadLocate() {
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        setLocale(language);
    }


    private void setupCreateButton() {
        Button bCreate = findViewById(R.id.b_create);
        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase database = AppDatabase.getInstance(getBaseContext());
                String orchardName = etOrchardName.getText().toString().trim();
                if (TextUtils.isEmpty(etOrchardName.getText().toString().trim()))
                    Toast.makeText(MainActivity.this, getString(R.string.message_for_empty_name), Toast.LENGTH_SHORT).show();
                else {
                    if (TextUtils.isEmpty(database.orchardDao().searchName(orchardName))) {
                        openDataEntryDialog(orchardName);

                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.message_for_existing_name), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void openDataEntryDialog(String orchardName) {
        DialogFragment dialogFragment = new DataEntryDialog(this);
        Bundle args = new Bundle();
        args.putString("dialogKey", orchardName);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "data entry");
    }

    @Override
    public void addNewOrchardCell() {
        orchardRecyclerAdapter.addNewCell(etOrchardName.getText().toString().trim());
        orchardRecyclerView.scrollToPosition(0);
        etOrchardName.getText().clear();
    }

    private void askForPermissions() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            requestPermissions(permissions, STARTING_PERMISSIONS_CODE);
        }

    }

    @Override
    public void onDeleteClick(int position, RelativeLayout rlCellOrchard) {
        this.rlCellOrchard = rlCellOrchard;
        makeDeletingOrchardDialog(position);
    }

    private void makeDeletingOrchardDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.title_for_deleting_orchard);
        builder.setMessage(R.string.message_for_deleting_orchard);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteOrchard(position);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteOrchard(int position) {
        rlCellOrchard.setBackground(null);
        AppDatabase database = AppDatabase.getInstance(getBaseContext());
        Orchard orchard = database.orchardDao().getOrchard(orchardRecyclerAdapter.getOrchardNames().get(position));
        database.orchardDao().deleteOrchard(orchard);
        orchardRecyclerAdapter.removeCell(position);
    }

    @Override
    public void onImageClick(String currentOrchardName, RelativeLayout rlCellOrchard) {
        this.rlCellOrchard = rlCellOrchard;
        this.currentOrchardName = currentOrchardName;
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions, IMAGE_PICK_PERMISSION_CODE);
        } else {
            pickImageFromGallery();
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    @Override
    public void onCameraClick(String currentOrchardName, RelativeLayout rlCellOrchard) {
        this.rlCellOrchard = rlCellOrchard;
        this.currentOrchardName = currentOrchardName;
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = {Manifest.permission.CAMERA};
            requestPermissions(permissions, CAMERA_PERMISSION_CODE);
        } else {
            takePicture();
        }

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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Uri getImageUriNewerVersion() {
        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "FruitGrowingPictures");
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    private Uri getImageUri() throws IOException {
        String fileName = "" + System.currentTimeMillis();
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
        return FileProvider.getUriForFile(MainActivity.this, "com.example.fruitgrowingapplication.fileprovider", imageFile);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case IMAGE_PICK_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.message_for_denied_permission), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case CAMERA_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.message_for_denied_permission), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case IMAGE_WRITE_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePicture();
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.message_for_denied_permission), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            Uri imageURI = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                rlCellOrchard.setBackground(drawable);
                bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                saveToExternalStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (resultCode == RESULT_OK && requestCode == CAMERA_CODE) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), currentImageUri);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                rlCellOrchard.setBackground(drawable);
                bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppDatabase.getInstance(getBaseContext()).orchardDao().setOrchardImageUri(currentOrchardName, currentImageUri);
                } else {
                    saveToExternalStorage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void saveToExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            savePictureNewerVersion();
        } else {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, IMAGE_WRITE_PERMISSION_CODE);
            } else {
                savePicture();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void savePictureNewerVersion() {
        try {
            OutputStream fos;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            Uri imageUri = getImageUriNewerVersion();
            ContentResolver resolver = getContentResolver();
            fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Objects.requireNonNull(fos);
            AppDatabase.getInstance(getBaseContext()).orchardDao().setOrchardImageUri(currentOrchardName, imageUri);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void savePicture() {
        Bitmap bitmap = bitmapDrawable.getBitmap();
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            AppDatabase.getInstance(getBaseContext()).orchardDao().setOrchardImageUri(currentOrchardName, Uri.fromFile(outFile));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setNewOrchardName(int position, String newOrchardName) {
        orchardRecyclerAdapter.setOrchardName(position, newOrchardName);
    }
}

