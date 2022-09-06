package com.example.fruitgrowingapplication.Database;


import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "pictures",
        foreignKeys = {
                @ForeignKey(
                        entity = Tree.class,
                        parentColumns = "ID",
                        childColumns = "treeID",
                        onDelete = ForeignKey.CASCADE
                )
        })
public class Picture {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "treeID", index = true)
    private int treeID;

    @ColumnInfo(name = "pictureUri")
    private Uri pictureUri;


    public Picture(int treeID, Uri pictureUri) {
        this.treeID = treeID;
        this.pictureUri = pictureUri;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTreeID() {
        return treeID;
    }

    public void setTreeID(int treeID) {
        this.treeID = treeID;
    }

    public Uri getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(Uri pictureUri) {
        this.pictureUri = pictureUri;
    }
}
