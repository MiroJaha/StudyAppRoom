package com.example.studyapproom

import androidx.room.*

@Dao
interface SubjectsDao {

    //Android
    @Query("SELECT * FROM Android")
    fun gettingAllAndroidSubjects(): List<DataAndroid>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAndroidSubject(dataAndroid: DataAndroid)

    @Delete
    fun deleteAndroidSubject(dataAndroid: DataAndroid)

    @Update
    fun updateAndroidSubject(dataAndroid: DataAndroid)

    //Kotlin
    @Query("SELECT * FROM Kotlin")
    fun gettingAllKotlinSubjects(): List<Data>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addKotlinSubject(data: Data)

    @Delete
    fun deleteKotlinSubject(data: Data)

    @Update
    fun updateKotlinSubject(data: Data)

}