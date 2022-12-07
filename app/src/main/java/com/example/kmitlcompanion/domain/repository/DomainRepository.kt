package com.example.kmitlcompanion.domain.repository

import com.example.kmitlcompanion.data.model.LocationData
import com.example.kmitlcompanion.data.model.ReturnLoginData
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.domain.model.LocationDetail
import com.example.kmitlcompanion.domain.model.MapInformation
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface DomainRepository {

    fun getLocationQuery(latitude: Double , longitude: Double) : Observable<LocationDetail>

    fun getMapPoints() : Observable<MapInformation>

    fun createLocationQuery(location: LocationData): Completable

    fun postLogin(authCode : String) : Observable<ReturnLoginData>

    fun postUserData(name: Any,
                     surname : Any,
                     faculty : Any,
                     department : Any,
                     year : Any,
                     token : Any
    ): Completable

    fun updateUser(email : String,token : String): Completable

    fun getUser(): Observable<List<UserData>>
}