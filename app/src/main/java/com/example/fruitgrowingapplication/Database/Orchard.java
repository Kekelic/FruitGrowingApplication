package com.example.fruitgrowingapplication.Database;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "orchards", indices = {@Index(value = {"name"}, unique = true)})
public class Orchard {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "rows")
    private int rows;

    @ColumnInfo(name = "columns")
    private int columns;

    @ColumnInfo(name = "imageURI")
    private Uri imageURI;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public Uri getImageURI() {
        return imageURI;
    }

    public void setImageURI(Uri imageURI) {
        this.imageURI = imageURI;
    }
}
