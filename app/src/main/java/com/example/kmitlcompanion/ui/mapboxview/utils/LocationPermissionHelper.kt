package com.example.kmitlcompanion.ui.mapboxview.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.ref.WeakReference
import javax.inject.Inject

@ActivityScoped
class LocationPermissionHelper @Inject constructor(
    @ActivityContext private val context: Context,
    private val toasterUtil: ToasterUtil,
    ) {
    private lateinit var permissionsManager: PermissionsManager

    fun checkPermissions(onMapReady: () -> Unit) {
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
            onMapReady()
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                    toasterUtil.showToast("You need to accept location permissions.")
                    Toast.makeText(
                        context, "You need to accept location permissions.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        onMapReady()
                    } else {
                        (context as Activity).finish()
                    }
                }
            })
            permissionsManager.requestLocationPermissions(context as Activity)
        }
    }

}