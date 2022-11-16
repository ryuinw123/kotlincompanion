package com.example.kmitlcompanion.data

import android.util.Log
import com.example.kmitlcompanion.data.mapper.MapPointMapper
import com.example.kmitlcompanion.data.model.LocationQuery
import com.example.kmitlcompanion.data.model.ReturnLoginData
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.data.store.DataStore
import com.example.kmitlcompanion.data.util.TimeUtils
import com.example.kmitlcompanion.domain.model.*
import com.example.kmitlcompanion.domain.repository.DomainRepository
import com.mapbox.geojson.Point
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val dataStore: DataStore,
    private val timeUtils: TimeUtils,
    private val mapper: MapPointMapper

) : DomainRepository{
    override fun getLocationQuery(latitude: Double, longitude: Double): Observable<LocationDetail> {
//        val token = dataStore.getRemoteData(false).getUser().value
//        Log.d("ttttttttttttttttooooooooooooooooooken",token?.user_email + token?.user_token)
        return dataStore.getRemoteData(true).getLocationQuery(latitude,longitude,"")
            .map {
                LocationDetail(
                    point = Point.fromLngLat(longitude,latitude),
                    address = it.address,
                    place = it.place
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

    override fun createLocationQuery(latitude: Double, longitude: Double): Completable {
        return dataStore.getRemoteData(true).createLocationQuery(latitude,longitude)
    }


    override fun postLogin(authCode: String): Observable<LoginData> {
        return dataStore.getRemoteData(true).postLogin(authCode).map {
            LoginData(
                status = it.status,
                refreshToken = it.refreshToken,
                email = it.email
            )
        }
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

    override fun getUser(): Observable<DomainUserData> {
//        val userData = dataStore.getRemoteData(false).getUser()
//        return Observable.just(DomainUserData(
//            id = userData.value?.user_id?:0,
//            email = userData.value?.user_email?:"",
//            token = userData.value?.user_token?:"",
//        ))

        return dataStore.getRemoteData(false).getUser()
            .map{
                DomainUserData(
                    id = it.user_id?:0 ,
                    email = it.user_email?:"",
                    token = it.user_token?:""
                )
            }
    }


}