package com.thanh.deeplinkplus.usecase

import androidx.room.Update
import com.thanh.deeplinkplus.common.SingleLiveEvent
import com.thanh.deeplinkplus.model.ActionDataChanged
import com.thanh.deeplinkplus.model.ActionModel
import com.thanh.deeplinkplus.model.UpdateModel
import com.thanh.deeplinkplus.model.UrlModel
import io.reactivex.Observable

interface UrlUseCase {
    suspend fun saveUrl(urlModel: UrlModel)
    suspend fun removeUrlById(urlModel: UrlModel): Int
    suspend fun clearAllUrl(): Int
    suspend fun getListUrlFromLocal(): List<UrlModel>
    suspend fun requestDataUpdate(): UpdateModel
    fun onListUrlUpdated(): SingleLiveEvent<List<UrlModel>>
    fun onListUrlItemChanged(): SingleLiveEvent<ActionModel>
}