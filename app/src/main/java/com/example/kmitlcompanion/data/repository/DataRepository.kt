package com.example.kmitlcompanion.data.repository

import com.example.kmitlcompanion.data.model.MapPointData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface DataRepository {

    fun getMapPoints(): Observable<List<MapPointData>>

    fun updateLastLocationTimeStamp(timestamp: Long):Completable

    fun saveMapPoints(list: List<MapPointData>):Completable



}