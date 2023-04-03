package com.example.kmitlcompanion.ui.createlocation.helper

import android.media.Image
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
@ActivityScoped
class CreateLocationHelper @Inject constructor(
    val image: ImageHelper,
    val spinner : SpinnerHelper,
    val upload : UploadHelper,
    val checkValid : CheckValidHelper,
)