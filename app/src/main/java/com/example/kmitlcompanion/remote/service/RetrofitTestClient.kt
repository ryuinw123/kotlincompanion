package com.example.kmitlcompanion.remote.service

import com.example.kmitlcompanion.data.model.MapPointData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface RetrofitTestClient {
    @GET("helloworld")
    fun getMapPoints(): Observable<List<MapPointData>>

    @Multipart
    @POST("testpost")
    fun createLocationQuery(@Part("latitude") latitude: Double, @Part("longitude") longitude: Double): Completable

}