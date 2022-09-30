package com.example.kmitlcompanion.data.api


import com.example.kmitlcompanion.data.test
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET


val BASE_URL = "https://jsonplaceholder.typicode.com/"
//val BASE_URL = "http://ptsv2.com/t/bnnkz-1664525254/"
interface ApiService {

    @GET("todos/1")
    fun getTest(): Call<test>

    @FormUrlEncoded
    @POST("post")
    fun postTest(
        @Field("first_name") first: String,
        @Field("last_name") last: String
    ): Call<test>

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