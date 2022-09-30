package com.example.kmitlcompanion.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kmitlcompanion.cache.database.constants.MapPointTable

@Entity(tableName = MapPointTable.MAP_POINT_TABLE)
data class MapPointEntity(
    @PrimaryKey
    @ColumnInfo(name = MapPointTable.MAP_POINT_ID)
    val id: Long,
    @ColumnInfo(name = MapPointTable.MAP_POINT_DESCRIPTION)
    val description: String,
    @ColumnInfo(name = MapPointTable.MAP_POINT_LATITUDE)
    val latitude: Double,
    @ColumnInfo(name = MapPointTable.MAP_POINT_LONGITUDE)
    val longitude: Double,
    @ColumnInfo(name = MapPointTable.MAP_POINT_NAME)
    val name: String
)