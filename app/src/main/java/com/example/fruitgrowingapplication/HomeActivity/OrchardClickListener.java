package com.example.fruitgrowingapplication.HomeActivity;

import android.widget.RelativeLayout;

public interface OrchardClickListener {
    void onDeleteClick(int position, RelativeLayout rlCellOrchard);

    void onImageClick(String currentOrchardName, RelativeLayout rlCellOrchard);

    void onCameraClick(String currentOrchardName, RelativeLayout rlCellOrchard);

}
