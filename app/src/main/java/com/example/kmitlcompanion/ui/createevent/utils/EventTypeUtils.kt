package com.example.kmitlcompanion.ui.createevent.utils

import javax.inject.Inject

class EventTypeUtils @Inject constructor() {

    private val eventType = mutableListOf<String>("อีเวนต์ทั่วไป","อีเวนต์และลิ้งภายนอก")
    private val eventTypeCode = mutableListOf<Int>(0,1)

    fun getType() : ArrayList<String>{
        return ArrayList(eventType)
    }

    fun getURLType() : String{
        return eventType.get(1)
    }

    fun getURLCode() : Int{
        return eventTypeCode.get(1)
    }

    fun getTypeByCode(code : String) : String{
        return eventType.get(code.toInt())
    }

    fun getCodeByType(type : String) : Int{
        return eventTypeCode.get(eventType.indexOf(type))
    }

}