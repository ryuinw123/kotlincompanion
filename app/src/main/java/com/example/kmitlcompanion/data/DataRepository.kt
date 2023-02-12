package com.example.kmitlcompanion.data

import com.example.kmitlcompanion.data.mapper.CommentMapper
import com.example.kmitlcompanion.data.mapper.MapPointMapper
import com.example.kmitlcompanion.data.mapper.SearchMapper
import com.example.kmitlcompanion.data.model.*
import com.example.kmitlcompanion.data.store.DataStore
import com.example.kmitlcompanion.data.util.ContentResolverUtil
import com.example.kmitlcompanion.data.util.TimeUtils
import com.example.kmitlcompanion.domain.model.*
import com.example.kmitlcompanion.domain.repository.DomainRepository
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.style.expressions.dsl.generated.get
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val dataStore: DataStore,
    private val timeUtils: TimeUtils,
    private val mapper: MapPointMapper,
    private val commentMapper : CommentMapper,
    private val contentResolverUtil: ContentResolverUtil,
    private val searchMapper: SearchMapper,
) : DomainRepository{

    private fun getToken(): String{
        return dataStore.getRemoteData(false).getUser().blockingFirst()[0].token
    }

    override fun getLocationQuery(latitude: Double, longitude: Double): Observable<LocationDetail> {

        return dataStore.getRemoteData(true).getLocationQuery(latitude,longitude,getToken())
            .map {
                LocationDetail(
                    point = Point.fromLngLat(longitude,latitude)?: null,
                    address = it?.address,
                    place = it?.place
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

    override fun createLocationQuery(location: LocationData): Completable {
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
        typeList: MutableList<Int?>
    ): Observable<List<SearchDetail>> {
        return dataStore.getRemoteData(true)
            .getSearchDetailsQuery(text=text,typeList = typeList, token = getToken()).map {
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
}