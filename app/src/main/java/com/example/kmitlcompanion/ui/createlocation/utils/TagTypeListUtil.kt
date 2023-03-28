package com.example.kmitlcompanion.ui.createlocation.utils

import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.domain.model.TagDetail
import javax.inject.Inject

class TagTypeListUtil @Inject constructor(
) {

    //"ร้านอาหาร","ตึก","ร้านค้า","หอพัก"
    private val tagTypeDetailsList = mutableListOf<TagDetail>(
        TagDetail(99,"ทั่วไป",R.drawable.ic_location_on_red_24dp),
        TagDetail(0,"ร้านอาหาร", R.drawable.tag_restaurant),
        TagDetail(6,"ห้องน้ำ",R.drawable.tag_toilet),
        TagDetail(1,"อาคารเรียน",R.drawable.tag_school),
        TagDetail(2,"ห้องเรียน",R.drawable.tag_room),
        TagDetail(3,"ร้านค้า",R.drawable.tag_shop),
        TagDetail(7,"ธนาคาร",R.drawable.tag_bank),
        TagDetail(4,"สถานที่",R.drawable.tag_building),
        TagDetail(5,"หอพัก",R.drawable.tag_dorm),
    )

    //spatial tag
    private val spacialTagTypeList = mutableListOf<TagDetail>(
        TagDetail(100,"สถานที่ของฉัน",R.drawable.ic_baseline_bookmark_24),
        TagDetail(969,"อีเวนต์",R.drawable.ic_event_48px),
    )

    fun getMutableListOfTagTypeString() : MutableList<String> {
        val newList = tagTypeDetailsList.map{
                        it.name
                    }.toMutableList()
        return newList
    }

    fun getAllMutableListOfTagTypeDetails() : MutableList<TagDetail>{
        return mutableListOf<TagDetail>().plus(tagTypeDetailsList).plus(spacialTagTypeList) as MutableList<TagDetail>
    }

    fun getAvailableMutableListOfTagTypeDetails() : MutableList<TagDetail>{
        return mutableListOf<TagDetail>()
            .plus(spacialTagTypeList[0]) //bookmark
            .plus(spacialTagTypeList[1]) //event
            .plus(tagTypeDetailsList.slice(IntRange(0, tagTypeDetailsList.size - 1)))
                as MutableList<TagDetail>
    }

    fun typeToTagCode(thaiTagName : String) : Int{
        var code = tagTypeDetailsList.firstOrNull { it.name == thaiTagName }?.code
        return code!!
    }

    fun codeToTagName(code : Int) : String {
        var name = tagTypeDetailsList.firstOrNull { it.code == code }?.name
        return name ?:""
    }

    fun getImageByCode(code : Int): Int{
        var image = (tagTypeDetailsList.plus(spacialTagTypeList)).firstOrNull { it.code == code }?.icon
        return image!!
    }

    fun getEventTagCode() : Int{
        return spacialTagTypeList[1].code
    }

}