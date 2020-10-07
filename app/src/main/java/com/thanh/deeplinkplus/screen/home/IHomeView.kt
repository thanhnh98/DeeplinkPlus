package com.thanh.deeplinkplus.screen.home

import android.content.Intent
import com.thanh.deeplinkplus.model.UrlModel

interface IHomeView{
    fun onError(msg: String)
    fun handleIntent(intent: Intent)
    fun showListUrl(listData: List<UrlModel>)
}