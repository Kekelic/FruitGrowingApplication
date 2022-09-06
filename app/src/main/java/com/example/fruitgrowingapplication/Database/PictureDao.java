package com.example.fruitgrowingapplication.Database;

import android.net.Uri;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PictureDao {
    @Query("SELECT * FROM pictures")
    List<Picture> getPictures();

    @Query("SELECT * FROM pictures WHERE pictureUri=:pictureUri")
    Picture getPicture(Uri pictureUri);

    @Query("SELECT pictureUri FROM pictures WHERE treeID=:treeID ORDER BY ID DESC;")
    List<Uri> getPictureUris(int treeID);

    @Insert
    void insertPicture(Picture picture);


    @Query("DELETE FROM pictures")
    void deleteAll();

    @Delete
    void deletePicture(Picture picture);
}
