package com.example.fruitgrowingapplication.ManagementActivity.Info;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.fruitgrowingapplication.BuildConfig;
import com.example.fruitgrowingapplication.R;

public class InfoDialog extends AppCompatDialogFragment {

    private TextView tvVersion;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_info, null);

        initializeUI(view);
        builder.setView(view)
                .setNeutralButton(R.string.Close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    private void initializeUI(View view) {
        String versionName = BuildConfig.VERSION_NAME;
        tvVersion = view.findViewById(R.id.tv_version);
        tvVersion.setText(getString(R.string.version) + " " + versionName);
    }
}
