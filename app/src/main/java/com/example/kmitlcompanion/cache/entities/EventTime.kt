package com.example.kmitlcompanion.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kmitlcompanion.cache.database.constants.NameTable
import java.sql.Timestamp
import java.util.jar.Attributes.Name

@Entity(tableName = NameTable.EVENT_TABLE ,
    primaryKeys = [NameTable.EVENT_ID , NameTable.USER_ID])
data class EventTime(
    @ColumnInfo(name = NameTable.EVENT_ID)
    val event_id: Int,
    @ColumnInfo(name = NameTable.USER_ID)
    val user_id : Int,
    @ColumnInfo(name = NameTable.EVENT_NOTIFICATION_TIME)
    val time: Long
)