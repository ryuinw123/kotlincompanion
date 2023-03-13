package com.example.kmitlcompanion.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kmitlcompanion.cache.database.constants.NameTable
import com.example.kmitlcompanion.cache.entities.DataProperty
import com.example.kmitlcompanion.cache.entities.EventTime
import com.example.kmitlcompanion.cache.entities.MapPointEntity
import com.example.kmitlcompanion.cache.entities.User
import com.example.kmitlcompanion.data.model.UserData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
abstract class CachedDao {

    @Query("SELECT event_notification_time FROM ${NameTable.EVENT_TABLE} WHERE event_id=(:event_id) and user_id=(:user_id)")
    abstract fun getLastestNotificationTime(event_id : Int , user_id : Int) : Observable<Long?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateNotificationTime(event : EventTime) : Completable

    @Query("SELECT * FROM ${NameTable.MAP_POINT_TABLE}")
    abstract fun getMapPoints(): Observable<List<MapPointEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveMapPoints(list: List<MapPointEntity>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateProperty(dataProperty: DataProperty): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateUser(user: User): Completable

    @Query("SELECT * FROM ${NameTable.USER_TABLE}")
    abstract fun getUser(): Observable<List<User>>

    @Query(
        """
        SELECT data_property_value FROM ${NameTable.DATA_PROPERTY_TABLE} 
        WHERE data_property_id=(:property)
        """
    )
    abstract fun getProperty(property: Long): Observable<String>
}