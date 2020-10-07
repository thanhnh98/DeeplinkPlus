package com.thanh.deeplinkplus.screen.home.presenter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.thanh.deeplinkplus.storage.AppPreferences
import com.thanh.deeplinkplus.common.base.BasePresenter
import com.thanh.deeplinkplus.extension.isSafe
import com.thanh.deeplinkplus.model.UrlModel
import com.thanh.deeplinkplus.screen.home.IHomeView
import com.thanh.deeplinkplus.usecase.UrlUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class HomePresenter(view: IHomeView): BasePresenter<IHomeView>(view), IHomePresenter{

    private var urlUseCase = UrlUseCase.getInstance()
    private var mComposite = CompositeDisposable()

    override fun requestHandleIntent(link: String) {
        if (!link.isSafe()){
            mView.onError("Có gì đó sai sai, vui lòng thử lại")
        }else{
            try {
                mView.handleIntent(Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(link)
                })

                //AppPreferences.getInstance().saveUrl(UrlModel(link))

            }catch (e: ActivityNotFoundException){
                mView.onError("Không tìm được ứng dụng phù hợp")
            }catch (e: Exception){
                mView.onError("Có gì đó sai sai, vui lòng thử lại")
            }
        }
    }

    override fun requestCleanUrls() {
        TODO("Not yet implemented")
    }

    override fun requestRemoveUrl(url: UrlModel) {
        TODO("Not yet implemented")
    }

    override fun requestShowListUrl() {
        mComposite.add(
            urlUseCase.getListUrlFromLocal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { res -> mView.showListUrl(res) },
                    {err -> err.printStackTrace()}

                )
        )
    }

    override fun onDestroy() {
        mComposite.clear()
    }
}