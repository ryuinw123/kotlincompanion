package com.example.kmitlcompanion.cache

import android.media.metrics.Event
import android.util.Log
import com.example.kmitlcompanion.cache.database.AppDatabase
import com.example.kmitlcompanion.cache.database.constants.NameTable
import com.example.kmitlcompanion.cache.entities.DataProperty
import com.example.kmitlcompanion.cache.entities.NotiLogEntity
import com.example.kmitlcompanion.cache.entities.EventTime
import com.example.kmitlcompanion.cache.entities.User
import com.example.kmitlcompanion.cache.mapper.MapPointMapper
import com.example.kmitlcompanion.cache.mapper.UserMapper
import com.example.kmitlcompanion.data.model.EventTimeData
import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.data.model.NotiLogData
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.data.repository.CacheRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val database: AppDatabase,
    private val mapPointMapper: MapPointMapper,
    private val userMapper: UserMapper
) : CacheRepository {
    override fun getLastestNotificationTime(event_id: Int, user_id: Int): Observable<List<Long>> {
        return database.cachedDao().getLastestNotificationTime(event_id , user_id)
    }

    override fun updateNotificationTime(eventData: EventTimeData): Completable {
        return database.cachedDao().updateNotificationTime(
            EventTime(eventData.event_id,eventData.user_id,eventData.time)
        )
    }

    override fun getMapPoints(token: String): Observable<List<MapPointData>> {
        return database.cachedDao().getMapPoints()
            .flatMap { list ->
                Observable.just(list.map { mapPointMapper.mapToData(it) })
            }
    }

    override fun saveMapPoints(list: List<MapPointData>): Completable {
        return database.cachedDao()
            .saveMapPoints(list.map { mapPointMapper.mapToEntity(it) })
    }

    override fun updateLastLocationTimeStamp(timestamp: Long): Completable {
        return database.cachedDao().updateProperty(
            DataProperty(
                id = NameTable.DATA_LAST_LOCATION_TIMESTAMP_ID,
                property = timestamp.toString()
            )
        )
    }

    override fun updateUser(email: String, token: String): Completable {
        return database.cachedDao().updateUser(
            User(
                id = 0,
                email = email,
                token = token
            )
        )
    }

    override fun getUser(): Observable<List<UserData>> {
        return database.cachedDao().getUser().flatMap { list ->
                Observable.just(list.map { userMapper.mapToData(it) })
            }
    }

    override fun saveNotificationLogDetails(
        id: Long,
        name: String,
        startTime: String,
        endTime: String,
        imageLinks: String
    ): Completable {
        return database.cachedDao().saveEventNotiLog(
            NotiLogEntity(
                id = id,
                name = name,
                startTime = startTime,
                endTime = endTime,
                imageLinks = imageLinks,
            )
        )
    }

    override fun getNotificationLogDetails(): Observable<List<NotiLogData>> {
        return database.cachedDao().getEventNotiLog().map { listNotiLog ->
            listNotiLog.map {
                NotiLogData(
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
        return database.cachedDao().deleteAllEventNotiLog()
    }

    override fun deleteByIDNotificationLogDetails(id: Long): Completable {
        return database.cachedDao().deleteEventNotiLogById(id)
    }
}
