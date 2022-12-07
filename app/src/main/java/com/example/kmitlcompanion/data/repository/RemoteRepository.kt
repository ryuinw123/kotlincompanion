package com.example.kmitlcompanion.data.repository

import com.example.kmitlcompanion.data.model.LocationQuery
import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.data.model.ReturnLoginData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import java.io.File
import java.net.URI

interface RemoteRepository {
    fun getLocationQuery(latitude: Double,longitude: Double,token : String): Observable<LocationQuery>
    fun getMapPoints(): Observable<List<MapPointData>>
    fun createLocationQuery(latitude: Double,
                            longitude: Double,
                            name: String,
                            type: String,
                            detail: String,
                            image : MultipartBody.Part,
                            token: String
    ): Completable
    fun postLogin(authCode : String) : Observable<ReturnLoginData>
    fun postUserData(name: Any,
                     surname : Any,
                     faculty : Any,
                     department : Any,
                     year : Any,
                     token : Any
                    ) : Completable
}