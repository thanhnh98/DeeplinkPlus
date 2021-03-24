package com.thanh.deeplinkplus.repo.update

import com.thanh.deeplinkplus.model.UpdateModel
import com.thanh.deeplinkplus.repo.Service
import io.reactivex.Observable

class UpdateRepoImpl(private val service: Service): UpdateRepo {
    override suspend fun getUpdateInfo(): UpdateModel {
        return service.getUpdateInfoCoroutine()
    }
}