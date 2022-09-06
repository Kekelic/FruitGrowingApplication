package com.example.fruitgrowingapplication.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "trees",
        foreignKeys = {
                @ForeignKey(
                        entity = Orchard.class,
                        parentColumns = "ID",
                        childColumns = "orchardID",
                        onDelete = ForeignKey.CASCADE
                )
        })
public class Tree {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "orchardID", index = true)
    private int orchardID;

    @ColumnInfo(name = "position")
    private int position;

    @ColumnInfo(name = "visibility")
    private boolean visibility;

    @ColumnInfo(name = "fruitType")
    private String fruitType;

    @ColumnInfo(name = "fruitVariety")
    private String fruitVariety;

    @ColumnInfo(name = "fruitRootstock")
    private String fruitRootstock;

    @ColumnInfo(name = "plantingDate")
    private String plantingDate;

    @ColumnInfo(name = "health")
    private TreeHealth health;

    @ColumnInfo(name = "yield")
    private TreeYield yield;

    @ColumnInfo(name = "size")
    private TreeSize size;

    @ColumnInfo(name = "growth")
    private TreeGrowth growth;

    @ColumnInfo(name = "isForReplace")
    private boolean isForReplace;

    @ColumnInfo(name = "notes")
    private String notes;


    public Tree(int orchardID, int position, boolean visibility) {
        this.orchardID = orchardID;
        this.position = position;
        this.visibility = visibility;
        this.fruitRootstock = "none";
        this.health = TreeHealth.NOT_RATED;
        this.yield = TreeYield.NOT_RATED;
        this.size = TreeSize.NOT_RATED;
        this.growth = TreeGrowth.NOT_RATED;
        this.plantingDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        this.isForReplace = false;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public int getOrchardID() {
        return orchardID;
    }

    public void setOrchardID(int orchardID) {
        this.orchardID = orchardID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public String getFruitType() {
        return fruitType;
    }

    public void setFruitType(String fruitType) {
        this.fruitType = fruitType;
    }

    public String getFruitVariety() {
        return fruitVariety;
    }

    public void setFruitVariety(String fruitVariety) {
        this.fruitVariety = fruitVariety;
    }

    public String getFruitRootstock() {
        return fruitRootstock;
    }

    public void setFruitRootstock(String fruitRootstock) {
        this.fruitRootstock = fruitRootstock;
    }

    public String getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(String plantingDate) {
        this.plantingDate = plantingDate;
    }

    public TreeHealth getHealth() {
        return health;
    }

    public void setHealth(TreeHealth health) {
        this.health = health;
    }

    public TreeYield getYield() {
        return yield;
    }

    public void setYield(TreeYield yield) {
        this.yield = yield;
    }

    public TreeSize getSize() {
        return size;
    }

    public void setSize(TreeSize size) {
        this.size = size;
    }

    public TreeGrowth getGrowth() {
        return growth;
    }

    public void setGrowth(TreeGrowth growth) {
        this.growth = growth;
    }

    public boolean isForReplace() {
        return isForReplace;
    }

    public void setForReplace(boolean forReplace) {
        isForReplace = forReplace;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
