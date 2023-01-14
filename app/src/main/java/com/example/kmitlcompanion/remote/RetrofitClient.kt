package com.example.kmitlcompanion.remote

import com.example.kmitlcompanion.data.model.LocationQuery
import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.data.model.ReturnLoginData
import com.example.kmitlcompanion.remote.service.RetrofitTestClient
import com.example.kmitlcompanion.data.repository.RemoteRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import javax.inject.Inject

class RetrofitClient @Inject constructor(
    private val retrofitTestClient: RetrofitTestClient,
) : RemoteRepository {
    override fun getLocationQuery(
        latitude: Double,
        longitude: Double,
        token: String
    ): Observable<LocationQuery> {
        return retrofitTestClient.getLocationQuery(latitude,longitude,token)
    }

    override fun getMapPoints(token: String): Observable<List<MapPointData>> {
        return retrofitTestClient.getMapPoints(token)
    }

    override fun createLocationQuery(
        name: String,
        place: String,
        address: String,
        latitude: Double,
        longitude: Double,
        detail: String,
        type: String,
        image: MultipartBody.Part,
        token: String
    ): Completable {
        return retrofitTestClient.createLocationQuery(name, place, address, latitude, longitude, detail, type, image, token)
    }


    override fun postLogin(authCode: String): Observable<ReturnLoginData> {
        return retrofitTestClient.postLogin(authCode)
    }

    override fun postUserData(
        name: Any,
        surname: Any,
        faculty: Any,
        department: Any,
        year: Any,
        token: Any
    ): Completable {
        return retrofitTestClient.postUserData(name,surname,faculty,department,year,token)
    }

}