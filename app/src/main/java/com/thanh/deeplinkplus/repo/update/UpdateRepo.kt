package com.thanh.deeplinkplus.repo.update

import com.thanh.deeplinkplus.model.UpdateModel
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface UpdateRepo {
    suspend fun getUpdateInfo(): UpdateModel
}