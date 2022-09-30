package com.example.kmitlcompanion.remote

import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.remote.service.RetrofitTestClient
import com.example.kmitlcompanion.data.repository.RemoteRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RetrofitClient @Inject constructor(
    private val retrofitTestClient: RetrofitTestClient,
) : RemoteRepository {
    override fun getMapPoints(): Observable<List<MapPointData>> {
        return retrofitTestClient.getMapPoints()
    }
}