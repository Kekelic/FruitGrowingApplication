package com.example.fruitgrowingapplication.HomeActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.fruitgrowingapplication.Database.AppDatabase;
import com.example.fruitgrowingapplication.Database.Orchard;
import com.example.fruitgrowingapplication.Database.Tree;
import com.example.fruitgrowingapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DataEntryDialog extends AppCompatDialogFragment {

    private String orchardName;
    private EditText etRows;
    private EditText etColumns;
    private final DataEntryDialogListener dataEntryDialogListener;


    public DataEntryDialog(DataEntryDialogListener dataEntryDialogListener) {
        this.dataEntryDialogListener = dataEntryDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_data_entry, null);

        getArgumentsFromActivity();
        initializeUI(view);

        builder.setView(view)
                .setTitle(R.string.title_for_orchard_data_entry_dialog)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String rowsOfET = etRows.getText().toString().trim();
                        String columnsOfET = etColumns.getText().toString().trim();
                        if (TextUtils.isEmpty(rowsOfET) || TextUtils.isEmpty(columnsOfET) ||
                                Integer.parseInt(rowsOfET) == 0 || Integer.parseInt(columnsOfET) == 0) {
                            Toast.makeText(getContext(), getString(R.string.message_for_invalid_data), Toast.LENGTH_LONG).show();
                        } else {
                            createNewOrchard(Integer.parseInt(rowsOfET), Integer.parseInt(columnsOfET));
                        }
                    }
                });
        return builder.create();
    }

    private void getArgumentsFromActivity() {
        orchardName = getArguments().getString("dialogKey");
    }

    private void initializeUI(View view) {
        etRows = view.findViewById(R.id.et_rows);
        etColumns = view.findViewById(R.id.et_columns);
    }

    private void createNewOrchard(int rows, int columns) {
        Orchard orchard = new Orchard();
        orchard.setName(orchardName);
        AppDatabase database = AppDatabase.getInstance(getContext());
        database.orchardDao().insertOrchard(orchard);
        database.orchardDao().update(orchardName, rows, columns);
        int orchardID = database.orchardDao().getID(orchardName);
        List<Tree> trees = new ArrayList<>();
        for (int i = 0; i < rows * columns; i++) {
            Tree tree = new Tree(orchardID, i, true);
            trees.add(tree);
        }
        database.treeDao().insertAll(trees);
        dataEntryDialogListener.addNewOrchardCell();
    }
}
