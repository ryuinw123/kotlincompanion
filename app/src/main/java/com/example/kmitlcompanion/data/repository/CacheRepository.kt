package com.example.kmitlcompanion.data.repository

import com.example.kmitlcompanion.data.model.EventTimeData
import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.domain.model.LocationDetail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface CacheRepository {

    fun getLastestNotificationTime(event_id : Int , user_id :  Int):Observable<Long?>

    fun updateNotificationTime(eventData: EventTimeData) : Completable

    fun getMapPoints(token: String): Observable<List<MapPointData>>

    fun saveMapPoints(list: List<MapPointData>):Completable

    fun updateLastLocationTimeStamp(timestamp: Long): Completable

    fun updateUser(email: String,token: String): Completable

    fun getUser(): Observable<List<UserData>>

}