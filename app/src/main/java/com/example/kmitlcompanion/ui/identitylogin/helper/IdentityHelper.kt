package com.example.kmitlcompanion.ui.identitylogin.helper

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
internal class IdentityHelper @Inject constructor(
    val api: ApiHelper,
    val validation: ValidatationHelper
)