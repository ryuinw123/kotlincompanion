package com.example.kmitlcompanion.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kmitlcompanion.cache.database.constants.NameTable

@Entity(tableName = NameTable.USER_TABLE)
data class User(
    @PrimaryKey
    @ColumnInfo(name = NameTable.USER_ID)
    val id: Int,

    @ColumnInfo(name = NameTable.USER_EMAIL)
    val email: String,

    @ColumnInfo(name = NameTable.USER_TOKEN)
    val token: String,
)
