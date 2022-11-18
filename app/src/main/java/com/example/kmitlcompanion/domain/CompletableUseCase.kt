package com.example.kmitlcompanion.domain

import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class CompletableUseCase<in Params> constructor(
    private val postExecutionThread: PostExecutionThread
) {

    private val disposables = CompositeDisposable()

    protected abstract fun buildUseCaseCompletable(params: Params? = null): Completable

    open fun execute(observer: DisposableCompletableObserver, params: Params? = null) {
        val completable = buildUseCaseCompletable(params)
            .subscribeOn(Schedulers.io())
            .observeOn(postExecutionThread.scheduler)
        addDisposable(completable.subscribeWith(observer))
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun dispose() {
        if (!disposables.isDisposed) disposables.dispose()
    }

}