package com.example.kmitlcompanion.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kmitlcompanion.cache.database.constants.NameTable

@Entity(tableName = NameTable.MAP_POINT_TABLE)
data class MapPointEntity(
    @PrimaryKey
    @ColumnInfo(name = NameTable.MAP_POINT_ID)
    val id: Long,
    @ColumnInfo(name = NameTable.MAP_POINT_NAME)
    val name: String,
    @ColumnInfo(name = NameTable.MAP_POINT_DESCRIPTION)
    val description: String,
    @ColumnInfo(name = NameTable.MAP_POINT_LATITUDE)
    val latitude: Double,
    @ColumnInfo(name = NameTable.MAP_POINT_LONGITUDE)
    val longitude: Double,
    @ColumnInfo(name = NameTable.MAP_POINT_PLACE)
    val place: String,
    @ColumnInfo(name = NameTable.MAP_POINT_ADDRESS)
    val address : String,
    @ColumnInfo(name = NameTable.MAP_POINT_TYPE)
    val type : String,
    @ColumnInfo(name = NameTable.MAP_POINT_IMAGE)
    val imageLink : String,
)