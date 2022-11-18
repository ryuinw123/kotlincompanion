package com.example.kmitlcompanion.ui.createmapboxlocation.helpers

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
internal class CreateLocationHelper @Inject constructor(
    val map:CreateLocationMap,
    val camera:CreateLocationCamera
){
}