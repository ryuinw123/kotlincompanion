package com.example.kmitlcompanion.data.store

import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.data.repository.DataRepository
import com.example.kmitlcompanion.data.repository.RemoteRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RemoteDataStore @Inject constructor(
    private val remoteRepository: RemoteRepository
) : DataRepository {

    override fun getMapPoints(): Observable<List<MapPointData>> {
        return remoteRepository.getMapPoints()
    }

    override fun updateLastLocationTimeStamp(timestamp: Long): Completable {
        TODO("Not yet implemented")
    }

    override fun saveMapPoints(list: List<MapPointData>): Completable {
        TODO("Not yet implemented")
    }


}