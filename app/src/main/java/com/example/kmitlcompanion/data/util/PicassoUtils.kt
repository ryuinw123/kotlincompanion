package com.example.kmitlcompanion.data.util

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.squareup.picasso.PicassoProvider
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object PicassoUtils {
    private var picasso: Picasso? = null

    fun getPicasso(context: Context): Picasso {
        if (picasso == null) {
            val client = UnsafeOkHttpClientUtils.getUnsafeOkHttpClient()
            picasso = Picasso.Builder(context).downloader(OkHttp3Downloader(client)).build()
        }
        return picasso!!
    }
}