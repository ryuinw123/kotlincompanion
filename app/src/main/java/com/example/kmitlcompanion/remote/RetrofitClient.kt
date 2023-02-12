package com.example.kmitlcompanion.remote

import android.util.Log
import com.example.kmitlcompanion.data.model.*
import com.example.kmitlcompanion.remote.service.RetrofitTestClient
import com.example.kmitlcompanion.data.repository.RemoteRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import javax.inject.Inject

class RetrofitClient @Inject constructor(
    private val retrofitTestClient: RetrofitTestClient,
) : RemoteRepository {
    override fun getLocationQuery(
        latitude: Double,
        longitude: Double,
        token: String
    ): Observable<LocationQuery> {
        return retrofitTestClient.getLocationQuery(latitude,longitude,token)
    }

    override fun getMapPoints(token: String): Observable<List<MapPointData>> {
        return retrofitTestClient.getMapPoints(token)
    }

    override fun createLocationQuery(
        name: String,
        place: String,
        address: String,
        latitude: Double,
        longitude: Double,
        detail: String,
        type: String,
        image: List<MultipartBody.Part>,
        token: String
    ): Completable {
        return retrofitTestClient.createLocationQuery(name, place, address, latitude, longitude, detail, type, image, token)
    }

    override fun createPublicLocationQuery(
        name: String,
        place: String,
        address: String,
        latitude: Double,
        longitude: Double,
        detail: String,
        type: String,
        image: List<MultipartBody.Part>,
        token: String
    ): Completable {
        return retrofitTestClient.createPublicLocationQuery(name, place, address, latitude, longitude, detail, type, image, token)
    }

    override fun postLogin(authCode: String): Observable<ReturnLoginData> {
        return retrofitTestClient.postLogin(authCode)
    }

    override fun postUserData(
        name: Any,
        surname: Any,
        faculty: Any,
        department: Any,
        year: Any,
        token: Any
    ): Completable {
        return retrofitTestClient.postUserData(name,surname,faculty,department,year,token)
    }

    override fun getPinDetailsLocationQuery(id: String, token: String): Observable<PinData> {
        return retrofitTestClient.getPinDetailsLocationQuery(id,token)
    }


    override fun addLikeLocationQuery(id: String, token: String): Completable {
        return retrofitTestClient.addLikeLocationQuery(id,token)
    }

    override fun removeLikeLocationQuery(id: String, token: String): Completable {
        return retrofitTestClient.removeLikeLocationQuery(id,token)
    }

    override fun addCommentMarkerLocationQuery(
        markerId: String,
        message: String,
        token: String
    ): Observable<ReturnAddCommentData> {
        return retrofitTestClient.addCommentMarkerLocationQuery(markerId,message,token)
    }

    override fun editCommentLocationQuery(
        commentId: String,
        newMessage: String,
        token: String
    ): Completable {
        return retrofitTestClient.editCommentMarkerLocationQuery(commentId,newMessage,token)
    }

    override fun deleteCommentLocationQuery(commentId: String, token: String): Completable {
        return retrofitTestClient.deleteCommentMarkerLocationQuery(commentId,token)
    }

    override fun likeDislikeCommentLocationQuery(
        commentId: String,
        isLikedComment: Int,
        isDisLikedComment: Int,
        token: String
    ): Completable {
        return retrofitTestClient.likeDislikeCommentMarkerLocationQuery(commentId,isLikedComment,isDisLikedComment,token)
    }

    override fun getSearchDetailsQuery(
        text: String,
        typeList: MutableList<Int?>,
        token: String
    ): Observable<List<SearchDataDetails>> {
        return retrofitTestClient.getSearchDetailsQuery(text,typeList,token)
    }

    override fun getAllBookmaker(token: String): Observable<MutableList<Int>> {
        return retrofitTestClient.getAllBookmakerLocationQuery(token)
    }

    override fun updateBookmakerQuery(
        markerId: String,
        isBookmarked: Boolean,
        token: String
    ): Completable {
        return retrofitTestClient.updateBookmakerLocationQuery(markerId,isBookmarked,token)
    }
}