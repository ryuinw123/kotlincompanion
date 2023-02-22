package com.example.kmitlcompanion.ui.mapboxview.helpers

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
internal class ViewHelper @Inject constructor(
    val map: ViewMap,
    val list : ViewList,
    val slider: ViewSlider,
    val comment: ViewComment,
    val location : ViewLocation,
    val geofence: ViewGeofence,
    val service: ViewService,
    val like: ViewLike,
    val navigation : ViewNavigation,
    val search : ViewSearch,
    val tag : ViewTag,
    val bookMark: ViewBookMark,
    val createSheet : ViewCreateSheet,
    val editDeleteMarker: ViewEditDeleteMarker,

)