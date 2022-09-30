package com.example.kmitlcompanion.data.repository

import com.example.kmitlcompanion.data.model.MapPointData
import io.reactivex.rxjava3.core.Observable

interface RemoteRepository {
    fun getMapPoints(): Observable<List<MapPointData>>
}