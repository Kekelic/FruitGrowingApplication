package com.example.fruitgrowingapplication.ManagementActivity.Edit;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.Database.Tree;
import com.example.fruitgrowingapplication.R;


public class GridViewHolderForEdit extends RecyclerView.ViewHolder {

    private final FrameLayout flEditTree;
    private final ImageView ivEditTree, ivEditStars, ivEditCross, ivEditExclamationMark, ivAdd;

    public GridViewHolderForEdit(@NonNull View itemView) {
        super(itemView);

        flEditTree = itemView.findViewById(R.id.fl_tree);
        ivEditTree = itemView.findViewById(R.id.iv_tree);
        ivEditStars = itemView.findViewById(R.id.iv_stars);
        ivEditCross = itemView.findViewById(R.id.iv_cross);
        ivEditExclamationMark = itemView.findViewById(R.id.iv_exclamation_mark);
        ivAdd = itemView.findViewById(R.id.iv_add);
    }

    public void setTrees(Tree tree) {
        if (tree != null) {
            setBackground(tree);
            setTreeSize(tree);
            setStars(tree);
            setTreeHealth(tree);
            setIsForReplace(tree);
        }
    }

    private void setBackground(Tree tree) {
        switch (tree.getGrowth()) {
            case NOT_RATED:
                flEditTree.setBackgroundResource(R.drawable.background_not_rated);
                break;
            case NOTHING:
                flEditTree.setBackgroundResource(R.drawable.background_nothing);
                break;
            case BAD:
                flEditTree.setBackgroundResource(R.drawable.background_bad);
                break;
            case GOOD:
                flEditTree.setBackgroundResource(R.drawable.background_good);
                break;
            case VERY_GOOD:
                flEditTree.setBackgroundResource(R.drawable.background_very_good);
                break;
            case EXCELLENT:
                flEditTree.setBackgroundResource(R.drawable.background_excellent);
        }
    }

    private void setTreeSize(Tree tree) {
        switch (tree.getSize()) {
            case NOT_RATED:
                ivEditTree.setImageResource(R.drawable.size_not_rated);
                break;
            case SMALL:
                ivEditTree.setImageResource(R.drawable.size_small);
                break;
            case MEDIUM:
                ivEditTree.setImageResource(R.drawable.size_medium);
                break;
            case LARGE:
                ivEditTree.setImageResource(R.drawable.size_large);
        }
    }

    private void setStars(Tree tree) {
        switch (tree.getYield()) {
            case NOT_RATED:
                ivEditStars.setImageResource(R.drawable.yield_not_rated);
                break;
            case NOTHING:
                ivEditStars.setImageResource(R.drawable.yield_nothing);
                break;
            case BAD:
                ivEditStars.setImageResource(R.drawable.yield_bad);
                break;
            case GOOD:
                ivEditStars.setImageResource(R.drawable.yield_good);
                break;
            case VERY_GOOD:
                ivEditStars.setImageResource(R.drawable.yield_very_good);
                break;
            case EXCELLENT:
                ivEditStars.setImageResource(R.drawable.yield_excellent);
        }
    }

    private void setTreeHealth(Tree tree) {
        switch (tree.getHealth()) {
            case NOT_RATED:
                ivEditCross.setImageResource(R.drawable.health_not_rated);
                break;
            case DRIED:
                ivEditCross.setImageResource(R.drawable.health_dried);
                break;
            case DISEASED:
                ivEditCross.setImageResource(R.drawable.health_diseased);
                break;
            case ATTACKED_BY_PESTS:
                ivEditCross.setImageResource(R.drawable.health_attacked_by_pests);
                break;
            case SMALL_DAMAGE:
                ivEditCross.setImageResource(R.drawable.health_small_damage);
                break;
            case HEALTHY:
                ivEditCross.setImageResource(R.drawable.health_healthy);
                break;
        }

    }

    private void setIsForReplace(Tree tree) {
        if (tree.isForReplace()) {
            ivEditExclamationMark.setVisibility(View.VISIBLE);
        } else {
            ivEditExclamationMark.setVisibility(View.GONE);
        }
    }

}
