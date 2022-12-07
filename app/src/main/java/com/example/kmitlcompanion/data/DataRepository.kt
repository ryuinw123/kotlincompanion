package com.example.kmitlcompanion.data

import android.content.Context
import com.example.kmitlcompanion.data.mapper.MapPointMapper
import com.example.kmitlcompanion.data.model.LocationData
import com.example.kmitlcompanion.data.model.ReturnLoginData
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.data.store.DataStore
import com.example.kmitlcompanion.data.util.ContentResolverUtil
import com.example.kmitlcompanion.data.util.TimeUtils
import com.example.kmitlcompanion.domain.model.LocationDetail
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.domain.model.Source
import com.example.kmitlcompanion.domain.repository.DomainRepository
import com.mapbox.geojson.Point
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val dataStore: DataStore,
    private val timeUtils: TimeUtils,
    private val mapper: MapPointMapper,
    private val contentResolverUtil: ContentResolverUtil,


) : DomainRepository{
    override fun getLocationQuery(latitude: Double, longitude: Double): Observable<LocationDetail> {


        return dataStore.getRemoteData(true).getLocationQuery(latitude,longitude,"abc")
            .map {
                LocationDetail(
                    point = Point.fromLngLat(longitude,latitude)?: null,
                    address = it?.address,
                    place = it?.place
                )
            }
    }

    override fun getMapPoints(): Observable<MapInformation> {
        //println(dataStore.getRemoteData(true).getMapPoints().toString())
        return dataStore.getRemoteData(true).getMapPoints() //ตัวอย่างข้อมูลของ datastore [{"name": "John", "id": 30, "latitude": "13.779677724153272", "longitude": "100.67650630259816", "description": "noob"}, {"name": "Cena", "id": 31, "latitude": "13.779677724153272", "longitude": "100.97650630259816", "description": "noob2"}]
            .map { list ->
                //ตัวอย่างข้อมูลของ list [MapPointData(description=noob, id=30, latitude=13.779677724153272, longitude=100.67650630259816, name=John), MapPointData(description=noob2, id=31, latitude=13.779677724153272, longitude=100.97650630259815, name=Cena)]
                MapInformation(
                    mapPoints = list.map { mapper.mapToDomain(it) },
                    source = Source.REMOTE,
                    timeStamp = timeUtils.currentTime
                )
                //ตัวอย่างข้อมูลที่ออกมา MapInformation(mapPoints=[MapPoint(description=noob, id=30, latitude=13.779677724153272, longitude=100.67650630259816, name=John), MapPoint(description=noob2, id=31, latitude=13.779677724153272, longitude=100.97650630259815, name=Cena)], source=REMOTE, timeStamp=1664528459529)
            }
            .map { information ->
                //ตัวอย่างข้อมูลของ information MapInformation(mapPoints=[MapPoint(description=noob, id=30, latitude=13.779677724153272, longitude=100.67650630259816, name=John), MapPoint(description=noob2, id=31, latitude=13.779677724153272, longitude=100.97650630259815, name=Cena)], source=REMOTE, timeStamp=1664529846636)
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
        val requestFile = file.asRequestBody(contentResolverUtil.getMediaType(uri))
        val image = MultipartBody.Part.createFormData("image",file.name,requestFile)

        return dataStore.getRemoteData(true).createLocationQuery(
            latitude = location.latitude,
            longitude = location.longitude,
            name = location.place,
            type = location.type,
            detail = location.address,
            image = image,
            token = ""
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
}