package com.example.kmitlcompanion.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kmitlcompanion.cache.database.constants.MapPointTable

@Entity(tableName = MapPointTable.DATA_PROPERTY_TABLE)
data class DataProperty(
    @PrimaryKey
    @ColumnInfo(name = MapPointTable.DATA_PROPERTY_ID)
    val id: Long,
    @ColumnInfo(name = MapPointTable.DATA_PROPERTY_VALUE)
    val property: String
)