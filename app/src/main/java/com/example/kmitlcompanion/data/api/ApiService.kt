package com.example.kmitlcompanion.data.api

import com.example.kmitlcompanion.data.test
import com.example.kmitlcompanion.data.testToken
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


val BASE_URL = "http://shitduck.duckdns.org:8000/api/"
//val BASE_URL = "http://ptsv2.com/t/bnnkz-1664525254/"
interface ApiService {

    @GET("todos/1")
    fun getTest(): Call<test>

    @POST("checktoken")
    fun postTest(
        @Body data: testToken
    ): Call<testToken>

    companion object{
        operator fun invoke(): ApiService {
            return Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

}