package com.example.kmitlcompanion.remote.service

import com.example.kmitlcompanion.data.model.MapPointData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface RetrofitTestClient {
    @GET("helloworld")
    fun getMapPoints(): Observable<List<MapPointData>>
}