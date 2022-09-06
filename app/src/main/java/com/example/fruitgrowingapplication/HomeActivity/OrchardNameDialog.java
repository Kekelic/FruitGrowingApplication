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
import androidx.fragment.app.DialogFragment;

import com.example.fruitgrowingapplication.Database.AppDatabase;
import com.example.fruitgrowingapplication.R;

public class OrchardNameDialog extends DialogFragment {
    private String orchardName;
    private int position;
    private EditText etNewOrchardName;
    private final OrchardNameDialogListener orchardNameDialogListener;


    public OrchardNameDialog(OrchardNameDialogListener orchardNameDialogListener) {
        this.orchardNameDialogListener = orchardNameDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_orchard_name, null);

        getArgumentsFromActivity();
        setupOrchardName(view);

        builder.setView(view)
                .setTitle(R.string.title_for_change_orchard_name_dialog)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeOrchardName();
                    }
                });
        return builder.create();

    }

    private void getArgumentsFromActivity() {
        orchardName = getArguments().getString("orchardNameDialogKey");
        position = getArguments().getInt("adapterPositionKey");
    }

    private void setupOrchardName(View view) {
        etNewOrchardName = view.findViewById(R.id.et_new_orchard_name);
        etNewOrchardName.setText(orchardName);
    }

    private void changeOrchardName() {
        AppDatabase database = AppDatabase.getInstance(getContext());
        if (TextUtils.isEmpty(etNewOrchardName.getText().toString().trim())) {
            Toast.makeText(getContext(), getString(R.string.message_for_empty_name), Toast.LENGTH_SHORT).show();
        } else {
            if (!orchardName.equals(etNewOrchardName.getText().toString())) {
                String newOrchardName = etNewOrchardName.getText().toString();
                if (TextUtils.isEmpty(database.orchardDao().searchName(newOrchardName))) {
                    database.orchardDao().setNewOrchardName(orchardName, newOrchardName);
                    orchardNameDialogListener.setNewOrchardName(position, newOrchardName);
                } else {
                    Toast.makeText(getContext(), getString(R.string.message_for_existing_name), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
