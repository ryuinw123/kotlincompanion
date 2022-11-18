package com.example.kmitlcompanion.data.repository

import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.data.model.UserData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface CacheRepository {
    fun getMapPoints(): Observable<List<MapPointData>>

    fun saveMapPoints(list: List<MapPointData>):Completable

    fun updateLastLocationTimeStamp(timestamp: Long): Completable

    fun updateUser(email: String,token: String): Completable

    fun getUser(): Observable<List<UserData>>
}