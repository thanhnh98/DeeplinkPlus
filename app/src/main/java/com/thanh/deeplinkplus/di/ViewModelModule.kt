package com.thanh.deeplinkplus.di

import androidx.lifecycle.ViewModelProvider
import bindViewModel
import com.thanh.deeplinkplus.screen.home.viewmodel.HomeViewModel
import com.thanh.deeplinkplus.usecase.UrlUseCase
import com.thanh.deeplinkplus.usecase.UrlUseCaseImpl
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val viewModelModule = Kodein.Module("VIEWMODEL_MODULE", false) {

    bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(kodein.direct) }

    bindViewModel<HomeViewModel>() with provider { createHomeViewModel(instance()) }
}

fun createHomeViewModel(urlUseCase: UrlUseCase): HomeViewModel{
    return HomeViewModel(urlUseCase = urlUseCase)
}