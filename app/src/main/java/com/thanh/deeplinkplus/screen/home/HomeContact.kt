package com.thanh.deeplinkplus.screen.home

import android.content.Context
import android.content.Intent
import com.thanh.deeplinkplus.common.base.IBasePresenter
import com.thanh.deeplinkplus.common.base.IBaseView
import com.thanh.deeplinkplus.model.UpdateModel
import com.thanh.deeplinkplus.model.UrlModel

interface HomeContact {
    interface Presenter: IBasePresenter{
        fun requestHandleIntent(link: String)
        fun requestCleanUrls()
        fun requestRemoveUrl(url: UrlModel)
        fun requestShowListUrl()
        fun subscribeEventOnListUrlChanged()
        fun requestVersionName(): String
        fun copyText(text: String?)
        fun getClipboard(context: Context): String
        fun requestCheckingUpdate()
        fun onStatusShowDialogUpdateChanged(boolean: Boolean)
    }
    interface View: IBaseView{
        fun onError(msg: String)
        fun handleIntent(intent: Intent)
        fun showListUrl(listData: List<UrlModel>)
        fun removeUrlFromList(position: Int)
        fun insertUrlIntoList(url: UrlModel)
        fun showEmpty()
        fun showWebView(url: String)
        fun copySuccess(msg: String)
        fun showDialogCheckingUpdate(update: UpdateModel)
    }
}