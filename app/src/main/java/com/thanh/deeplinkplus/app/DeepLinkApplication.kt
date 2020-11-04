package com.thanh.deeplinkplus.app

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.thanh.deeplinkplus.common.resources.Resources
import com.thanh.deeplinkplus.network.AppClient
import com.thanh.deeplinkplus.network.AppClientForCoroutine
import com.thanh.deeplinkplus.storage.AppPreferences
import com.thanh.deeplinkplus.storage.local_db.database.AppDatabase

class DeepLinkApplication: Application(), LifecycleObserver{
    override fun onCreate() {
        super.onCreate()
        Resources.init(this)
        AppPreferences.init(this)
        AppClient.init()
        AppClientForCoroutine.init()
        AppDatabase.init(this)
    }
}