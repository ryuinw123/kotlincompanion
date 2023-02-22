package com.example.kmitlcompanion.ui.mapboxview.utils

import android.os.CountDownTimer
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

object TimeCounterUtils {

    private val timeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private var timer: CountDownTimer? = null

    const val COUNTDOWNINTERVAL = 60000L

    interface OnTickListener {
        fun onTick(formattedTime: String)
    }

    interface OnFinishListener {
        fun onFinish()
    }

    fun startCountdownTimer(startTime : String,targetTime: String, onTickListener: OnTickListener, onFinishListener: OnFinishListener) {
        val targetDateTime = timeFormat.parse(targetTime)
        val startDateTime = timeFormat.parse(startTime)
        timer?.cancel()
        timer = null
        timer = object : CountDownTimer(targetDateTime.time - System.currentTimeMillis(), COUNTDOWNINTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                val result = getFormattedTime(millisUntilFinished)
                if (startDateTime.time > System.currentTimeMillis()){
                    onTickListener.onTick("อีเวนต์ยังไม่เริ่ม")
                }else{
                    onTickListener.onTick(result)
                }

            }

            override fun onFinish() {
                onFinishListener.onFinish()
            }
        }.start()
    }

    fun getFormattedTime(millisUntilFinished: Long): String {
        val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
        //val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
        val sb = StringBuilder()
        if (days > 0) {
            sb.append("$days วัน ")
        }
        if (hours > 0) {
            sb.append("$hours ชั่วโมง ")
        }
        if (minutes > 0) {
            sb.append("$minutes นาที ")
        }
//        if (seconds > 0) {
//            sb.append("$seconds วินาที ")
//        }
        return sb.toString()
    }

    fun stopTimer(){
        //Log.d("test_time","stop time")
        timer?.cancel()
    }

//    fun startCountdownTimer(targetTime: String) {
//        val targetDateTime = timeFormat.parse(targetTime)
//        val currentTime = Calendar.getInstance().time
//
//        val remainingMillis = targetDateTime.time - currentTime.time
//        if (remainingMillis > 0) {
//            timer?.cancel()
//            timer = object : CountDownTimer(remainingMillis, 1000) {
//                override fun onTick(millisUntilFinished: Long) {
//                    val remainingTime = getRemainingTimeString(millisUntilFinished)
//                    Log.d("test_time",remainingTime)
//                    //_remainingTime.value = remainingTime
//                }
//
//                override fun onFinish() {
//                    Log.d("test_time","00:00:00")
//                    //_remainingTime.value = "00:00:00"
//                }
//            }.start()
//        }
//    }
//
//    private fun getRemainingTimeString(millis: Long): String {
//        val seconds = (millis / 1000) % 60
//        val minutes = (millis / (1000 * 60)) % 60
//        val hours = (millis / (1000 * 60 * 60)) % 24
//        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
//    }


}