package com.example.kmitlcompanion.remote.service

import com.example.kmitlcompanion.data.model.*
import com.example.kmitlcompanion.domain.model.EventArea
import com.example.kmitlcompanion.domain.model.PinEventDetail
import com.example.kmitlcompanion.domain.usecases.ChangeEventBookmarkLocationQuery
import com.example.kmitlcompanion.domain.usecases.ChangeEventLikeLocationQuery
import com.mapbox.geojson.Point
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface RetrofitTestClient {
    @Multipart
    @POST("getlocationquery")
    fun getLocationQuery(
        @Part("latitude") latitude: Double,
        @Part("longitude") longitude: Double,
        @Part("token") token: Any
    ): Observable<LocationQuery>

    @GET("getmappoints")
    fun getMapPoints(@Query("token") token: String): Observable<List<MapPointData>>

    @GET("geteventlocations")
    fun getEventLocations(@Query("token") token: String): Observable<List<EventAreaData>>

    @Multipart
    @POST("createeventquery")
    fun createEventQuery(
        @Part("name") name: String,
        @Part("description") detail: String,
        @Part("startTime") startTime: String,
        @Part("endTime") endTime: String,
        @Part("point") point: List<Point>,
        @Part image: List<MultipartBody.Part>,
        @Part("type") type: Int,
        @Part("url") url: String,
        @Part("token") token: String
    ): Completable

    @Multipart
    @POST("createlocationquery")
    fun createLocationQuery(
        @Part("name") name: String,
        @Part("place") place: String,
        @Part("address") address: String,
        @Part("latitude") latitude: Double,
        @Part("longitude") longitude: Double,
        @Part("description") detail: String,
        @Part("type") type: String,
        @Part image: List<MultipartBody.Part>,
        @Part("token") token: String
    ): Completable

    @Multipart
    @POST("createpubliclocationquery")
    fun createPublicLocationQuery(
        @Part("name") name: String,
        @Part("place") place: String,
        @Part("address") address: String,
        @Part("latitude") latitude: Double,
        @Part("longitude") longitude: Double,
        @Part("description") detail: String,
        @Part("type") type: String,
        @Part image: List<MultipartBody.Part>,
        @Part("token") token: String
    ): Completable

    @Multipart
    @POST("login")
    fun postLogin(@Part("authCode") authCode: String): Observable<ReturnLoginData>

    @Multipart
    @POST("postuserdata")
    fun postUserData(
        @Part("name") name: Any,
        @Part("surname") surname: Any,
        @Part("faculty") faculty: Any,
        @Part("department") department: Any,
        @Part("year") year: Any,
        @Part("authCode") token: Any
    ): Completable

    @Multipart
    @POST("getpindetailslocationquery")
    fun getPinDetailsLocationQuery(
        @Part("id") id: String,
        @Part("token") token: String
    ): Observable<PinData>

    @Multipart
    @POST("likelocationquery")
    fun addLikeLocationQuery(@Part("id") id: String, @Part("token") token: String): Completable

    @Multipart
    @POST("dislikelocationquery")
    fun removeLikeLocationQuery(@Part("id") id: String, @Part("token") token: String): Completable

    @Multipart
    @POST("addcommentmarkerlocationquery")
    fun addCommentMarkerLocationQuery(
        @Part("id") markerId: String,
        @Part("message") message: String,
        @Part("token") token: String
    ): Observable<ReturnAddCommentData>

    @Multipart
    @POST("editcommentmarkerlocationquery")
    fun editCommentMarkerLocationQuery(
        @Part("id") markerId: String,
        @Part("newmessage") newMessage: String,
        @Part("token") token: String
    ): Completable

    @Multipart
    @POST("deletecommentmarkerlocationquery")
    fun deleteCommentMarkerLocationQuery(
        @Part("id") commentId: String,
        @Part("token") token: String
    ): Completable

    @Multipart
    @POST("likedislikecommentmarkerlocationquery")
    fun likeDislikeCommentMarkerLocationQuery(
        @Part("id") commentId: String,
        @Part("isLikedComment") isLikedComment: Int,
        @Part("isDisLikedComment") isDisLikedComment: Int,
        @Part("token") token: String
    ): Completable


    @Multipart
    @POST("getsearchdetailslocationquery")
    fun getSearchDetailsQuery(
        @Part("text") text: String,
        @Part("typeList") typeList: List<Int?>,
        @Part("lat") latitude: Double,
        @Part("long") longitude: Double,
        @Part("token") token: String,
    ): Observable<List<SearchDataDetails>>

    @Multipart
    @POST("getallbookmakerlocationquery")
    fun getAllBookmakerLocationQuery(
        @Part("token") token: String,
    ): Observable<MutableList<Int>>

    @Multipart
    @POST("updatebookmakerlocationquery")
    fun updateBookmakerLocationQuery(
        @Part("markerId") markerId: String,
        @Part("isBookmarked") isBookmarked: Boolean,
        @Part("token") token: String,
    ): Completable

    @Multipart
    @POST("changeeventlikelocationquery")
    fun changeEventLikeLocationQuery(
        @Part("eventId") eventId: String,
        @Part("isLiked") isLiked: Boolean,
        @Part("token") token: String
    ): Completable

    @Multipart
    @POST("changeeventbookmarklocationquery")
    fun changeEventBookmarkLocationQuery(
        @Part("eventId") eventId: String,
        @Part("isMark") isMark: Boolean,
        @Part("token") token: String
    ): Completable

    @Multipart
    @POST("geteventdetailslocationquery")
    fun getEventDetailsLocationQuery(
        @Part("id") id: String,
        @Part("token") token: String,
    ): Observable<PinEventData>

    @Multipart
    @POST("getalleventbookmarker")
    fun getAllEventBookMarker(@Part("token") token: String): Observable<MutableList<Int>>

    @Multipart
    @POST("deletemarkerlocationquery")
    fun deleteMarkerLocationQuery(@Part("id") id: String, @Part("token") token: String): Completable

    @Multipart
    @POST("deleteeventlocationquery")
    fun deleteEventLocationQuery(@Part("id") id: String, @Part("token") token: String): Completable

    @Multipart
    @POST("editmarkerlocationquery")
    fun editLocationQuery(
        @Part("id") id: String,
        @Part("name") name: String,
        @Part("type") type: String,
        @Part("description") description: String,
        @Part image: List<MultipartBody.Part?>,
        @Part("imageUrl") imageUrl: List<String?>,
        @Part("token") token: String
    ): Completable

    @Multipart
    @POST("editeventlocationquery")
    fun editEventLocationQuery(
        @Part("eventId") eventId: String,
        @Part("name") name: String,
        @Part("description") description: String,
        @Part image: List<MultipartBody.Part?>,
        @Part("imageUrl") imageUrl: List<String?>,
        @Part("type") type: Int,
        @Part("url") url: String,
        @Part("token") token: String
    ): Completable

    @Multipart
    @POST("settingsgetuserdata")
    fun settingsGetUserData(@Part("token") token: String) : Observable<UserSettingsDataModel>

    @Multipart
    @POST("settingseditupdateuserdata")
    fun settingsEditUpdateUserData(
        @Part("username") username : String,
        @Part("faculty") faculty :String,
        @Part("department") department :String,
        @Part("year") year : String,
        @Part("token") token: String) : Completable

    @Multipart
    @POST("reporteventlocationquery")
    fun reportEventLocationQueryDetails(
                                        @Part("id") id : Long,
                                        @Part("reason") reason : String,
                                        @Part("details") details : String,
                                        @Part("token") token: String) : Completable

    @Multipart
    @POST("reportmarkerlocationquery")
    fun reportMarkerLocationQueryDetails(
                                        @Part("id") id : Long,
                                        @Part("reason") reason : String,
                                        @Part("details") details : String,
                                        @Part("token") token: String) : Completable

    @Multipart
    @POST("checkvalidcreatemarkercount")
    fun checkValidCreateMarkerCountDetails(@Part("token") token: String) : Observable<Int>

    @Multipart
    @POST("checkvalidcreateeventcount")
    fun checkValidCreateEventCountDetails(@Part("token") token: String) : Observable<Int>


}