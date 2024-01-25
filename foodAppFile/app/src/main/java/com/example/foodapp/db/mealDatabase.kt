package com.example.foodapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodapp.pojo.Meal

@Database(entities = [Meal::class], version = 1, exportSchema = false)
@TypeConverters(mealTypeConverter::class)
abstract class mealDatabase :RoomDatabase(  ){
    abstract fun mealDao():MealDao

    companion object{
        @Volatile
        var INSTANCE : mealDatabase? =null

        @Synchronized
        fun getInstance(context: Context):mealDatabase{
            if(INSTANCE== null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    mealDatabase::class.java,
                    "meal.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as mealDatabase
        }

    }
}