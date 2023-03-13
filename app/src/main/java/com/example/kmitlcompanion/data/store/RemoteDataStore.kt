package com.example.kmitlcompanion.data.store

import com.example.kmitlcompanion.data.model.*
import com.example.kmitlcompanion.data.repository.DataRepository
import com.example.kmitlcompanion.data.repository.RemoteRepository
import com.mapbox.geojson.Point
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import javax.inject.Inject

class RemoteDataStore @Inject constructor(
    private val remoteRepository: RemoteRepository
) : DataRepository {
    
    override fun getLastestNotificationTime(event_id: Int, user_id: Int): Observable<Long?> {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun updateNotificationTime(eventData: EventTimeData): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun createEventQuery(
        name: String,
        detail: String,
        startTime: String,
        endTime: String,
        point: List<Point>,
        image: List<MultipartBody.Part>,
        token: String
    ): Completable {
        return remoteRepository.createEventQuery(name, detail, startTime,endTime, point, image, token)
    }

    override fun getEventLocations(token: String): Observable<List<EventAreaData>> {
        return remoteRepository.getEventLocations(token)
    }

    override fun getLocationQuery(
        latitude: Double,
        longitude: Double,
        token: String
    ): Observable<LocationQuery> {
        return remoteRepository.getLocationQuery(latitude,longitude,token)
    }

    override fun getMapPoints(token: String): Observable<List<MapPointData>> {
        return remoteRepository.getMapPoints(token)
    }

    override fun saveMapPoints(list: List<MapPointData>): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun updateLastLocationTimeStamp(timestamp: Long): Completable {
        throw IllegalStateException("Function not currently supported!")
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
        return remoteRepository.createLocationQuery(name, place, address, latitude, longitude, detail, type, image, token)
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
        return remoteRepository.createPublicLocationQuery(name, place, address, latitude, longitude, detail, type, image, token)
    }

    override fun postLogin(authCode: String): Observable<ReturnLoginData> {
        return remoteRepository.postLogin(authCode)
    }

    override fun postUserData(
        name: Any,
        surname: Any,
        faculty: Any,
        department: Any,
        year: Any,
        token:Any
    ): Completable {
        return remoteRepository.postUserData(name,surname,faculty,department,year,token)
    }

//    override fun postUserData(name: Any,
//                              surname : Any,
//                              faculty : Any,
//                              department : Any,
//                              year : Any): Completable{
//        return remoteRepository.postUserData(name,surname,faculty,department,year)
//    }

    override fun updateUser(email: String, token: String): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun getUser(): Observable<List<UserData>> {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun getPinDetailsLocationQuery(id: String, token: String): Observable<PinData> {
        return remoteRepository.getPinDetailsLocationQuery(id ,token)
    }

    override fun addLikeLocationQuery(id: String, token: String): Completable {
        return remoteRepository.addLikeLocationQuery(id,token)
    }

    override fun removeLikeLocationQuery(id: String, token: String): Completable {
        return remoteRepository.removeLikeLocationQuery(id,token)
    }

    override fun addCommentMarkerLocationQuery(
        markerId: String,
        message: String,
        token: String
    ): Observable<ReturnAddCommentData> {
        return remoteRepository.addCommentMarkerLocationQuery(markerId,message,token)
    }

    override fun editCommentLocationQuery(commentId: String, newMessage: String, token: String): Completable {
        return remoteRepository.editCommentLocationQuery(commentId,newMessage,token)
    }

    override fun deleteCommentLocationQuery(commentId: String, token: String): Completable {
        return remoteRepository.deleteCommentLocationQuery(commentId,token)
    }


    override fun likeDislikeCommentLocationQuery(
        commentId: String,
        isLikedComment: Int,
        isDisLikedComment: Int,
        token: String
    ): Completable {
        return remoteRepository.likeDislikeCommentLocationQuery(commentId,isLikedComment,isDisLikedComment,token)
    }

    override fun getSearchDetailsQuery(
        text: String,
        typeList: MutableList<Int?>,
        latitude: Double,
        longitude: Double,
        token: String
    ): Observable<List<SearchDataDetails>> {
         return remoteRepository.getSearchDetailsQuery(
             text = text,
             typeList = typeList,
             latitude = latitude,
             longitude = longitude,
             token = token
         )
    }

    override fun getAllBookmaker(token: String): Observable<MutableList<Int>> {
        return remoteRepository.getAllBookmaker(token = token)
    }

    override fun updateBookmakerQuery(
        markerId: String,
        isBookmarked: Boolean,
        token: String
    ): Completable {
        return remoteRepository.updateBookmakerQuery(markerId = markerId, isBookmarked = isBookmarked,token = token)
    }

    override fun changeEventLikeLocationQuery(
        eventId: String,
        isLike: Boolean,
        token: String
    ): Completable {
        return remoteRepository.changeEventLikeLocationQuery(eventId,isLike,token)
    }

    override fun changeEventBookmarkLocationQuery(
        eventId: String,
        isMark: Boolean,
        token: String
    ): Completable {
        return remoteRepository.changeEventBookmarkLocationQuery(eventId,isMark,token)
    }

    override fun getEventDetailsLocationQuery(
        id: String,
        token: String
    ): Observable<PinEventData> {
        return remoteRepository.getEventDetailsLocationQuery(id,token)
    }

    override fun getAllEventBookMarker(token: String): Observable<MutableList<Int>> {
        return remoteRepository.getAllEventBookMarker(token)
    }


    override fun deleteMarkerLocationQuery(id: String, token: String): Completable {
        return remoteRepository.deleteMarkerLocationQuery(id,token)
    }

    override fun deleteEventLocationQuery(id: String, token: String): Completable {
        return remoteRepository.deleteEventLocationQuery(id,token)
    }

    override fun editLocationQuery(
        id: String,
        name: String,
        type: String,
        description: String,
        image: List<MultipartBody.Part?>,
        imageUrl: List<String?>,
        token: String
    ): Completable {
        return remoteRepository.editLocationQuery(id, name, type, description, image,imageUrl, token)
    }

    override fun editEventLocationQuery(
        eventId: String,
        name: String,
        description: String,
        image: List<MultipartBody.Part?>,
        imageUrl: List<String?>,
        token: String
    ): Completable {
        return remoteRepository.editEventLocationQuery(eventId, name, description, image,imageUrl, token)
    }


    override fun settingsGetUserData(token: String): Observable<UserSettingsDataModel> {
        return remoteRepository.settingsGetUserData(token)
    }

    override fun settingsEditUpdateUserData(
        username: String,
        faculty: String,
        department: String,
        year: String,
        token: String
    ): Completable {
        return remoteRepository.settingsEditUpdateUserData(username,faculty,department,year,token)
    }

    override fun saveNotificationLogDetails(
        id: Long,
        name: String,
        startTime: String,
        endTime: String,
        imageLinks: String
    ): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun getNotificationLogDetails(): Observable<List<NotiLogData>> {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun deleteAllNotificationLogDetails(): Completable {
        throw IllegalStateException("Function not currently supported!")
    }

    override fun deleteByIDNotificationLogDetails(id: Long): Completable {
        throw IllegalStateException("Function not currently supported!")
    }
}