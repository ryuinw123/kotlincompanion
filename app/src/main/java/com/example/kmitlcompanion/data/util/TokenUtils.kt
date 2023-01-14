package com.example.kmitlcompanion.data.util

import javax.inject.Inject

class TokenUtils @Inject constructor(

) {

    fun getToken() : String{
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYyMDEwODkzIiwiZW1haWwiOiI2MjAxMDg5M0BrbWl0bC5hYy50aCIsImV4cCI6MzU2MzAxMTMyNCwiaWF0IjoxNjcwODUxMzI0fQ.dxsOyXLIy-zGTcHXDBGwmJJz63nNxC9OspY41jtNWwQ"
    }

}