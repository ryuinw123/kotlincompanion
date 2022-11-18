package com.example.kmitlcompanion.data.store

import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.data.model.UserData
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

    override fun saveMapPoints(list: List<MapPointData>): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun updateLastLocationTimeStamp(timestamp: Long): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun createLocationQuery(latitude: Double, longitude: Double): Completable {
        return remoteRepository.createLocationQuery(latitude,longitude)
    }

    override fun postLogin(token: String): Observable<Int> {
        return remoteRepository.postLogin(token)
    }

    override fun postUserData(
        name: Any,
        surname: Any,
        faculty: Any,
        department: Any,
        year: Any,
        token:Any
    ): Completable {
        return remoteRepository.postUserData(name,surname,faculty,department,year,token)
    }

//    override fun postUserData(name: Any,
//                              surname : Any,
//                              faculty : Any,
//                              department : Any,
//                              year : Any): Completable{
//        return remoteRepository.postUserData(name,surname,faculty,department,year)
//    }

    override fun updateUser(email: String, token: String): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun getUser(): Observable<List<UserData>> {
        throw IllegalStateException("Function not currently supported!")
    }
}