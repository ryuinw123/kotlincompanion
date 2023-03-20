package com.example.kmitlcompanion.ui.createevent.helper

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CreateEventHelper @Inject constructor(
    val image: ImageHelper,
    val upload : UploadHelper,
    val datetime : DateTimePickHelper,
    val spinner : EventTypeSpinnerHelper,
)