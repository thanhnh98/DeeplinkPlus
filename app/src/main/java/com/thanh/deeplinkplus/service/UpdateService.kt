package com.thanh.deeplinkplus.service

import com.thanh.deeplinkplus.model.UpdateModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface UpdateService {
    @GET("update_checking")
    fun getUpdateInfo(): Observable<UpdateModel>

    @GET("update_checking")
    suspend fun getUpdateInfoCoroutine(): UpdateModel
}