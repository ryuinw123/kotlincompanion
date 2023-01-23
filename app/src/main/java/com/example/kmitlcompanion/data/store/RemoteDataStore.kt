package com.example.kmitlcompanion.data.store

import com.example.kmitlcompanion.data.model.LocationQuery
import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.data.model.ReturnLoginData
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.data.repository.DataRepository
import com.example.kmitlcompanion.data.repository.RemoteRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import javax.inject.Inject

class RemoteDataStore @Inject constructor(
    private val remoteRepository: RemoteRepository
) : DataRepository {
    override fun getLocationQuery(
        latitude: Double,
        longitude: Double,
        token: String
    ): Observable<LocationQuery> {
        return remoteRepository.getLocationQuery(latitude,longitude,token)
    }

    override fun getMapPoints(token: String): Observable<List<MapPointData>> {
        return remoteRepository.getMapPoints(token)
    }

    override fun saveMapPoints(list: List<MapPointData>): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun updateLastLocationTimeStamp(timestamp: Long): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun createLocationQuery(
        name: String,
        place: String,
        address: String,
        latitude: Double,
        longitude: Double,
        detail: String,
        type: String,
        image: List<MultipartBody.Part>,
        token: String
    ): Completable {
        return remoteRepository.createLocationQuery(name, place, address, latitude, longitude, detail, type, image, token)
    }


    override fun postLogin(authCode: String): Observable<ReturnLoginData> {
        return remoteRepository.postLogin(authCode)
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