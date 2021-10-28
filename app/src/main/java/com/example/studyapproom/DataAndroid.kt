package com.example.studyapproom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Android")
data class DataAndroid(
    @PrimaryKey(autoGenerate = true)
    val pk: Int,
    val title: String,
    val material:String,
    val details: String
)