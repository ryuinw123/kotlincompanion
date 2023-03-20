package com.example.kmitlcompanion.data

import android.content.Intent
import com.example.kmitlcompanion.data.mapper.CommentMapper
import com.example.kmitlcompanion.data.mapper.EventMapper
import com.example.kmitlcompanion.data.mapper.MapPointMapper
import com.example.kmitlcompanion.data.mapper.SearchMapper
import com.example.kmitlcompanion.data.model.*
import com.example.kmitlcompanion.data.store.DataStore
import com.example.kmitlcompanion.data.util.ContentResolverUtil
import com.example.kmitlcompanion.data.util.TimeUtils
import com.example.kmitlcompanion.domain.model.*
import com.example.kmitlcompanion.domain.repository.DomainRepository
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mapbox.geojson.Point
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.sql.Timestamp
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val dataStore: DataStore,
    private val timeUtils: TimeUtils,
    private val mapper: MapPointMapper,
    private val commentMapper : CommentMapper,
    private val contentResolverUtil: ContentResolverUtil,
    private val searchMapper: SearchMapper,
    private val eventMapper: EventMapper,
) : DomainRepository{

    private fun getToken(): String{
        return dataStore.getRemoteData(false).getUser().blockingFirst()[0].token
    }

    private fun getUserId() : Int {
        return  dataStore.getRemoteData(false).getUser().blockingFirst()[0].id
    }

    override fun getLastestNotificationTime(event_id: Int): Observable<Timestamp> {
        val user_id = getUserId()

        return dataStore.getRemoteData(false).getLastestNotificationTime(event_id , user_id).map { list ->
            list.firstOrNull()?.let {
                Timestamp(it)
            } ?: Timestamp(0L)
        }
    }


    override fun updateNotificationTime(eventTime: EventTime): Completable {
        val user_id = getUserId()
        return dataStore.getRemoteData(false).updateNotificationTime(EventTimeData(
            eventTime.id,
            user_id,
            eventTime.time.time
        ))
    }

    override fun createEventQuery(event: Event): Completable {
        val file = event.file
        val uri = event.uri
        var imageList : MutableList<MultipartBody.Part> = mutableListOf()
        if (file.isNotEmpty() && uri.isNotEmpty()){
            for (i in 0 until(file.size) ){
                val requestFile = file[i]?.asRequestBody(contentResolverUtil.getMediaType(uri[i]!!))!!
                imageList.add(MultipartBody.Part.createFormData("image",file[i]!!.name,requestFile))
            }
        }
        return dataStore.getRemoteData(true).createEventQuery(
            name = event.name!!,
            detail = event.description!!,
            startTime = event.startTime!!,
            endTime = event.endTime!!,
            point = event.point,
            image = imageList,
            type = event.type!!,
            url = event.url ?:"",
            token = getToken()
        )
    }

    override fun getEventLocations(): Observable<EventInformation> {
        val d = dataStore.getRemoteData(true).getEventLocations(getToken()).map { list ->
            EventInformation(
                eventPoints = list.map { eventMapper.mapToDomain(it) },
                source = Source.REMOTE,
                timeStamp = timeUtils.currentTime
            )
        }.map { information ->
            Observable.just(information)
        }.flatMap { it }
        return d
    }

    override fun getLocationQuery(latitude: Double, longitude: Double): Observable<LocationDetail> {

        return dataStore.getRemoteData(true).getLocationQuery(latitude,longitude,getToken())
            .map {
                LocationDetail(
                    point = Point.fromLngLat(longitude,latitude)!!,
                    address = it?.address,
                    place = it?.place,
                )
            }
    }

    override fun getMapPoints(): Observable<MapInformation> {
        return dataStore.getRemoteData(true).getMapPoints(token=getToken())
            //token=tokenUtils.getToken())
            //ตัวอย่างข้อมูลของ datastore [{"name": "John", "commentId": 30, "latitude": "13.779677724153272", "longitude": "100.67650630259816", "description": "noob"}, {"name": "Cena", "commentId": 31, "latitude": "13.779677724153272", "longitude": "100.97650630259816", "description": "noob2"}]
            .map { list ->
                //ตัวอย่างข้อมูลของ list [MapPointData(description=noob, commentId=30, latitude=13.779677724153272, longitude=100.67650630259816, name=John), MapPointData(description=noob2, commentId=31, latitude=13.779677724153272, longitude=100.97650630259815, name=Cena)]
                MapInformation(
                    mapPoints = list.map { mapper.mapToDomain(it) },
                    source = Source.REMOTE,
                    timeStamp = timeUtils.currentTime
                )
                //ตัวอย่างข้อมูลที่ออกมา MapInformation(mapPoints=[MapPoint(description=noob, commentId=30, latitude=13.779677724153272, longitude=100.67650630259816, name=John), MapPoint(description=noob2, commentId=31, latitude=13.779677724153272, longitude=100.97650630259815, name=Cena)], source=REMOTE, timeStamp=1664528459529)
            }
            .map { information ->
                //ตัวอย่างข้อมูลของ information MapInformation(mapPoints=[MapPoint(description=noob, commentId=30, latitude=13.779677724153272, longitude=100.67650630259816, name=John), MapPoint(description=noob2, commentId=31, latitude=13.779677724153272, longitude=100.97650630259815, name=Cena)], source=REMOTE, timeStamp=1664529846636)
                //val list = information.mapPoints.map { mapper.mapToData(it) }

                /*val updateLastLocationTimeStamp = dataStore.getRemoteData(false)
                    .updateLastLocationTimeStamp(information.timeStamp)*/

                /*dataStore.getRemoteData(false)
                    .saveMapPoints(list)
                    .andThen(updateLastLocationTimeStamp)
                    .andThen(Observable.just(information))*/
                Observable.just(information)

            }
            .flatMap { it }

    }

    override fun createLocationQuery(location: Location): Completable {
        val file = location.file
        val uri = location.uri
        var imageList : MutableList<MultipartBody.Part> = mutableListOf()

        if (file.isNotEmpty() && uri.isNotEmpty()){
            for (i in 0 until(file.size) ){
                val requestFile = file[i]?.asRequestBody(contentResolverUtil.getMediaType(uri[i]!!))!!
                imageList.add(MultipartBody.Part.createFormData("image",file[i]!!.name,requestFile))
            }
        }
        return dataStore.getRemoteData(true).createLocationQuery(
            name = location.inputName!!,
            place = location.place!!,
            address = location.address!!,
            latitude = location.point!!.latitude(),
            longitude = location.point!!.longitude(),
            type = location.type!!,
            detail = location.description!!,
            image = imageList,
            token = getToken()
        )
    }

    override fun createPublicLocationQuery(location: LocationPublicData): Completable {
        val file = location.file
        val uri = location.uri
        var imageList : MutableList<MultipartBody.Part> = mutableListOf()

        if (file.isNotEmpty() && uri.isNotEmpty()){
            for (i in 0 until(file.size) ){
                val requestFile = file[i]?.asRequestBody(contentResolverUtil.getMediaType(uri[i]!!))!!
                imageList.add(MultipartBody.Part.createFormData("image",file[i]!!.name,requestFile))
            }
        }
        return dataStore.getRemoteData(true).createPublicLocationQuery(
            name = location.inputName,
            place = location.place,
            address = location.address,
            latitude = location.latitude,
            longitude = location.longitude,
            type = location.type,
            detail = location.description,
            image = imageList,
            token = getToken()
        )
    }

    override fun postLogin(authCode: String): Observable<ReturnLoginData> {
        return dataStore.getRemoteData(true).postLogin(authCode)
    }

    override fun postUserData(
        name: Any,
        surname: Any,
        faculty: Any,
        department: Any,
        year: Any,
        token: Any
    ): Completable {
        return dataStore.getRemoteData(true).postUserData(name,surname,faculty,department,year,token)
    }

    override fun updateUser(email: String, token: String): Completable {
        return dataStore.getRemoteData(false).updateUser(email,token)
    }

    override fun getUser(): Observable<List<UserData>> {
        return dataStore.getRemoteData(false).getUser()
    }


    override fun getPinDetailsLocationQuery(id: String): Observable<PinDetail> {
        return dataStore.getRemoteData(true).getPinDetailsLocationQuery(
            id,
            getToken()
        )
            .map { data ->
                PinDetail(
                    likeCounting = data.likeCounting,
                    isLiked = data.isLiked,
                    comment = data.comment.map{ commentMapper.mapToDomain(it)} as MutableList<Comment>,
                    isBookmarked = data.isBookmarked,
                    isMyPin = data.isMyPin,
                    createdUserName = data.createdUserName,
                )  //list.map { commentMapper.mapToDomain(it.comment) }
            }
    }

    override fun addLikeLocationQuery(id: String): Completable {
        return dataStore.getRemoteData(true).addLikeLocationQuery(id,getToken())
    }

    override fun removeLikeLocationQuery(id: String): Completable {
        return dataStore.getRemoteData(true).removeLikeLocationQuery(id,getToken())
    }

    override fun addCommentMarkerLocationQuery(
        markerId: String,
        message: String
    ): Observable<ReturnAddComment> {
        return dataStore.getRemoteData(true).addCommentMarkerLocationQuery(markerId,message,getToken()).map {
            ReturnAddComment(
                commentId = it.commentId,
                author = it.author
            )
        }
    }

    override fun editCommentLocationQuery(commentId: String, newMessage: String): Completable {
        return dataStore.getRemoteData(true).editCommentLocationQuery(commentId, newMessage,getToken())
    }

    override fun deleteCommentLocationQuery(commentId: String): Completable {
        return dataStore.getRemoteData(true).deleteCommentLocationQuery(commentId,getToken())
    }

    override fun likeDislikeCommentLocationQuery(
        commentId: String,
        isLikedComment: Int,
        isDisLikedComment: Int
    ): Completable {
        return dataStore.getRemoteData(true)
            .likeDislikeCommentLocationQuery(commentId,isLikedComment,isDisLikedComment,getToken())
    }


    override fun getSearchDetailsQuery(
        text: String,
        typeList: MutableList<Int?>,
        latitude: Double,
        longitude: Double,
    ): Observable<List<SearchDetail>> {
        return dataStore.getRemoteData(true)
            .getSearchDetailsQuery(text=text, typeList = typeList,latitude = latitude,longitude = longitude, token = getToken()).map {
                it.map { searchDataD ->
                    searchMapper.mapToDomain(searchDataD)
            }
        }
    }

    override fun getAllBookmaker(): Observable<MutableList<Int>> {
        return dataStore.getRemoteData(true).getAllBookmaker(token = getToken())
    }

    override fun updateBookmakerQuery(markerId: String, isBookmarked: Boolean): Completable {
        return dataStore.getRemoteData(true)
            .updateBookmakerQuery(markerId = markerId,isBookmarked = isBookmarked,token = getToken())
    }

    //For event
    override fun changeEventLikeLocationQuery(eventId: String, isLike: Boolean): Completable {
        return dataStore.getRemoteData(true).changeEventLikeLocationQuery(eventId,isLike,getToken())
    }

    override fun changeEventBookmarkLocationQuery(eventId: String, isMark: Boolean): Completable {
        return dataStore.getRemoteData(true).changeEventBookmarkLocationQuery(eventId,isMark,getToken())
    }

    override fun getEventDetailsLocationQuery(id: String): Observable<PinEventDetail> {
        return dataStore.getRemoteData(true).getEventDetailsLocationQuery(id = id, token = getToken()).map {
            PinEventDetail(
                eventLikeCounting= it.eventLikeCounting,
                isEventLiked = it.isEventLiked,
                isEventBookmarked = it.isEventBookmarked,
                isMyPin = it.isMyPin,
                createdUserName = it.createdUserName,
            )
        }
    }

    override fun getAllEventBookMarker(): Observable<MutableList<Int>> {
        return dataStore.getRemoteData(true).getAllEventBookMarker(getToken())
    }

    override fun deleteMarkerLocationQuery(id: String): Completable {
        return dataStore.getRemoteData(true).deleteMarkerLocationQuery(id,getToken())
    }

    override fun deleteEventLocationQuery(id: String): Completable {
        return dataStore.getRemoteData(true).deleteEventLocationQuery(id,getToken())
    }


    override fun editLocationQuery(
        id: String,
        name: String,
        type: String,
        description: String,
        image: MutableList<Pair<Int, Any>>
    ): Completable {
        var imageList : MutableList<MultipartBody.Part?> = mutableListOf()
        var imageUrl : MutableList<String?> = mutableListOf()
        image.forEach {
            if (it.first == 0){
                val file = ImagePicker.getFile(it.second as Intent)
                val uri = (it.second as Intent).data
                val requestFile = file?.asRequestBody(contentResolverUtil.getMediaType(uri!!))!!
                imageList.add(MultipartBody.Part.createFormData("image",file!!.name,requestFile))
                imageUrl.add("null")
            }else if(it.first == 1){
                imageList.add(MultipartBody.Part.createFormData("image","null"))
                imageUrl.add(it.second.toString())
            }
        }
        return dataStore.getRemoteData(true).editLocationQuery(
            id,
            name,
            type,
            description,
            imageList ,
            imageUrl,
            getToken()
        )
    }


    override fun editEventLocationQuery(
        eventId: String,
        name: String,
        description: String,
        image: MutableList<Pair<Int, Any>>,
        type: Int,
        url: String,
    ): Completable {
        var imageList : MutableList<MultipartBody.Part?> = mutableListOf()
        var imageUrl : MutableList<String?> = mutableListOf()

        image.forEach {
            if (it.first == 0){
                val file = ImagePicker.getFile(it.second as Intent)
                val uri = (it.second as Intent).data
                val requestFile = file?.asRequestBody(contentResolverUtil.getMediaType(uri!!))!!
                imageList.add(MultipartBody.Part.createFormData("image",file!!.name,requestFile))
                imageUrl.add("null")
            }else if(it.first == 1){
                imageList.add(MultipartBody.Part.createFormData("image","null"))
                imageUrl.add(it.second.toString())
            }
        }

        return dataStore.getRemoteData(true).editEventLocationQuery(
            eventId,
            name,
            description,
            imageList,
            imageUrl,
            type,
            url,
            getToken()
        )
    }

    override fun settingsGetUserData(): Observable<UserSettingsData> {
        return dataStore.getRemoteData(true).settingsGetUserData(getToken()).map {
            UserSettingsData(
                username = it.username,
                faculty = it.faculty,
                email = it.email,
                department = it.department,
                year = it.year,
            )
        }
    }

    override fun settingsEditUpdateUserData(userEditData: UserEditData): Completable {
        return dataStore.getRemoteData(true).settingsEditUpdateUserData(
            username = userEditData.username!!,
            department = userEditData.department!!,
            faculty = userEditData.faculty!!,
            year = userEditData.year!!,
            token = getToken()
        )
    }

    override fun saveNotificationLogDetails(
        id: Long,
        name: String,
        startTime: String,
        endTime: String,
        imageLinks: String
    ): Completable {
        return dataStore.getRemoteData(false).saveNotificationLogDetails(
            id,
            name,
            startTime,
            endTime,
            imageLinks,
        )
    }

    override fun getNotificationLogDetails(): Observable<List<NotiLogDetails>> {
        return dataStore.getRemoteData(false).getNotificationLogDetails().map { notiLogData ->
            notiLogData.map {
                NotiLogDetails(
                    id = it.id,
                    name = it.name,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    imageLinks = it.imageLinks,
                )
            }
        }
    }

    override fun deleteAllNotificationLogDetails(): Completable {
        return dataStore.getRemoteData(false).deleteAllNotificationLogDetails()
    }

    override fun deleteByIDNotificationLogDetails(id: Long): Completable {
        return dataStore.getRemoteData(false).deleteByIDNotificationLogDetails(id)
    }

    override fun reportEventLocationQueryDetails(
        id: Long,
        reason: String,
        details: String
    ): Completable {
        return dataStore.getRemoteData(true).reportEventLocationQueryDetails(id,reason,details,getToken())
    }

    override fun reportMarkerLocationQueryDetails(
        id: Long,
        reason: String,
        details: String
    ): Completable {
        return dataStore.getRemoteData(true).reportMarkerLocationQueryDetails(id,reason,details, getToken())
    }
}