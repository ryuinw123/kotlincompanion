package com.example.kmitlcompanion.data.repository

import android.content.Intent
import com.example.kmitlcompanion.data.model.*
import com.example.kmitlcompanion.domain.model.Event
import com.example.kmitlcompanion.domain.model.EventInformation
import com.example.kmitlcompanion.domain.model.LocationDetail
import com.example.kmitlcompanion.domain.model.SearchDetail
import com.mapbox.geojson.Point
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import java.io.File
import java.net.URI

interface DataRepository {

    fun createEventQuery(name : String, detail: String,status : String, point : List<Point>, image: List<MultipartBody.Part>, token: String) : Completable
    fun getLocationQuery(latitude: Double,longitude: Double , token : String) : Observable<LocationQuery>

    fun getEventLocations(token: String) : Observable<List<EventAreaData>>

    fun getMapPoints(token: String): Observable<List<MapPointData>>

    fun saveMapPoints(list: List<MapPointData>): Completable

    fun updateLastLocationTimeStamp(timestamp: Long): Completable

    fun createLocationQuery(name : String,place : String,address:String,latitude: Double,longitude: Double,detail : String, type : String,  image: List<MultipartBody.Part> ,token: String) : Completable

    fun createPublicLocationQuery(name : String, place : String, address:String, latitude: Double, longitude: Double, detail : String, type : String, image: List<MultipartBody.Part>, token: String) : Completable

    fun postLogin(authCode : String) : Observable<ReturnLoginData>

    fun postUserData(name: Any,
                     surname : Any,
                     faculty : Any,
                     department : Any,
                     year : Any,token : Any
                    ) : Completable


    fun updateUser(email: String,token: String): Completable

    fun getUser(): Observable<List<UserData>>

    fun getPinDetailsLocationQuery(id : String, token : String) : Observable<PinData>

    fun addLikeLocationQuery(id : String, token : String) : Completable

    fun removeLikeLocationQuery(id : String, token : String) : Completable

    fun addCommentMarkerLocationQuery(markerId : String, message : String, token : String) : Observable<ReturnAddCommentData>

    fun editCommentLocationQuery(commentId : String,newMessage : String,token : String) : Completable

    fun deleteCommentLocationQuery(commentId : String,token : String) : Completable

    fun likeDislikeCommentLocationQuery(commentId : String,
                                        isLikedComment : Int,
                                        isDisLikedComment : Int,
                                        token : String) : Completable

    fun getSearchDetailsQuery(text : String,typeList : MutableList<Int?>,token: String) : Observable<List<SearchDataDetails>>

    fun getAllBookmaker(token : String) : Observable<MutableList<Int>>
    fun updateBookmakerQuery(markerId : String,isBookmarked : Boolean,token : String) : Completable

}