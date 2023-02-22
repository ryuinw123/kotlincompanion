package com.example.kmitlcompanion.data.mapper

import com.example.kmitlcompanion.data.model.SearchDataDetails
import com.example.kmitlcompanion.domain.model.SearchDetail
import com.mapbox.maps.extension.style.expressions.dsl.generated.distance
import javax.inject.Inject

class SearchMapper @Inject constructor() {

    fun mapToDomain(it: SearchDataDetails): SearchDetail {
        return SearchDetail(
            id = it.id,
            name = it.name,
            place = it.place,
            address = it.address,
            pic = it.pic,
            code = it.code,
            distance = it.distance,
        )
    }

}