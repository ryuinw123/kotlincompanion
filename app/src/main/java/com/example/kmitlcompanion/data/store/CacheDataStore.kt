package com.example.kmitlcompanion.data.store

import com.example.kmitlcompanion.data.model.*
import com.example.kmitlcompanion.data.repository.CacheRepository
import com.example.kmitlcompanion.data.repository.DataRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import javax.inject.Inject

class CacheDataStore @Inject constructor(
    private val cacheRepository: CacheRepository
) :DataRepository {
    override fun getLocationQuery(
        latitude: Double,
        longitude: Double,
        token: String
    ): Observable<LocationQuery> {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun getMapPoints(token: String): Observable<List<MapPointData>> {
        return cacheRepository.getMapPoints(token)
    }

    override fun updateLastLocationTimeStamp(timestamp: Long): Completable {
        return cacheRepository.updateLastLocationTimeStamp(timestamp)
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
        throw IllegalStateException("Function not currently supported!")
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
        throw IllegalStateException("Function not currently supported!")
    }

    override fun postLogin(authCode: String): Observable<ReturnLoginData> {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun postUserData(
        name: Any,
        surname: Any,
        faculty: Any,
        department: Any,
        year: Any,
        token: Any,
    ): Completable {
        throw IllegalStateException("Function not currently supported!")
    }


    override fun saveMapPoints(list: List<MapPointData>): Completable {
        return cacheRepository.saveMapPoints(list)
    }

    override fun updateUser(email: String, token: String): Completable {
        return cacheRepository.updateUser(email,token)
    }

    override fun getUser(): Observable<List<UserData>> {
        return cacheRepository.getUser()
    }

    override fun getPinDetailsLocationQuery(id: String, token: String): Observable<PinData> {
        throw IllegalStateException("Function not currently supported!")
    }


    override fun addLikeLocationQuery(id: String, token: String): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun removeLikeLocationQuery(id: String, token: String): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun addCommentMarkerLocationQuery(
        markerId: String,
        message: String,
        token: String
    ): Observable<ReturnAddCommentData> {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun editCommentLocationQuery(commentId: String, newMessage: String, token: String): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun deleteCommentLocationQuery(commentId: String, token: String): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun likeDislikeCommentLocationQuery(
        commentId: String,
        isLikedComment: Int,
        isDisLikedComment: Int,
        token: String
    ): Completable {
        throw IllegalStateException("Function not currently supported!")
    }
}