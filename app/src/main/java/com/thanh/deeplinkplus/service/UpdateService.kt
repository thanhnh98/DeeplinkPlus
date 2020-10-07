package com.thanh.deeplinkplus.service

import com.thanh.deeplinkplus.model.UpdateModel
import io.reactivex.Observable
import retrofit2.http.GET

interface UpdateService {
    @GET("update_checking")
    fun getUpdateInfo(): Observable<UpdateModel>
}