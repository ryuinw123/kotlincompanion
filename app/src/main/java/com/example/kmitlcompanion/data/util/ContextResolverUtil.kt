package com.example.kmitlcompanion.data.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import javax.inject.Inject

class ContentResolverUtil @Inject constructor(
   private val contentResolver: ContentResolver
) {
    fun getMediaType(uri: Uri) : MediaType? {
        return contentResolver.getType(uri)?.toMediaTypeOrNull()
    }
}