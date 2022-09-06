package com.example.fruitgrowingapplication.ManagementActivity.Manage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fruitgrowingapplication.Database.AppDatabase;
import com.example.fruitgrowingapplication.Database.TreeGrowth;
import com.example.fruitgrowingapplication.Database.TreeHealth;
import com.example.fruitgrowingapplication.Database.TreeSize;
import com.example.fruitgrowingapplication.Database.TreeYield;
import com.example.fruitgrowingapplication.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TreeDataDialog extends DialogFragment {


    private String orchardName;
    private int position;
    private final AppDatabase database = AppDatabase.getInstance(getContext());
    private final TreeDataDialogListener treeDataDialogListener;
    private AutoCompleteTextView autoCompleteHealthText, autoCompleteYieldText, autoCompleteSizeText, autoCompleteGrowthText;
    private TreeArrayAdapter treeHealthArrayAdapter, treeYieldArrayAdapter, treeSizeArrayAdapter, treeGrowthArrayAdapter;
    private EditText etType, etVariety, etRootstock, etPlantingDate, etNotes;
    private CheckBox cbHasRootstock, cbIsForReplace;


    public TreeDataDialog(TreeDataDialogListener treeDataDialogListener) {
        this.treeDataDialogListener = treeDataDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_tree_data, null);

        getArgumentsFromActivity();
        initializeUI(view);
        setArrayAdapters();
        setDialogData();
        builder.setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int orchardID = database.orchardDao().getID(orchardName);
                        saveFruitType(orchardID);
                        saveFruitVariety(orchardID);
                        saveFruitRootstock(orchardID);
                        saveTreePlantingDate(orchardID);
                        saveTreeHealth(orchardID);
                        saveTreeYield(orchardID);
                        saveTreeSize(orchardID);
                        saveTreeGrowth(orchardID);
                        saveTreeForReplace(orchardID);
                        saveTreeNotes(orchardID);
                        treeDataDialogListener.notifyGridItemChanged(position);
                    }
                });
        return builder.create();
    }

    private void getArgumentsFromActivity() {
        orchardName = getArguments().getString("OrchardNameKey");
        position = getArguments().getInt("PositionKey");
    }


    private void initializeUI(View view) {
        etType = view.findViewById(R.id.et_type);
        etVariety = view.findViewById(R.id.et_variety);
        etRootstock = view.findViewById(R.id.et_rootstock);
        cbHasRootstock = view.findViewById(R.id.cb_has_rootstock);
        cbHasRootstock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etRootstock.setVisibility(View.VISIBLE);
                } else {
                    etRootstock.setVisibility(View.GONE);
                    etRootstock.setText("");
                }
            }
        });
        etPlantingDate = view.findViewById(R.id.et_planting_date);
        autoCompleteHealthText = view.findViewById(R.id.auto_complete_text_health);
        autoCompleteYieldText = view.findViewById(R.id.auto_complete_text_yield);
        autoCompleteSizeText = view.findViewById(R.id.auto_complete_text_size);
        autoCompleteGrowthText = view.findViewById(R.id.auto_complete_text_growth);
        cbIsForReplace = view.findViewById(R.id.cb_is_for_replace);
        etNotes = view.findViewById(R.id.et_notes);
    }

    private void setArrayAdapters() {
        treeHealthArrayAdapter = new TreeArrayAdapter(getContext(), getResources().getStringArray(R.array.health_options), getHealthImagesIds());
        treeYieldArrayAdapter = new TreeArrayAdapter(getContext(), getResources().getStringArray(R.array.yield_options), getYieldImagesIds());
        treeSizeArrayAdapter = new TreeArrayAdapter(getContext(), getResources().getStringArray(R.array.size_options), getSizeImagesIds());
        treeGrowthArrayAdapter = new TreeArrayAdapter(getContext(), getResources().getStringArray(R.array.growth_options), getGrowthImagesIds());
    }

    private List<Integer> getGrowthImagesIds() {
        List<Integer> growthImages = new ArrayList<>();
        growthImages.add(R.drawable.background_not_rated);
        growthImages.add(R.drawable.background_nothing);
        growthImages.add(R.drawable.background_bad);
        growthImages.add(R.drawable.background_good);
        growthImages.add(R.drawable.background_very_good);
        growthImages.add(R.drawable.background_excellent);
        return growthImages;
    }

    private List<Integer> getSizeImagesIds() {
        List<Integer> sizeImages = new ArrayList<>();
        sizeImages.add(R.drawable.size_not_rated);
        sizeImages.add(R.drawable.size_small);
        sizeImages.add(R.drawable.size_medium);
        sizeImages.add(R.drawable.size_large);
        return sizeImages;
    }

    private List<Integer> getYieldImagesIds() {
        List<Integer> yieldImages = new ArrayList<>();
        yieldImages.add(R.drawable.yield_not_rated);
        yieldImages.add(R.drawable.yield_nothing);
        yieldImages.add(R.drawable.yield_bad);
        yieldImages.add(R.drawable.yield_good);
        yieldImages.add(R.drawable.yield_very_good);
        yieldImages.add(R.drawable.yield_excellent);
        return yieldImages;
    }

    private List<Integer> getHealthImagesIds() {
        List<Integer> healthImages = new ArrayList<>();
        healthImages.add(R.drawable.health_not_rated);
        healthImages.add(R.drawable.health_dried);
        healthImages.add(R.drawable.health_diseased);
        healthImages.add(R.drawable.health_attacked_by_pests);
        healthImages.add(R.drawable.health_small_damage);
        healthImages.add(R.drawable.health_healthy);
        return healthImages;
    }

    private void setDialogData() {
        int orchardID = database.orchardDao().getID(orchardName);
        setDialogFruitType(orchardID);
        setDialogFruitVariety(orchardID);
        setDialogFruitRootstock(orchardID);
        setDialogTreePlantingDate(orchardID);
        setDialogTreeHealth(orchardID);
        setDialogTreeYield(orchardID);
        setDialogTreeSize(orchardID);
        setDialogTreeGrowth(orchardID);
        setDialogForReplace(orchardID);
        setDialogTreeNotes(orchardID);
    }

    private void setDialogFruitType(int orchardID) {
        String fruitType = database.treeDao().getFruitType(orchardID, position);
        if (fruitType != null) {
            etType.setText(fruitType);
        }
    }

    private void setDialogFruitVariety(int orchardID) {
        String fruitVariety = database.treeDao().getFruitVariety(orchardID, position);
        if (fruitVariety != null) {
            etVariety.setText(fruitVariety);
        }
    }

    private void setDialogFruitRootstock(int orchardID) {
        String fruitRootstock = database.treeDao().getFruitRootstock(orchardID, position);

        if (fruitRootstock.equals("none")) {
            etRootstock.setText("");
            etRootstock.setVisibility(View.GONE);
            cbHasRootstock.setChecked(false);
        } else {
            etRootstock.setText(fruitRootstock);
            cbHasRootstock.setChecked(true);
        }
    }

    private void setDialogTreePlantingDate(int orchardID) {
        String plantingDate = database.treeDao().getTreePlantingDate(orchardID, position);
        if (plantingDate != null) {
            etPlantingDate.setText(plantingDate);
        }
    }

    private void setDialogTreeHealth(int orchardID) {
        for (int i = 0; i < treeHealthArrayAdapter.getCount(); i++) {
            if (database.treeDao().getTreeHealth(orchardID, position) == TreeHealth.values()[i]) {
                autoCompleteHealthText.setText(treeHealthArrayAdapter.getItem(i));
            }
        }
        autoCompleteHealthText.setAdapter(treeHealthArrayAdapter);
    }

    private void setDialogTreeYield(int orchardID) {
        for (int i = 0; i < treeYieldArrayAdapter.getCount(); i++) {
            if (database.treeDao().getTreeYield(orchardID, position) == TreeYield.values()[i]) {
                autoCompleteYieldText.setText(treeYieldArrayAdapter.getItem(i));
            }
        }
        autoCompleteYieldText.setAdapter(treeYieldArrayAdapter);
    }

    private void setDialogTreeSize(int orchardID) {
        for (int i = 0; i < treeSizeArrayAdapter.getCount(); i++) {
            if (database.treeDao().getTreeSize(orchardID, position) == TreeSize.values()[i]) {
                autoCompleteSizeText.setText(treeSizeArrayAdapter.getItem(i));
            }
        }
        autoCompleteSizeText.setAdapter(treeSizeArrayAdapter);
    }

    private void setDialogTreeGrowth(int orchardID) {
        for (int i = 0; i < treeGrowthArrayAdapter.getCount(); i++) {
            if (database.treeDao().getTreeGrowth(orchardID, position) == TreeGrowth.values()[i]) {
                autoCompleteGrowthText.setText(treeGrowthArrayAdapter.getItem(i));
            }
        }
        autoCompleteGrowthText.setAdapter(treeGrowthArrayAdapter);
    }

    private void setDialogForReplace(int orchardID) {
        boolean isForReplace = database.treeDao().isForReplace(orchardID, position);
        cbIsForReplace.setChecked(isForReplace);

    }

    private void setDialogTreeNotes(int orchardID) {
        String treeNotes = database.treeDao().getTreeNotes(orchardID, position);
        if (treeNotes != null) {
            etNotes.setText(treeNotes);
        }
    }

    private void saveFruitType(int orchardID) {
        String fruitType = etType.getText().toString().trim();
        if (!TextUtils.isEmpty(fruitType)) {
            database.treeDao().setFruitType(orchardID, position, fruitType);
        }
    }

    private void saveFruitVariety(int orchardID) {
        String fruitVariety = etVariety.getText().toString().trim();
        if (!TextUtils.isEmpty(fruitVariety)) {
            database.treeDao().setFruitVariety(orchardID, position, fruitVariety);
        }
    }

    private void saveFruitRootstock(int orchardID) {
        if (cbHasRootstock.isChecked()) {
            String fruitRootstock = etRootstock.getText().toString().trim();
            if (!TextUtils.isEmpty(fruitRootstock)) {
                database.treeDao().setFruitRootstock(orchardID, position, fruitRootstock);
            } else {
                database.treeDao().setFruitRootstock(orchardID, position, "none");
            }
        } else {
            database.treeDao().setFruitRootstock(orchardID, position, "none");
        }

    }

    private void saveTreePlantingDate(int orchardID) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date plantingDate;
        try {
            dateFormat.setLenient(false);
            plantingDate = dateFormat.parse(etPlantingDate.getText().toString());
            String date = new SimpleDateFormat("dd/MM/yyyy").format(plantingDate);
            database.treeDao().setTreePlantingDate(orchardID, position, date);
        } catch (ParseException e) {
            Toast.makeText(getContext(), getString(R.string.message_for_wrong_date_entry), Toast.LENGTH_SHORT).show();
        }


    }


    private void saveTreeHealth(int orchardID) {
        for (int i = 0; i < treeHealthArrayAdapter.getCount(); i++) {
            if (autoCompleteHealthText.getText().toString().equals(treeHealthArrayAdapter.getItem(i))) {
                database.treeDao().setTreeHealth(orchardID, position, TreeHealth.values()[i]);
            }
        }
    }

    private void saveTreeYield(int orchardID) {
        for (int i = 0; i < treeYieldArrayAdapter.getCount(); i++) {
            if (autoCompleteYieldText.getText().toString().equals(treeYieldArrayAdapter.getItem(i))) {
                database.treeDao().setTreeYield(orchardID, position, TreeYield.values()[i]);
            }
        }
    }

    private void saveTreeSize(int orchardID) {
        for (int i = 0; i < treeSizeArrayAdapter.getCount(); i++) {
            if (autoCompleteSizeText.getText().toString().equals(treeSizeArrayAdapter.getItem(i))) {
                database.treeDao().setTreeSize(orchardID, position, TreeSize.values()[i]);
            }
        }
    }

    private void saveTreeGrowth(int orchardID) {
        for (int i = 0; i < treeGrowthArrayAdapter.getCount(); i++) {
            if (autoCompleteGrowthText.getText().toString().equals(treeGrowthArrayAdapter.getItem(i))) {
                database.treeDao().setTreeGrowth(orchardID, position, TreeGrowth.values()[i]);
            }
        }
    }

    private void saveTreeForReplace(int orchardID) {
        database.treeDao().setForReplace(orchardID, position, cbIsForReplace.isChecked());
    }

    private void saveTreeNotes(int orchardID) {
        String treeNotes = etNotes.getText().toString().trim();
        if (!TextUtils.isEmpty(treeNotes)) {
            database.treeDao().setTreeNotes(orchardID, position, treeNotes);
        }
    }
}
