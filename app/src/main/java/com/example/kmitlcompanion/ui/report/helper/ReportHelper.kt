package com.example.kmitlcompanion.ui.report.helper

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
internal class ReportHelper @Inject constructor(
    val viewReport: ViewReport,
    val spinnerReportHelper: SpinnerReportHelper,
)