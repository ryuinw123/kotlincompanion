package com.example.kmitlcompanion.ui.eventpage.helper

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
internal class NotiLogHelper @Inject constructor(
    val viewNotiLog: ViewNotiLog,
)