package com.example.studyapproom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Data::class,DataAndroid::class],version = 1, exportSchema = false)
abstract class SubjectsDatabase: RoomDatabase() {

    abstract fun subjectsDao(): SubjectsDao

    companion object{
        @Volatile
        var instance: SubjectsDatabase?= null

        fun getInstance(context: Context): SubjectsDatabase{
            if (instance!=null)
                return instance!!
            synchronized(this){
                instance= Room.databaseBuilder(
                    context.applicationContext,
                    SubjectsDatabase::class.java,
                    "Subjects"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}