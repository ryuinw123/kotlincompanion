package com.example.kmitlcompanion.cache

import com.example.kmitlcompanion.cache.database.AppDatabase
import com.example.kmitlcompanion.cache.database.constants.MapPointTable
import com.example.kmitlcompanion.cache.entities.DataProperty
import com.example.kmitlcompanion.cache.mapper.MapPointMapper
import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.data.repository.CacheRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val database: AppDatabase,
    private val mapPointMapper: MapPointMapper

) : CacheRepository {

    override fun getMapPoints(): Observable<List<MapPointData>> {
        return database.cachedDao().getMapPoints()
            .flatMap { list ->
                Observable.just(list.map { mapPointMapper.mapToData(it) })
            }
    }

    override fun saveMapPoints(list: List<MapPointData>): Completable {
        return database.cachedDao()
            .saveMapPoints(list.map { mapPointMapper.mapToEntity(it) })
    }

    override fun updateLastLocationTimeStamp(timestamp: Long): Completable {
        return database.cachedDao().updateProperty(
            DataProperty(
                id = MapPointTable.DATA_LAST_LOCATION_TIMESTAMP_ID,
                property = timestamp.toString()
            )
        )
    }
}