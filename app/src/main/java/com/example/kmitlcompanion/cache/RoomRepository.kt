package com.example.kmitlcompanion.cache

import com.example.kmitlcompanion.cache.database.AppDatabase
import com.example.kmitlcompanion.cache.database.constants.NameTable
import com.example.kmitlcompanion.cache.entities.DataProperty
import com.example.kmitlcompanion.cache.entities.User
import com.example.kmitlcompanion.cache.mapper.MapPointMapper
import com.example.kmitlcompanion.cache.mapper.UserMapper
import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.data.repository.CacheRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val database: AppDatabase,
    private val mapPointMapper: MapPointMapper,
    private val userMapper: UserMapper
) : CacheRepository {

    override fun getMapPoints(token: String): Observable<List<MapPointData>> {
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
                id = NameTable.DATA_LAST_LOCATION_TIMESTAMP_ID,
                property = timestamp.toString()
            )
        )
    }

    override fun updateUser(email: String, token: String): Completable {
        return database.cachedDao().updateUser(
            User(
                id = 0,
                email = email,
                token = token
            )
        )
    }

    override fun getUser(): Observable<List<UserData>> {
        return database.cachedDao().getUser().flatMap { list ->
                Observable.just(list.map { userMapper.mapToData(it) })
            }
    }

}
