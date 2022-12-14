package com.example.kmitlcompanion.ui.mapboxview.helpers

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
internal class ViewHelper @Inject constructor(
    val map: ViewMap
)