package com.thanh.deeplinkplus.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleObserver
import com.thanh.deeplinkplus.common.resources.Resources
import com.thanh.deeplinkplus.di.*
import com.thanh.deeplinkplus.storage.AppPreferences
import com.thanh.deeplinkplus.storage.local_db.database.AppDatabase
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class DeepLinkApplication: Application(), LifecycleObserver {
    companion object{
        lateinit var appContext: Context
    }
    override fun onCreate() {
        Resources.init(this)
        AppPreferences.init(this)
        AppDatabase.init(this)
        super.onCreate()
        startKoin {
            modules(appModule, serviceModule, useCaseModule, viewModelModule)
            printLogger(Level.DEBUG)
        }
    }
}