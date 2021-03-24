package com.thanh.deeplinkplus.usecase

import com.thanh.deeplinkplus.common.SingleLiveEvent
import com.thanh.deeplinkplus.model.*
import com.thanh.deeplinkplus.repo.update.UpdateRepo
import com.thanh.deeplinkplus.storage.AppPreferences
import com.thanh.deeplinkplus.storage.local_db.database.AppDatabase

class UrlUseCaseImpl(val mPreferences: AppPreferences, val mDatabase: AppDatabase, val updateRepo: UpdateRepo): UrlUseCase {

    private var urlDataNotifier = SingleLiveEvent<ActionModel>() //pass action and model -> auto handle
    private var listUrlNotifier = SingleLiveEvent<List<UrlModel>>()

    override fun onListUrlItemChanged(): SingleLiveEvent<ActionModel>{
        return urlDataNotifier
    }

    override fun onListUrlUpdated(): SingleLiveEvent<List<UrlModel>>{
        return listUrlNotifier
    }

    override suspend fun saveUrl(urlModel: UrlModel){
        return mDatabase.getUrlDao()
            .insertUrl(urlModel)
    }

    override suspend fun removeUrlById(urlModel: UrlModel): Int{
        return mDatabase.getUrlDao()
            .removeUrl(urlModel.id)
    }

    override suspend fun clearAllUrl(): Int{
        return mDatabase.getUrlDao()
            .removeAllLocalData()
    }

    override suspend fun getListUrlFromLocal(): List<UrlModel>{
        return mDatabase.getUrlDao()
            .getListUrl()
    }

    override suspend fun requestDataUpdate(): UpdateModel {
        return updateRepo.getUpdateInfo()
    }
}