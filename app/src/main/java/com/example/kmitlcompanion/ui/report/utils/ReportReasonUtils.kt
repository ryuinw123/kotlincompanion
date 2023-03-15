package com.example.kmitlcompanion.ui.report.utils

import javax.inject.Inject

class ReportReasonUtils @Inject constructor() {


    private val reasonMarker = mutableListOf<String>("พบชื่อหรือข้อความไม่เหมาะสมในปักหมุด",
        "พบรูปภาพไม่เหมาะสมในปักหมุด",
        "พบความคิดเห็นที่ไม่เหมาะสม",
        "อื่นๆ",)

    private val reasonEvent = mutableListOf<String>("พบชื่อหรือข้อความไม่เหมาะสมในอีเวนต์",
        "พบรูปภาพไม่เหมาะสมในอีเวนต์",
        "การจัดอีเวนต์ไม่เหมาะสม",
        "อื่นๆ",)

    fun getReason(isEvent : Boolean) : ArrayList<String>{
        return if (isEvent){
            ArrayList(reasonEvent)
        }else{
            ArrayList(reasonMarker)
        }
    }


}