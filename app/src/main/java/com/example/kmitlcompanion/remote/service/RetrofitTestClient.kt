package com.example.kmitlcompanion.remote.service

import com.example.kmitlcompanion.data.model.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface RetrofitTestClient {
    @Multipart
    @POST("getlocationquery")
    fun getLocationQuery(@Part("latitude") latitude: Double , @Part("longitude") longitude: Double , @Part("token") token:Any): Observable<LocationQuery>

    @GET("getmappoints")
    fun getMapPoints(@Query("token") token: String): Observable<List<MapPointData>>


    @Multipart
    @POST("createlocationquery")
    fun createLocationQuery(@Part("name") name : String,@Part("place") place : String,@Part("address") address : String, @Part("latitude") latitude: Double, @Part("longitude") longitude: Double  , @Part("description") detail : String ,@Part("type") type : String ,@Part image:List<MultipartBody.Part>,@Part("token") token:String): Completable

    @Multipart
    @POST("createpubliclocationquery")
    fun createPublicLocationQuery(@Part("name") name : String,@Part("place") place : String,@Part("address") address : String, @Part("latitude") latitude: Double, @Part("longitude") longitude: Double  , @Part("description") detail : String ,@Part("type") type : String ,@Part image:List<MultipartBody.Part>,@Part("token") token:String): Completable

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

    @Multipart
    @POST("getpindetailslocationquery")
    fun getPinDetailsLocationQuery(@Part("id") id: String, @Part("token") token : String): Observable<PinData>

    @Multipart
    @POST("likelocationquery")
    fun addLikeLocationQuery(@Part("id") id: String, @Part("token") token : String): Completable

    @Multipart
    @POST("dislikelocationquery")
    fun removeLikeLocationQuery(@Part("id") id: String, @Part("token") token : String): Completable

    @Multipart
    @POST("addcommentmarkerlocationquery")
    fun addCommentMarkerLocationQuery(@Part("id") markerId: String,
                                      @Part("message") message : String,
                                      @Part("token") token : String): Observable<ReturnAddCommentData>

    @Multipart
    @POST("editcommentmarkerlocationquery")
    fun editCommentMarkerLocationQuery(@Part("id") markerId: String,
                                       @Part("newmessage") newMessage : String,
                                       @Part("token") token : String): Completable

    @Multipart
    @POST("deletecommentmarkerlocationquery")
    fun deleteCommentMarkerLocationQuery(@Part("id") commentId: String, @Part("token") token : String): Completable

    @Multipart
    @POST("likedislikecommentmarkerlocationquery")
    fun likeDislikeCommentMarkerLocationQuery(@Part("id") commentId: String,
                                              @Part("isLikedComment") isLikedComment : Int ,
                                              @Part("isDisLikedComment") isDisLikedComment : Int ,
                                              @Part("token") token : String): Completable

}