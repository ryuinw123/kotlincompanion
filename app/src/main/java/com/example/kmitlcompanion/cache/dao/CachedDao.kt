package com.example.kmitlcompanion.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kmitlcompanion.cache.database.constants.MapPointTable
import com.example.kmitlcompanion.cache.entities.DataProperty
import com.example.kmitlcompanion.cache.entities.MapPointEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
abstract class CachedDao {

    @Query("SELECT * FROM ${MapPointTable.MAP_POINT_TABLE}")
    abstract fun getMapPoints(): Observable<List<MapPointEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveMapPoints(list: List<MapPointEntity>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateProperty(dataProperty: DataProperty): Completable

    @Query(
        """
        SELECT data_property_value FROM ${MapPointTable.DATA_PROPERTY_TABLE} 
        WHERE data_property_id=(:property)
        """
    )
    abstract fun getProperty(property: Long): Observable<String>
}