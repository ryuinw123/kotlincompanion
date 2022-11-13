package com.example.kmitlcompanion.data.store

import com.example.kmitlcompanion.data.model.LocationQuery
import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.data.model.ReturnLoginData
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.data.repository.CacheRepository
import com.example.kmitlcompanion.data.repository.DataRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CacheDataStore @Inject constructor(
    private val cacheRepository: CacheRepository
) :DataRepository {
    override fun getLocationQuery(
        latitude: Double,
        longitude: Double,
        token: String
    ): Observable<LocationQuery> {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun getMapPoints(): Observable<List<MapPointData>> {
        return cacheRepository.getMapPoints()
    }

    override fun updateLastLocationTimeStamp(timestamp: Long): Completable {
        return cacheRepository.updateLastLocationTimeStamp(timestamp)
    }

    override fun createLocationQuery(latitude: Double, longitude: Double): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun postLogin(authCode: String): Observable<ReturnLoginData> {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun postUserData(
        name: Any,
        surname: Any,
        faculty: Any,
        department: Any,
        year: Any,
        token: Any,
    ): Completable {
        throw IllegalStateException("Function not currently supported!")
    }


    override fun saveMapPoints(list: List<MapPointData>): Completable {
        return cacheRepository.saveMapPoints(list)
    }

    override fun updateUser(email: String, token: String): Completable {
        return cacheRepository.updateUser(email,token)
    }

    override fun getUser(): Observable<List<UserData>> {
        return cacheRepository.getUser()
    }
}