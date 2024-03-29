package com.example.kmitlcompanion.data.repository

import com.example.kmitlcompanion.data.model.*
import com.example.kmitlcompanion.domain.model.PinEventDetail
import com.example.kmitlcompanion.domain.model.SearchDetail
import com.example.kmitlcompanion.domain.model.UserEditData
import com.example.kmitlcompanion.domain.model.UserSettingsData
import com.mapbox.geojson.Point
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import java.io.File
import java.net.URI

interface RemoteRepository {

    fun createEventQuery(name : String, detail: String,startTime : String,endTime : String, point : List<Point>, image: List<MultipartBody.Part>,type: Int,url: String, token: String) : Completable

    fun getEventLocations(token: String) : Observable<List<EventAreaData>>

    fun getLocationQuery(latitude: Double,longitude: Double,token : String): Observable<LocationQuery>

    fun getMapPoints(token:String): Observable<List<MapPointData>>

    fun createLocationQuery(name: String,
                            place:String,
                            address:String,
                            latitude: Double,
                            longitude: Double,
                            detail: String,
                            type: String,
                            image : List<MultipartBody.Part>,
                            token: String
                            ): Completable

    fun createPublicLocationQuery(name: String,
                                  place:String,
                                  address:String,
                                  latitude: Double,
                                  longitude: Double,
                                  detail: String,
                                  type: String,
                                  image : List<MultipartBody.Part>,
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

    fun getPinDetailsLocationQuery(id : String, token : String) : Observable<PinData>

    fun addLikeLocationQuery(id : String, token : String) : Completable

    fun removeLikeLocationQuery(id : String, token : String) : Completable

    fun addCommentMarkerLocationQuery(markerId : String, message : String, token : String) : Observable<ReturnAddCommentData>

    fun editCommentLocationQuery(commentId : String,newMessage : String, token : String) : Completable

    fun deleteCommentLocationQuery(commentId : String,token : String) : Completable

    fun likeDislikeCommentLocationQuery(commentId : String,
                                        isLikedComment : Int,
                                        isDisLikedComment : Int,
                                        token : String) : Completable

    fun getSearchDetailsQuery(text : String,typeList : MutableList<Int?>,latitude: Double,longitude: Double,token: String) : Observable<List<SearchDataDetails>>

    fun getAllBookmaker(token : String) : Observable<MutableList<Int>>

    fun updateBookmakerQuery(markerId : String,isBookmarked : Boolean,token : String) : Completable

    fun changeEventLikeLocationQuery(eventId : String,isLike : Boolean,token: String) : Completable

    fun changeEventBookmarkLocationQuery(eventId : String,isMark : Boolean,token: String) : Completable

    fun getEventDetailsLocationQuery(id : String,token : String) : Observable<PinEventData>

    fun getAllEventBookMarker(token : String) : Observable<MutableList<Int>>

    fun deleteMarkerLocationQuery(id : String,token: String) : Completable

    fun deleteEventLocationQuery(id : String,token: String) : Completable

    fun editLocationQuery(id: String, name : String, type : String, description : String, image: List<MultipartBody.Part?>,imageUrl : List<String?>,token: String) : Completable

    fun editEventLocationQuery(eventId: String, name : String, description : String, image: List<MultipartBody.Part?>,imageUrl : List<String?>,type: Int,url: String,token: String) : Completable

    fun settingsGetUserData(token: String) : Observable<UserSettingsDataModel>

    fun settingsEditUpdateUserData(username : String,faculty:String,department:String,year : String, token: String) : Completable

    fun reportEventLocationQueryDetails(id : Long,reason : String,details : String,token: String) : Completable

    fun reportMarkerLocationQueryDetails(id : Long,reason : String,details : String,token: String) : Completable

    fun checkValidCreateMarkerCountDetails(token: String) : Observable<Int>

    fun checkValidCreateEventCountDetails(token: String) : Observable<Int>

}


