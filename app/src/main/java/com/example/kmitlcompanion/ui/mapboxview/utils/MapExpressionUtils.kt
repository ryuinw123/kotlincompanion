package com.example.kmitlcompanion.ui.mapboxview.utils

import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.ui.mapboxview.helpers.ViewMap
import com.mapbox.maps.extension.style.StyleContract
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.image.ImageExtensionImpl
import com.mapbox.maps.extension.style.image.image
import com.mapbox.maps.extension.style.style
import javax.inject.Inject

class MapExpressionUtils @Inject constructor(
    private val bitmapConverter: BitmapUtils,
) {

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

            eq {get{literal(ViewMap.TAG)}
                literal(6)}
            literal(ViewMap.TOILET)

            eq {get{literal(ViewMap.TAG)}
                literal(7)}
            literal(ViewMap.BANK)

            literal(ViewMap.LOCATION)
        }
    }

    fun getImageStyle(styleUri : String) : StyleContract.StyleExtension {
        val styleExtension = style(styleUri = styleUri) {
            +image(ViewMap.LOCATION) {
                var bitmap = bitmapConverter.bitmapFromDrawableRes(R.drawable.type_pin)!!
                bitmap(bitmap)
            }
            +image(ViewMap.RESTAURANT) {
                bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_restaurant)!!)
            }
            +image(ViewMap.SCHOOL) {
                bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_school)!!)
            }
            +image(ViewMap.ROOM) {
                bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_room)!!)
            }
            +image(ViewMap.SHOP) {
                bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_shop)!!)
            }
            +image(ViewMap.BUILDING) {
                bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_building)!!)
            }
            +image(ViewMap.DORM) {
                bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_dorm)!!)
            }
            +image(ViewMap.TOILET) {
                bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_toilet)!!)
            }
            +image(ViewMap.BANK) {
                bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_bank)!!)
            }
        }
        return styleExtension
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