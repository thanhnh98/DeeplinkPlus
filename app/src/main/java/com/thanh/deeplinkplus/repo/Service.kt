package com.thanh.deeplinkplus.repo

import com.thanh.deeplinkplus.model.UpdateModel
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface Service {
    @GET("update_checking")
    fun getUpdateInfo(): Observable<UpdateModel>

    @GET("update_checking")
    suspend fun getUpdateInfoCoroutine(): UpdateModel
}