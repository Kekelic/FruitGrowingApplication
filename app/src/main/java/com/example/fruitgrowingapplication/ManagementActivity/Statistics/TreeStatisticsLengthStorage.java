package com.example.fruitgrowingapplication.ManagementActivity.Statistics;

import java.util.ArrayList;
import java.util.List;

public class TreeStatisticsLengthStorage {
    private int typeVarietyAndRootstockLength;
    private int plantingDateLength;
    private int healthLength;
    private int yieldLength;
    private int sizeLength;
    private int growthLength;
    private int forReplacementLength;


    public void updateLengths(int startLength, int endLength) {
        List<Integer> lengths = new ArrayList<Integer>(
                List.of(typeVarietyAndRootstockLength, plantingDateLength, healthLength, yieldLength, sizeLength, growthLength, forReplacementLength)
        );
        for (int i = 0; i < lengths.size(); i++) {
            if (lengths.get(i) > startLength) {
                lengths.set(i, lengths.get(i) - (endLength - startLength));
            }
        }
        setAllLengths(lengths);
    }

    private void setAllLengths(List<Integer> lengths) {
        typeVarietyAndRootstockLength = lengths.get(0);
        plantingDateLength = lengths.get(1);
        healthLength = lengths.get(2);
        yieldLength = lengths.get(3);
        sizeLength = lengths.get(4);
        growthLength = lengths.get(5);
        forReplacementLength = lengths.get(6);
    }

    public int getTypeVarietyAndRootstockLength() {
        return typeVarietyAndRootstockLength;
    }

    public void setTypeVarietyAndRootstockLength(int typeVarietyAndRootstockLength) {
        this.typeVarietyAndRootstockLength = typeVarietyAndRootstockLength;
    }

    public int getPlantingDateLength() {
        return plantingDateLength;
    }

    public void setPlantingDateLength(int plantingDateLength) {
        this.plantingDateLength = plantingDateLength;
    }

    public int getHealthLength() {
        return healthLength;
    }

    public void setHealthLength(int healthLength) {
        this.healthLength = healthLength;
    }

    public int getYieldLength() {
        return yieldLength;
    }

    public void setYieldLength(int yieldLength) {
        this.yieldLength = yieldLength;
    }

    public int getSizeLength() {
        return sizeLength;
    }

    public void setSizeLength(int sizeLength) {
        this.sizeLength = sizeLength;
    }

    public int getGrowthLength() {
        return growthLength;
    }

    public void setGrowthLength(int growthLength) {
        this.growthLength = growthLength;
    }

    public int getForReplacementLength() {
        return forReplacementLength;
    }

    public void setForReplacementLength(int forReplacementLength) {
        this.forReplacementLength = forReplacementLength;
    }
}
