package com.example.fruitgrowingapplication.Database;

import android.net.Uri;

import androidx.room.TypeConverter;

public class Converters {

    @TypeConverter
    public static String fromURI(Uri imageURI) {
        if (imageURI == null) {
            return null;
        } else {
            return imageURI.toString();
        }

    }

    @TypeConverter
    public static Uri toURI(String imageURIString) {
        if (imageURIString == null) {
            return null;
        } else {
            return Uri.parse(imageURIString);
        }

    }

    @TypeConverter
    public static String fromTreeHealth(TreeHealth health) {
        if (health == null)
            return TreeHealth.NOT_RATED.toString();
        else
            return health.toString();

    }

    @TypeConverter
    public static TreeHealth toTreeHealth(String health) {
        return TreeHealth.valueOf(health);
    }

    @TypeConverter
    public static String fromTreeYield(TreeYield yield) {
        if (yield == null)
            return TreeYield.NOT_RATED.toString();
        else
            return yield.toString();
    }

    @TypeConverter
    public static TreeYield toTreeYield(String yield) {
        return TreeYield.valueOf(yield);
    }

    @TypeConverter
    public static String fromTreeSize(TreeSize size) {
        if (size == null)
            return TreeSize.NOT_RATED.toString();
        else
            return size.toString();
    }

    @TypeConverter
    public static TreeSize toTreeSize(String size) {
        return TreeSize.valueOf(size);
    }

    @TypeConverter
    public static String fromTreeGrowth(TreeGrowth growth) {
        if (growth == null)
            return TreeGrowth.NOT_RATED.toString();
        else
            return growth.toString();
    }

    @TypeConverter
    public static TreeGrowth toTreeGrowth(String growth) {
        return TreeGrowth.valueOf(growth);
    }
}
