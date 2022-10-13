package com.example.kmitlcompanion.data

import com.example.kmitlcompanion.data.mapper.MapPointMapper
import com.example.kmitlcompanion.data.store.DataStore
import com.example.kmitlcompanion.data.util.TimeUtils
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.domain.model.Source
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val dataStore: DataStore,
    private val timeUtils: TimeUtils,
    private val mapper: MapPointMapper

) : DomainRepository{
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


    override fun postLogin(token: String): Observable<Int> {
        return dataStore.getRemoteData(true).postLogin(token)
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

}