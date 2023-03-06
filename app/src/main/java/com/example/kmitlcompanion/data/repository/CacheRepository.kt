package com.example.kmitlcompanion.data.repository

import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.data.model.NotiLogData
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.domain.model.LocationDetail
import com.example.kmitlcompanion.domain.model.NotiLogDetails
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface CacheRepository {

    fun getMapPoints(token: String): Observable<List<MapPointData>>

    fun saveMapPoints(list: List<MapPointData>):Completable

    fun updateLastLocationTimeStamp(timestamp: Long): Completable

    fun updateUser(email: String,token: String): Completable

    fun getUser(): Observable<List<UserData>>

    fun saveNotificationLogDetails(id : Long,name : String,startTime : String,endTime : String,imageLinks : String) : Completable

    fun getNotificationLogDetails() : Observable<List<NotiLogData>>

    fun deleteAllNotificationLogDetails() : Completable

    fun deleteByIDNotificationLogDetails(id : Long) : Completable

}