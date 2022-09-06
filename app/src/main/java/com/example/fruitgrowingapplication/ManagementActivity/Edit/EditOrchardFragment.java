package com.example.fruitgrowingapplication.ManagementActivity.Edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.Database.AppDatabase;
import com.example.fruitgrowingapplication.R;

public class EditOrchardFragment extends Fragment {

    private RecyclerView gridRecycler, horizontalRecycler, verticalRecycler;
    private GridRecyclerAdapterForEdit gridAdapter;
    private HorizontalRecyclerAdapterForEdit horizontalAdapter;
    private VerticalRecyclerAdapterForEdit verticalAdapter;
    private final String orchardName;
    private int columns;
    private int rows;
    private final ImageView ivActivityImage;

    public EditOrchardFragment(String orchardName, ImageView ivActivityImage) {
        this.orchardName = orchardName;
        this.ivActivityImage = ivActivityImage;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trees, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupActivityImage();
        getOrchardInformation();
        setupRecyclers();
        setupRecyclersData();
        setupScrollListeners();
    }

    private void setupActivityImage() {
        ivActivityImage.setImageResource(R.drawable.ic_edit);
    }

    private void getOrchardInformation() {
        AppDatabase database = AppDatabase.getInstance(getContext());
        columns = database.orchardDao().getColumns(orchardName) + 2;
        rows = database.orchardDao().getRows(orchardName) + 2;
    }

    private void setupRecyclers() {
        gridRecycler = getView().findViewById(R.id.recycler_grid_content);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), columns);
        gridRecycler.setLayoutManager(gridLayoutManager);
        gridAdapter = new GridRecyclerAdapterForEdit(getContext());
        gridRecycler.setAdapter(gridAdapter);

        horizontalRecycler = getView().findViewById(R.id.recycler_horizontal_content);
        horizontalRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        horizontalAdapter = new HorizontalRecyclerAdapterForEdit();
        horizontalRecycler.setAdapter(horizontalAdapter);

        verticalRecycler = getView().findViewById(R.id.recycler_vertical_content);
        verticalRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        verticalAdapter = new VerticalRecyclerAdapterForEdit();
        verticalRecycler.setAdapter(verticalAdapter);

        gridAdapter.setFriendlyHorizontalAdapter(horizontalAdapter);
        gridAdapter.setFriendlyVerticalAdapter(verticalAdapter);

        horizontalAdapter.setFriendlyGridAdapter(gridAdapter);
        verticalAdapter.setFriendlyGridAdapter(gridAdapter);
    }

    private void setupRecyclersData() {
        verticalAdapter.setRows(rows);
        horizontalAdapter.setColumns(columns);
        gridAdapter.addInformation(rows, columns, orchardName, (GridLayoutManager) gridRecycler.getLayoutManager());

    }

    private void setupScrollListeners() {
        final RecyclerView.OnScrollListener[] scrollListeners = new RecyclerView.OnScrollListener[2];
        scrollListeners[0] = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                gridRecycler.removeOnScrollListener(scrollListeners[1]);
                gridRecycler.scrollBy(dx, dy);
                gridRecycler.addOnScrollListener(scrollListeners[1]);
            }
        };
        scrollListeners[1] = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                verticalRecycler.removeOnScrollListener(scrollListeners[0]);
                verticalRecycler.scrollBy(dx, dy);
                verticalRecycler.addOnScrollListener(scrollListeners[0]);
            }
        };

        verticalRecycler.addOnScrollListener(scrollListeners[0]);
        gridRecycler.addOnScrollListener(scrollListeners[1]);
    }

}
