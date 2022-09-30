package com.example.kmitlcompanion.domain.repository

import com.example.kmitlcompanion.domain.model.MapInformation
import io.reactivex.rxjava3.core.Observable

interface DomainRepository {

    fun getMapPoints() : Observable<MapInformation>


}