package com.example.kmitlcompanion.remote.service

import com.example.kmitlcompanion.data.model.LocationQuery
import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.data.model.ReturnLoginData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface RetrofitTestClient {
    @Multipart
    @POST("getlocationquery")
    fun getLocationQuery(@Part("latitude") latitude: Double , @Part("longitude") longitude: Double , @Part("authCode") token:Any): Observable<LocationQuery>

    @GET("getmappoints")
    fun getMapPoints(): Observable<List<MapPointData>>


    @Multipart
    @POST("createlocationquery")
    fun createLocationQuery(@Part("latitude") latitude: Double, @Part("longitude") longitude: Double , @Part("name") name : String , @Part("type") type : String , @Part("detail") detail : String ,@Part image:MultipartBody.Part,@Part("authCode") token:String): Completable

    @Multipart
    @POST("login")
    fun postLogin(@Part("authCode") authCode:String): Observable<ReturnLoginData>

    @Multipart
    @POST("postuserdata")
    fun postUserData(@Part("name") name: Any,
                     @Part("surname") surname:Any,
                     @Part("faculty") faculty:Any,
                     @Part("department") department:Any,
                     @Part("year") year:Any,
                     @Part("authCode") token:Any
    ) : Completable
}