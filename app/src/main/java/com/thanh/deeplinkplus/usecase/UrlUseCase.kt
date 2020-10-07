package com.thanh.deeplinkplus.usecase

import com.thanh.deeplinkplus.model.ActionDataChanged
import com.thanh.deeplinkplus.model.PinModel
import com.thanh.deeplinkplus.model.UrlModel
import com.thanh.deeplinkplus.model.UserModel
import com.thanh.deeplinkplus.model.converter.TypeUrl
import com.thanh.deeplinkplus.storage.AppPreferences
import com.thanh.deeplinkplus.storage.local_db.database.AppDatabase
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class UrlUseCase {
    companion object{
        var urlUseCase: UrlUseCase? = null

        fun getInstance(): UrlUseCase{
            if (urlUseCase == null)
                urlUseCase = UrlUseCase()
            val urlUseCaseInstance: UrlUseCase? = urlUseCase
            return urlUseCaseInstance?: throw IllegalStateException("urlUseCaseInstance is NULL")
        }
    }

    private var mPreferences: AppPreferences = AppPreferences.getInstance()
    private var mDatabase = AppDatabase.getInstance()
    private var urlDataChanged: BehaviorSubject<Pair<ActionDataChanged, UrlModel>> = BehaviorSubject.create()

    fun saveUrl(urlModel: UrlModel): Observable<Unit>{
        return mDatabase.getUrlDao()
            .insertUrl(urlModel)
            .toObservable()
            .doOnNext {
                urlDataChanged.onNext(Pair(ActionDataChanged.INSERT, urlModel))
            }
    }

    fun removeUrlById(urlModel: UrlModel): Observable<Int>{
        return mDatabase.getUrlDao()
            .removeUrl(urlModel.id)
            .toObservable()
            .doOnNext {
                urlDataChanged.onNext(Pair(ActionDataChanged.DELETE, urlModel))
            }
    }

    fun getListUrlFromLocal(): Observable<List<UrlModel>>{
        return mDatabase.getUrlDao().getListUrl().toObservable()
    }

    fun onUrlDataChanged(): Observable<Pair<ActionDataChanged, UrlModel>>{
        return urlDataChanged
    }

    fun generateUrlModel(url: String): UrlModel{
        val url = url
        val createdAt: Long = System.currentTimeMillis()
        val createdBy = UserModel("Thanh")
        val pin = PinModel(System.currentTimeMillis().toString())
        val isEnable = true
        val typeUrl = TypeUrl.DEEP_LINK
        return UrlModel(url, createdAt.toString(), createdBy, typeUrl, isEnable, pin)
    }
}