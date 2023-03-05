package com.example.kmitlcompanion.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kmitlcompanion.cache.database.constants.NameTable

@Entity(tableName = NameTable.EVENT_NOTI_LOG)
data class NotiLogEntity(
    @PrimaryKey
    @ColumnInfo(name = NameTable.EVENT_ID)
    val id: Long,

    @ColumnInfo(name = NameTable.EVENT_NAME)
    val name: String,
    @ColumnInfo(name = NameTable.EVENT_STARTTIME)
    val startTime: String,
    @ColumnInfo(name = NameTable.EVENT_ENDTIME)
    val endTime: String,


)