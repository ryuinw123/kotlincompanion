package com.example.kmitlcompanion.remote

import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.remote.service.RetrofitTestClient
import com.example.kmitlcompanion.data.repository.RemoteRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RetrofitClient @Inject constructor(
    private val retrofitTestClient: RetrofitTestClient,
) : RemoteRepository {
    override fun getMapPoints(): Observable<List<MapPointData>> {
        return retrofitTestClient.getMapPoints()
    }

    override fun createLocationQuery(latitude: Double, longitude: Double): Completable {
        return retrofitTestClient.createLocationQuery(latitude,longitude)
    }

    override fun postLogin(token: String): Observable<Int> {
        return retrofitTestClient.postLogin(token)
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