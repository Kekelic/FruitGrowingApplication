package com.example.fruitgrowingapplication.ManagementActivity.Edit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.Database.AppDatabase;
import com.example.fruitgrowingapplication.Database.Tree;
import com.example.fruitgrowingapplication.R;

import java.util.ArrayList;
import java.util.List;

public class GridRecyclerAdapterForEdit extends RecyclerView.Adapter<GridViewHolderForEdit> {


    private int columns;
    private int rows;
    private String orchardName;
    private AppDatabase database;
    private HorizontalRecyclerAdapterForEdit horizontalAdapter;
    private VerticalRecyclerAdapterForEdit verticalAdapter;
    private GridLayoutManager gridLayoutManager;

    public GridRecyclerAdapterForEdit(Context context){
        this.database = AppDatabase.getInstance(context);
    }


    @NonNull
    @Override
    public GridViewHolderForEdit onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cellView;
        if (viewType == 0) {
            cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_tree_add, parent, false);
        } else {
            cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_tree, parent, false);
        }
        return new GridViewHolderForEdit(cellView);


    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolderForEdit holder, int position) {
        Tree tree;
        if (!isMarginalAdd(position) && isTree(position)) {
            tree = database.treeDao().getTree(database.orchardDao().getID(orchardName), calculatePositionFromEdit(position));
        } else {
            tree = null;
        }
        holder.setTrees(tree);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(isMarginalAdd(position))) {
                    if (isTree(position)) {
                        removeTree(holder, position);
                    } else {
                        addTree(position);
                    }
                } else {
                    if (isBottomAdd(position)) {
                        addBottomRow(position);
                    } else if (isAboveAdd(position)) {
                        addAboveRow(position);
                    }

                    if (isLeftAdd(position)) {
                        addLeftColumn(position);
                    } else if (isRightAdd(position)) {
                        addRightColumn(position);
                    }
                }
            }
        });
    }

    private void removeTree(@NonNull GridViewHolderForEdit holder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setTitle(R.string.title_for_deleting_tree_dialog);
        builder.setMessage(R.string.message_for_deleting_tree);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTree(position);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void deleteTree(int position) {
        database.treeDao().deleteTree(database.treeDao().getTree(database.orchardDao().getID(orchardName), calculatePositionFromEdit(position)));
        database.treeDao().insertTree(new Tree(database.orchardDao().getID(orchardName), calculatePositionFromEdit(position), false));
        notifyItemChanged(position);

    }


    private void addTree(int position) {
        database.treeDao().setVisibility(database.orchardDao().getID(orchardName), calculatePositionFromEdit(position), true);
        notifyItemChanged(position);
    }

    public void addBottomRow(int position) {
        int orchardID = database.orchardDao().getID(orchardName);
        int maxPosition = (rows - 2) * (columns - 2) - 1;
        List<Tree> trees = new ArrayList<>();
        boolean visibility;
        for (int i = 1; i < columns - 1; i++) {
//            click on edit position must match to the tree position, else tree in not visible
//            position is -1 when method is called from vertical or horizontal adapter and all trees must be visible
            if (maxPosition + i == calculatePositionFromEdit(position) || position == -1) {
                visibility = true;
            } else {
                visibility = false;
            }
            Tree tree = new Tree(orchardID, maxPosition + i, visibility);
            ;
            trees.add(tree);

        }
        database.treeDao().insertAll(trees);
        database.orchardDao().addRow(orchardName);
        rows++;
        notifyItemRangeInserted(columns * (rows - 1), columns);
        verticalAdapter.addDataOnTheEnd();
    }

    public void addAboveRow(int position) {
        int orchardID = database.orchardDao().getID(orchardName);
        List<Tree> trees = new ArrayList<>();

        database.treeDao().movePositionsDown(orchardID, columns - 2);
        boolean visibility;
        for (int i = 1; i < columns - 1; i++) {
            visibility = i - 1 == calculatePositionFromEdit(position) + columns - 2 || position == -1;
            Tree tree = new Tree(orchardID, i - 1, visibility);
            trees.add(tree);
        }
        database.treeDao().insertAll(trees);
        database.orchardDao().addRow(orchardName);
        rows++;
        notifyItemRangeInserted(columns, columns);
        notifyItemRangeChanged(columns * 2, columns * (rows - 2));
        verticalAdapter.addDataOnTheBeginning();
    }

    public void addLeftColumn(int position) {
        int orchardID = database.orchardDao().getID(orchardName);
        List<Tree> trees = new ArrayList<>();
        database.treeDao().movePositionsRight(orchardID, columns - 2);
        boolean visibility;
        for (int i = 0; i < rows - 2; i++) {
            visibility = i * (columns - 1) == calculatePositionFromEdit(position) + position / columns || position == -1;
            Tree tree = new Tree(orchardID, i * (columns - 1), visibility);
            trees.add(tree);
        }

        database.treeDao().insertAll(trees);
        if (isAboveLeftAdd(position)) {
            database.treeDao().setVisibility(orchardID, 0, true);
        }
        database.orchardDao().addColumn(orchardName);
        columns++;
        gridLayoutManager.setSpanCount(columns);
        for (int i = 0; i < rows; i++) {
            notifyItemInserted(i * columns + 1);
            notifyItemRangeChanged(i * columns + 1, columns);
        }
        horizontalAdapter.addDataOnTheBeginning();
    }

    public void addRightColumn(int position) {
        int orchardID = database.orchardDao().getID(orchardName);
        List<Tree> trees = new ArrayList<>();
        database.treeDao().movePositionsLeft(orchardID, columns - 2);
        boolean visibility;
        for (int i = 1; i < rows - 1; i++) {
            visibility = i * (columns - 1) - 1 == calculatePositionFromEdit(position) + i - 1 || position == -1;
            Tree tree = new Tree(orchardID, i * (columns - 1) - 1, visibility);
            trees.add(tree);
        }

        database.treeDao().insertAll(trees);
        if (isAboveRightAdd(position)) {
            database.treeDao().setVisibility(orchardID, columns - 2, true);
        }
        database.orchardDao().addColumn(orchardName);
        columns++;
        gridLayoutManager.setSpanCount(columns);
        for (int i = 0; i < rows; i++) {
            notifyItemInserted(columns * (i + 1) - 2);
            notifyItemRangeChanged(columns * (i + 1) - 2, columns);
        }
        horizontalAdapter.addDataOnTheEnd();
    }


    private boolean isBottomAdd(int position) {
        return position >= columns * (rows - 1);
    }

    private boolean isAboveAdd(int position) {
        return position < columns;
    }

    private boolean isLeftAdd(int position) {
        return position % columns == 0;
    }

    private boolean isRightAdd(int position) {
        return position % columns == columns - 1;
    }

    private boolean isMarginalAdd(int position) {
        return isBottomAdd(position) || isAboveAdd(position) || isLeftAdd(position) || isRightAdd(position);
    }

    private boolean isAboveLeftAdd(int position) {
        return isAboveAdd(position) && isLeftAdd(position);
    }

    private boolean isAboveRightAdd(int position) {
        return isAboveAdd(position) && isRightAdd(position);
    }

    private boolean isTree(int position) {
        return database.treeDao().isVisible(database.orchardDao().getID(orchardName), calculatePositionFromEdit(position));
    }

    @Override
    public int getItemCount() {
        return rows * columns;
    }


    public void addInformation(int rows, int columns, String orchardName, GridLayoutManager gridLayoutManager) {
        this.rows = rows;
        this.columns = columns;
        this.orchardName = orchardName;
        this.gridLayoutManager = gridLayoutManager;
    }


    @Override
    public int getItemViewType(int position) {
        if (isMarginalAdd(position) || !isTree(position)) {
            return 0;
        } else
            return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setFriendlyHorizontalAdapter(HorizontalRecyclerAdapterForEdit horizontalAdapter) {
        this.horizontalAdapter = horizontalAdapter;
    }

    public void setFriendlyVerticalAdapter(VerticalRecyclerAdapterForEdit verticalAdapter) {
        this.verticalAdapter = verticalAdapter;
    }

    public int calculatePositionFromEdit(int position) {
        return position - (columns + (position / columns) * 2 - 1);
    }
}
