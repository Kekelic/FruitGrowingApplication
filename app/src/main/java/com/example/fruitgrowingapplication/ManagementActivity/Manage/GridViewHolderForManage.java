package com.example.fruitgrowingapplication.ManagementActivity.Manage;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitgrowingapplication.Database.AppDatabase;
import com.example.fruitgrowingapplication.Database.Tree;
import com.example.fruitgrowingapplication.R;

public class GridViewHolderForManage extends RecyclerView.ViewHolder {

    private final FrameLayout flTree;
    private final ImageView ivTree, ivStars, ivCross, ivExclamationMark;

    public GridViewHolderForManage(@NonNull View itemView) {
        super(itemView);
        flTree = itemView.findViewById(R.id.fl_tree);
        ivTree = itemView.findViewById(R.id.iv_tree);
        ivStars = itemView.findViewById(R.id.iv_stars);
        ivCross = itemView.findViewById(R.id.iv_cross);
        ivExclamationMark = itemView.findViewById(R.id.iv_exclamation_mark);
    }

    public void setTrees(Tree tree) {
        if (!tree.isVisibility()) {
            flTree.setVisibility(View.GONE);
        } else {
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
                flTree.setBackgroundResource(R.drawable.background_not_rated);
                break;
            case NOTHING:
                flTree.setBackgroundResource(R.drawable.background_nothing);
                break;
            case BAD:
                flTree.setBackgroundResource(R.drawable.background_bad);
                break;
            case GOOD:
                flTree.setBackgroundResource(R.drawable.background_good);
                break;
            case VERY_GOOD:
                flTree.setBackgroundResource(R.drawable.background_very_good);
                break;
            case EXCELLENT:
                flTree.setBackgroundResource(R.drawable.background_excellent);
        }
    }

    private void setTreeSize(Tree tree) {
        switch (tree.getSize()) {
            case NOT_RATED:
                ivTree.setImageResource(R.drawable.size_not_rated);
                break;
            case SMALL:
                ivTree.setImageResource(R.drawable.size_small);
                break;
            case MEDIUM:
                ivTree.setImageResource(R.drawable.size_medium);
                break;
            case LARGE:
                ivTree.setImageResource(R.drawable.size_large);
        }
    }

    private void setStars(Tree tree) {
        switch (tree.getYield()) {
            case NOT_RATED:
                ivStars.setImageResource(R.drawable.yield_not_rated);
                break;
            case NOTHING:
                ivStars.setImageResource(R.drawable.yield_nothing);
                break;
            case BAD:
                ivStars.setImageResource(R.drawable.yield_bad);
                break;
            case GOOD:
                ivStars.setImageResource(R.drawable.yield_good);
                break;
            case VERY_GOOD:
                ivStars.setImageResource(R.drawable.yield_very_good);
                break;
            case EXCELLENT:
                ivStars.setImageResource(R.drawable.yield_excellent);
        }
    }

    private void setTreeHealth(Tree tree) {
        switch (tree.getHealth()) {
            case NOT_RATED:
                ivCross.setImageResource(R.drawable.health_not_rated);
                break;
            case DRIED:
                ivCross.setImageResource(R.drawable.health_dried);
                break;
            case DISEASED:
                ivCross.setImageResource(R.drawable.health_diseased);
                break;
            case ATTACKED_BY_PESTS:
                ivCross.setImageResource(R.drawable.health_attacked_by_pests);
                break;
            case SMALL_DAMAGE:
                ivCross.setImageResource(R.drawable.health_small_damage);
                break;
            case HEALTHY:
                ivCross.setImageResource(R.drawable.health_healthy);
                break;
        }

    }

    private void setIsForReplace(Tree tree) {
        if (tree.isForReplace()) {
            ivExclamationMark.setVisibility(View.VISIBLE);
        } else {
            ivExclamationMark.setVisibility(View.GONE);
        }
    }


}
