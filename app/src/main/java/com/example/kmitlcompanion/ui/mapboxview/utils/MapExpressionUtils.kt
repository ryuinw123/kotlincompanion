package com.example.kmitlcompanion.ui.mapboxview.utils

import com.example.kmitlcompanion.ui.mapboxview.helpers.ViewMap
import com.mapbox.maps.extension.style.expressions.generated.Expression
import javax.inject.Inject

class MapExpressionUtils @Inject constructor() {

    fun getImageExpression() : Expression {
        return Expression.switchCase{
            eq {get{literal(ViewMap.TAG)}
                literal(0)}
            literal(ViewMap.RESTAURANT)

            eq {get{literal(ViewMap.TAG)}
                literal(1)}
            literal(ViewMap.SCHOOL)

            eq {get{literal(ViewMap.TAG)}
                literal(2)}
            literal(ViewMap.ROOM)

            eq {get{literal(ViewMap.TAG)}
                literal(3)}
            literal(ViewMap.SHOP)

            eq {get{literal(ViewMap.TAG)}
                literal(4)}
            literal(ViewMap.BUILDING)

            eq {get{literal(ViewMap.TAG)}
                literal(5)}
            literal(ViewMap.DORM)

            literal(ViewMap.LOCATION)
        }
    }

    fun getLocationLayersID() : String{
        return ViewMap.LOCATION_LAYER_ID
    }

    fun getLocationSourceID() : String{
        return ViewMap.SOURCE_ID
    }

    fun getTagExpression() : String{
        return ViewMap.TAG
    }

    fun getLocationAreaLayersID() : String{
        return ViewMap.AREA_LAYER_ID
    }

    fun getLocationAreaID() : String{
        return ViewMap.AREA_ID
    }


}