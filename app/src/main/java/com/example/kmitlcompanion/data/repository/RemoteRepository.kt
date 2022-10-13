package com.example.kmitlcompanion.data.repository

import com.example.kmitlcompanion.data.model.MapPointData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface RemoteRepository {
    fun getMapPoints(): Observable<List<MapPointData>>
    fun createLocationQuery(latitude: Double, longitude: Double): Completable
    fun postLogin(token : String) : Observable<Int>
    fun postUserData(name: Any,
                     surname : Any,
                     faculty : Any,
                     department : Any,
                     year : Any,
                     token : Any
                    ) : Completable
}