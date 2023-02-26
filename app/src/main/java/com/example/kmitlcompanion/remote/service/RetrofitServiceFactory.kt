package com.example.kmitlcompanion.remote.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitServiceFactory {

    fun makeRetrofitTestClient(isDebug: Boolean): RetrofitTestClient {
        val okHttpClient = makeOkHttpClient(
            makeLoggingInterceptor((isDebug))
        )
        return makeRetrofitTestClient(okHttpClient, Gson())
    }


    private fun makeRetrofitTestClient(okHttpClient: OkHttpClient, gson: Gson): RetrofitTestClient {
        val retrofit = Retrofit.Builder()
            //.baseUrl("http://shitduck.duckdns.org:8000/api/")
            //.baseUrl("http://192.168.2.40:8000/api/")
            .baseUrl("http://192.168.96.1:8000/api/") //For Banana PC
            //.baseUrl("http://192.168.1.22:8000/api/") //For Banana Phone
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(RetrofitTestClient::class.java)
    }


    private fun makeOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }

}