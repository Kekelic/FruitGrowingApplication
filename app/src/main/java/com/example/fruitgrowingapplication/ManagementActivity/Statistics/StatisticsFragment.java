package com.example.fruitgrowingapplication.ManagementActivity.Statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fruitgrowingapplication.Database.AppDatabase;
import com.example.fruitgrowingapplication.Database.Tree;
import com.example.fruitgrowingapplication.Database.TreeGrowth;
import com.example.fruitgrowingapplication.Database.TreeHealth;
import com.example.fruitgrowingapplication.Database.TreeSize;
import com.example.fruitgrowingapplication.Database.TreeYield;
import com.example.fruitgrowingapplication.R;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private final String orchardName;
    private final ImageView ivActivityImage;

    private StringBuffer result = new StringBuffer();

    private final AppDatabase database = AppDatabase.getInstance(getContext());

    private CheckBox cbTypeAndVariety, cbPlantingDate, cbHealth, cbYield, cbSize, cbGrowth, cbForReplacement;
    private Button bShowResults;
    private TextView tvResults;

    private final TreeStatisticsLengthStorage startLengthStorage = new TreeStatisticsLengthStorage();
    private final TreeStatisticsLengthStorage endLengthStorage = new TreeStatisticsLengthStorage();


    public StatisticsFragment(String orchardName, ImageView ivActivityImage) {
        this.orchardName = orchardName;
        this.ivActivityImage = ivActivityImage;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupActivityImage();
        initializeTextViewResults(view);
        setupCheckBoxes();
        setupButtonResults();

    }

    private void setupActivityImage() {
        ivActivityImage.setImageResource(R.drawable.ic_statistics);
    }

    private void initializeTextViewResults(@NonNull View view) {
        tvResults = view.findViewById(R.id.tv_results);
    }


    private void setupCheckBoxes() {
        setupTypeVarietyAndRootstockCb();
        setupPlantingDateCb();
        setupHealthCb();
        setupYieldCb();
        setupSizeCb();
        setupGrowthCb();
        setupForReplacementCb();
    }


    private void setupButtonResults() {
        bShowResults = getView().findViewById(R.id.b_show_results);
        bShowResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResults.setText(result);
            }
        });
    }

    private void setupTypeVarietyAndRootstockCb() {
        cbTypeAndVariety = getView().findViewById(R.id.cb_type_variety_and_rootstock);
        cbTypeAndVariety.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    makeTypeVarietyAndRootstockResult();
                } else {
                    removeResult(startLengthStorage.getTypeVarietyAndRootstockLength(), endLengthStorage.getTypeVarietyAndRootstockLength());
                }
            }
        });
    }

    private void makeTypeVarietyAndRootstockResult() {
        startLengthStorage.setTypeVarietyAndRootstockLength(result.length());
        List<Tree> trees = database.treeDao().getTreesOfOrchard(database.orchardDao().getID(orchardName));
        List<String> types = new ArrayList<>();
        List<String> varieties = new ArrayList<>();
        List<String> rootstocks = new ArrayList<>();
        boolean differentFlag = true;
        boolean isNullCounted = false;
        int nullCounter = 0;

        for (int i = 0; i < trees.size(); i++) {
            String fruitVariety = trees.get(i).getFruitVariety();
            String fruitType = trees.get(i).getFruitType();
            String fruitRootstock = trees.get(i).getFruitRootstock();
            for (int j = 0; j < varieties.size(); j++) {
                String uniqueVariety = varieties.get(j);
                String uniqueType = types.get(j);
                String uniqueRootstock = rootstocks.get(j);
                if (fruitVariety == null || fruitType == null) {
                    differentFlag = false;
                    nullCounter++;
                    break;
                } else {
                    if (fruitRootstock == null) {
                        if (fruitVariety.equals(uniqueVariety) && fruitType.equals(uniqueType) && fruitRootstock == uniqueRootstock) {
                            differentFlag = false;
                            break;
                        }
                    } else {
                        if (fruitVariety.equals(uniqueVariety) && fruitType.equals(uniqueType) && fruitRootstock.equals(uniqueRootstock)) {
                            differentFlag = false;
                            break;
                        }
                    }
                }

            }
            if ((fruitType == null || fruitVariety == null) && !isNullCounted) {
                types.add(getString(R.string.unnamed));
                varieties.add("");
                rootstocks.add("");
                isNullCounted = true;
                if (i == 0) {
                    nullCounter++;
                }
            } else if (differentFlag) {
                types.add(fruitType);
                varieties.add(fruitVariety);
                if (fruitRootstock == null) {
                    rootstocks.add("none");
                } else {
                    rootstocks.add(fruitRootstock);
                }
            }
            differentFlag = true;
        }
        result.append(getString(R.string.title_for_types_varieties_and_rootstocks)).append("\n");
        for (int i = 0; i < types.size(); i++) {
            int treeCount;
            if (types.get(i).equals(getString(R.string.unnamed))) {
                treeCount = nullCounter;
            } else {
                treeCount = database.treeDao().countTreesOfTypeAndVarietyAndRootstock(database.orchardDao().getID(orchardName), types.get(i), varieties.get(i), rootstocks.get(i));
            }
            String rootstockText;
            if (rootstocks.get(i).equals("") || rootstocks.get(i).equals("none")) {
                rootstockText = "";
            } else
                rootstockText = ", " + rootstocks.get(i);
            result.append("\t").append(types.get(i)).append(" ").append(varieties.get(i)).append(rootstockText).append(" [").append(treeCount).append("]").append("\n");
        }
        result.append("\n");
        endLengthStorage.setTypeVarietyAndRootstockLength(result.length());
    }


    private void setupPlantingDateCb() {
        cbPlantingDate = getView().findViewById(R.id.cb_planting_date);
        cbPlantingDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    makePlantingDateResult();
                } else {
                    removeResult(startLengthStorage.getPlantingDateLength(), endLengthStorage.getPlantingDateLength());
                }
            }
        });
    }

    private void makePlantingDateResult() {
        startLengthStorage.setPlantingDateLength(result.length());
        List<String> plantingDates = new ArrayList<String>();
        plantingDates = database.treeDao().getDistinctPlantingDates(database.orchardDao().getID(orchardName));

        result.append(getString(R.string.title_for_planting_dates)).append("\n");
        for (int i = 0; i < plantingDates.size(); i++) {
            int treeCount = database.treeDao().countTreesOfPlantingDate(database.orchardDao().getID(orchardName), plantingDates.get(i));
            result.append("\t").append(plantingDates.get(i)).append(" [").append(treeCount).append("]").append("\n");
        }
        result.append("\n");
        endLengthStorage.setPlantingDateLength(result.length());
    }

    private void setupHealthCb() {
        cbHealth = getView().findViewById(R.id.cb_health);
        cbHealth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    makeHealthResult();
                } else {
                    removeResult(startLengthStorage.getHealthLength(), endLengthStorage.getHealthLength());
                }
            }
        });
    }

    private void makeHealthResult() {
        startLengthStorage.setHealthLength(result.length());
        result.append(getString(R.string.title_for_tree_health)).append("\n");
        String[] health_options = getResources().getStringArray(R.array.health_options);
        for (int i = 0; i < TreeHealth.values().length; i++) {
            int treeCount = database.treeDao().countTreesOfHealth(database.orchardDao().getID(orchardName), TreeHealth.values()[i]);
            result.append("\t").append(health_options[i]).append(" [").append(treeCount).append("]").append("\n");
        }
        result.append("\n");
        endLengthStorage.setHealthLength(result.length());
    }

    private void setupYieldCb() {
        cbYield = getView().findViewById(R.id.cb_yield);
        cbYield.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    makeYieldResult();
                } else {
                    removeResult(startLengthStorage.getYieldLength(), endLengthStorage.getYieldLength());
                }
            }
        });
    }

    private void makeYieldResult() {
        startLengthStorage.setYieldLength(result.length());
        result.append(getString(R.string.title_for_tree_yield)).append("\n");
        String[] yield_options = getResources().getStringArray(R.array.yield_options);
        for (int i = 0; i < TreeYield.values().length; i++) {
            int treeCount = database.treeDao().countTreesOfYield(database.orchardDao().getID(orchardName), TreeYield.values()[i]);
            result.append("\t").append(yield_options[i]).append(" [").append(treeCount).append("]").append("\n");
        }
        result.append("\n");
        endLengthStorage.setYieldLength(result.length());
    }

    private void setupSizeCb() {
        cbSize = getView().findViewById(R.id.cb_size);
        cbSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    makeSizeResult();
                } else {
                    removeResult(startLengthStorage.getSizeLength(), endLengthStorage.getSizeLength());
                }
            }
        });
    }

    private void makeSizeResult() {
        startLengthStorage.setSizeLength(result.length());
        result.append(getString(R.string.title_for_tree_size)).append("\n");
        String[] size_options = getResources().getStringArray(R.array.size_options);
        for (int i = 0; i < TreeSize.values().length; i++) {
            int treeCount = database.treeDao().countTreesOfSize(database.orchardDao().getID(orchardName), TreeSize.values()[i]);
            result.append("\t").append(size_options[i]).append(" [").append(treeCount).append("]").append("\n");
        }
        result.append("\n");
        endLengthStorage.setSizeLength(result.length());
    }

    private void setupGrowthCb() {
        cbGrowth = getView().findViewById(R.id.cb_growth);

        cbGrowth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    makeGrowthResult();
                } else {
                    removeResult(startLengthStorage.getGrowthLength(), endLengthStorage.getGrowthLength());
                }
            }
        });
    }

    private void makeGrowthResult() {
        startLengthStorage.setGrowthLength(result.length());
        result.append(getString(R.string.title_for_tree_growth)).append("\n");
        String[] growth_options = getResources().getStringArray(R.array.growth_options);
        for (int i = 0; i < TreeGrowth.values().length; i++) {
            int treeCount = database.treeDao().countTreesOfGrowth(database.orchardDao().getID(orchardName), TreeGrowth.values()[i]);
            result.append("\t").append(growth_options[i]).append(" [").append(treeCount).append("]").append("\n");
        }
        result.append("\n");
        endLengthStorage.setGrowthLength(result.length());
    }

    private void setupForReplacementCb() {
        cbForReplacement = getView().findViewById(R.id.cb_for_replacement);

        cbForReplacement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    makeForReplacementResult();
                } else {
                    removeResult(startLengthStorage.getForReplacementLength(), endLengthStorage.getForReplacementLength());
                }
            }
        });
    }

    private void makeForReplacementResult() {
        startLengthStorage.setForReplacementLength(result.length());
        result.append(getString(R.string.title_for_tree_replace));
        int treeCount = database.treeDao().countTreesForReplace(database.orchardDao().getID(orchardName));
        result.append(" [").append(treeCount).append("]").append("\n\n");
        endLengthStorage.setForReplacementLength(result.length());
    }

    private void removeResult(int startLength, int endLength) {
        result.delete(startLength, endLength);
        startLengthStorage.updateLengths(startLength, endLength);
        endLengthStorage.updateLengths(startLength, endLength);
    }


}
