package com.example.kmitlcompanion.domain.repository

import com.example.kmitlcompanion.data.model.LocationData
import com.example.kmitlcompanion.data.model.LocationPublicData
import com.example.kmitlcompanion.data.model.ReturnLoginData
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.domain.model.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import java.sql.Timestamp

interface DomainRepository {

    fun getLastestNotificationTime(id : Int):Observable<Timestamp>

    fun updateNotificationTime(eventTime: EventTime) : Completable

    fun createEventQuery(event : Event) : Completable

    fun getLocationQuery(latitude: Double , longitude: Double) : Observable<LocationDetail>

    fun getMapPoints() : Observable<MapInformation>

    fun createLocationQuery(location: Location): Completable

    fun createPublicLocationQuery(location: LocationPublicData): Completable

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

    fun getPinDetailsLocationQuery(id : String) : Observable<PinDetail>

    fun addLikeLocationQuery(id : String) : Completable

    fun removeLikeLocationQuery(id : String) : Completable

    fun addCommentMarkerLocationQuery(markerId : String, message : String) : Observable<ReturnAddComment>

    fun editCommentLocationQuery(commentId : String,newMessage : String) : Completable

    fun deleteCommentLocationQuery(commentId : String) : Completable

    fun likeDislikeCommentLocationQuery(commentId : String,
                                        isLikedComment : Int,
                                        isDisLikedComment : Int) : Completable

}