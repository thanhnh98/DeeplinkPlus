package com.thanh.deeplinkplus.screen.home.presenter

import com.thanh.deeplinkplus.model.UrlModel

interface IHomePresenter{
    fun requestHandleIntent(link: String)
    fun requestCleanUrls()
    fun requestRemoveUrl(url: UrlModel)
    fun requestShowListUrl()
    fun onDestroy()
}