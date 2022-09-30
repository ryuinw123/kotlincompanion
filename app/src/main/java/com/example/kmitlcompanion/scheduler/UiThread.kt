package com.example.kmitlcompanion.scheduler

import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class UiThread @Inject constructor() : PostExecutionThread {

    override val scheduler: Scheduler
        get() = AndroidSchedulers.mainThread()
}