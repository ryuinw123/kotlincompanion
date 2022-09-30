package com.example.kmitlcompanion.domain

import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class ObservableUseCase<T : Any, in Params> constructor(
    private val postExecutionThread: PostExecutionThread
) {

    var observer: DisposableObserver<T>? = null
    private val disposables = CompositeDisposable()


    protected abstract fun buildUseCaseObservable(params: Params? = null): Observable<T>

    fun execute(observer: DisposableObserver<T>, params: Params? = null) {
        this.observer = observer

        val observable = buildUseCaseObservable(params)
            .subscribeOn(Schedulers.io())
            .observeOn(postExecutionThread.scheduler)

        addDisposable(observable.subscribeWith(observer))
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun dispose() {
        if (!disposables.isDisposed) disposables.dispose()
    }
}