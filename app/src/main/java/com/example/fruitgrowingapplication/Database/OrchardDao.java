package com.example.fruitgrowingapplication.Database;

import android.net.Uri;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrchardDao {
    @Query("SELECT * FROM orchards")
    List<Orchard> getOrchardList();

    @Query("SELECT * FROM orchards WHERE name=:name")
    Orchard getOrchard(String name);

    @Query("SELECT name FROM orchards ORDER BY ID DESC;")
    List<String> getOrchardNames();

    @Query("SELECT imageURI FROM orchards WHERE name=:name")
    Uri getOrchardImageURI(String name);

    @Query("UPDATE orchards SET imageURI=:imageURI WHERE name=:name")
    void setOrchardImageUri(String name, Uri imageURI);

    @Query("UPDATE orchards SET name=:newName WHERE name=:name")
    void setNewOrchardName(String name, String newName);

    @Query("SELECT name FROM orchards WHERE name=:name")
    String searchName(String name);

    @Query("SELECT columns FROM orchards WHERE name=:name")
    int getColumns(String name);

    @Query("SELECT rows FROM orchards WHERE name=:name")
    int getRows(String name);

    @Query("SELECT ID FROM orchards WHERE name=:name")
    int getID(String name);

    @Insert
    void insertOrchard(Orchard orchard);

    @Query("UPDATE orchards SET rows =:rows, columns=:columns WHERE name=:name")
    void update(String name, int rows, int columns);

    @Query("UPDATE orchards SET rows=rows+1 WHERE name=:name")
    void addRow(String name);

    @Query("UPDATE orchards SET columns=columns+1 WHERE name=:name")
    void addColumn(String name);


    @Query("DELETE FROM orchards WHERE name LIKE :name")
    void delete(String name);

    @Query("DELETE FROM orchards")
    void deleteAll();

    @Update
    void updateOrchard(Orchard orchard);

    @Delete
    void deleteOrchard(Orchard orchard);

}
