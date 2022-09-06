package com.example.fruitgrowingapplication.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TreeDao {
    @Query("SELECT * FROM trees")
    List<Tree> getTreesList();

    @Query("SELECT * FROM trees WHERE orchardID=:orchardID  AND visibility=1")
    List<Tree> getTreesOfOrchard(int orchardID);

    @Query("SELECT * FROM trees WHERE orchardID=:orchardID AND position=:position")
    Tree getTree(int orchardID, int position);

    @Query("SELECT ID FROM trees WHERE orchardID=:orchardID AND position=:position")
    int getTreeID(int orchardID, int position);

    @Query("SELECT visibility FROM trees WHERE orchardID=:orchardID AND position=:position")
    boolean isVisible(int orchardID, int position);

    @Query("UPDATE trees SET visibility =:visibility WHERE orchardID=:orchardID AND position =:position")
    void setVisibility(int orchardID, int position, boolean visibility);

    @Query("UPDATE trees SET position =:position+position WHERE orchardID=:orchardID")
    void movePositionsDown(int orchardID, int position);

    @Query("UPDATE trees SET position=position+1+position/:columns WHERE orchardID=:orchardID")
    void movePositionsRight(int orchardID, int columns);

    @Query("UPDATE trees SET position=position+position/:columns WHERE orchardID=:orchardID")
    void movePositionsLeft(int orchardID, int columns);

    @Query("UPDATE trees SET fruitType=:fruitType WHERE orchardID=:orchardID AND position =:position")
    void setFruitType(int orchardID, int position, String fruitType);

    @Query("SELECT fruitType FROM trees WHERE orchardID=:orchardID AND position=:position")
    String getFruitType(int orchardID, int position);

    @Query("UPDATE trees SET fruitVariety=:fruitVariety WHERE orchardID=:orchardID AND position =:position")
    void setFruitVariety(int orchardID, int position, String fruitVariety);

    @Query("SELECT fruitVariety FROM trees WHERE orchardID=:orchardID AND position=:position")
    String getFruitVariety(int orchardID, int position);

    @Query("UPDATE trees SET fruitRootstock=:fruitRootstock WHERE orchardID=:orchardID AND position =:position")
    void setFruitRootstock(int orchardID, int position, String fruitRootstock);

    @Query("SELECT fruitRootstock FROM trees WHERE orchardID=:orchardID AND position=:position")
    String getFruitRootstock(int orchardID, int position);

    @Query("UPDATE trees SET plantingDate=:plantingDate WHERE orchardID=:orchardID AND position =:position")
    void setTreePlantingDate(int orchardID, int position, String plantingDate);

    @Query("SELECT plantingDate FROM trees WHERE orchardID=:orchardID AND position=:position")
    String getTreePlantingDate(int orchardID, int position);

    @Query("UPDATE trees SET health=:health WHERE orchardID=:orchardID AND position =:position")
    void setTreeHealth(int orchardID, int position, TreeHealth health);

    @Query("SELECT health FROM trees WHERE orchardID=:orchardID AND position=:position")
    TreeHealth getTreeHealth(int orchardID, int position);

    @Query("UPDATE trees SET yield=:yield WHERE orchardID=:orchardID AND position =:position")
    void setTreeYield(int orchardID, int position, TreeYield yield);

    @Query("SELECT yield FROM trees WHERE orchardID=:orchardID AND position=:position")
    TreeYield getTreeYield(int orchardID, int position);

    @Query("UPDATE trees SET size=:size WHERE orchardID=:orchardID AND position =:position")
    void setTreeSize(int orchardID, int position, TreeSize size);

    @Query("SELECT size FROM trees WHERE orchardID=:orchardID AND position=:position")
    TreeSize getTreeSize(int orchardID, int position);

    @Query("UPDATE trees SET growth=:growth WHERE orchardID=:orchardID AND position =:position")
    void setTreeGrowth(int orchardID, int position, TreeGrowth growth);

    @Query("SELECT growth FROM trees WHERE orchardID=:orchardID AND position=:position")
    TreeGrowth getTreeGrowth(int orchardID, int position);

    @Query("UPDATE trees SET isForReplace=:isForReplace WHERE orchardID=:orchardID AND position=:position")
    void setForReplace(int orchardID, int position, boolean isForReplace);

    @Query("SELECT isForReplace FROM trees WHERE orchardID=:orchardID AND position=:position")
    boolean isForReplace(int orchardID, int position);

    @Query("UPDATE trees SET notes=:notes WHERE orchardID=:orchardID AND position =:position")
    void setTreeNotes(int orchardID, int position, String notes);

    @Query("SELECT notes FROM trees WHERE orchardID=:orchardID AND position=:position")
    String getTreeNotes(int orchardID, int position);

    @Query("SELECT COUNT(ID) FROM trees WHERE orchardID=:orchardID AND fruitType=:fruitType AND fruitVariety=:fruitVariety " +
            "AND fruitRootstock =:fruitRootstock AND visibility=1")
    int countTreesOfTypeAndVarietyAndRootstock(int orchardID, String fruitType, String fruitVariety, String fruitRootstock);

    @Query("SELECT COUNT(ID) FROM trees WHERE orchardID=:orchardID AND plantingDate=:plantingDate AND visibility=1")
    int countTreesOfPlantingDate(int orchardID, String plantingDate);

    @Query("SELECT COUNT(ID) FROM trees WHERE orchardID=:orchardID AND health=:health AND visibility=1")
    int countTreesOfHealth(int orchardID, TreeHealth health);

    @Query("SELECT COUNT(ID) FROM trees WHERE orchardID=:orchardID AND yield=:yield AND visibility=1")
    int countTreesOfYield(int orchardID, TreeYield yield);

    @Query("SELECT COUNT(ID) FROM trees WHERE orchardID=:orchardID AND size=:size AND visibility=1")
    int countTreesOfSize(int orchardID, TreeSize size);

    @Query("SELECT COUNT(ID) FROM trees WHERE orchardID=:orchardID AND growth=:growth AND visibility=1")
    int countTreesOfGrowth(int orchardID, TreeGrowth growth);

    @Query("SELECT COUNT(ID) FROM trees WHERE orchardID=:orchardID AND isForReplace = 1 AND visibility=1")
    int countTreesForReplace(int orchardID);

    @Query("SELECT DISTINCT plantingDate FROM trees WHERE orchardID=:orchardID AND visibility=1")
    List<String> getDistinctPlantingDates(int orchardID);


    @Insert
    void insertTree(Tree tree);

    @Insert
    void insertAll(List<Tree> trees);


    @Query("DELETE FROM trees")
    void deleteAll();

    @Update
    void updateTree(Tree tree);

    @Delete
    void deleteTree(Tree tree);


}
