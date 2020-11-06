package com.thanh.deeplinkplus.screen.home

import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.EditText
import com.thanh.deeplinkplus.R
import com.thanh.deeplinkplus.common.base.BasePresenter
import com.thanh.deeplinkplus.common.resources.Resources
import com.thanh.deeplinkplus.extension.createDebounce
import com.thanh.deeplinkplus.extension.isSafe
import com.thanh.deeplinkplus.extension.text
import com.thanh.deeplinkplus.model.ActionDataChanged
import com.thanh.deeplinkplus.model.UpdateModel
import com.thanh.deeplinkplus.model.UrlModel
import com.thanh.deeplinkplus.model.converter.TypeUrl
import com.thanh.deeplinkplus.network.AppClient
import com.thanh.deeplinkplus.screen.home.HomeContact.Presenter
import com.thanh.deeplinkplus.screen.home.HomeContact.View
import com.thanh.deeplinkplus.service.UpdateService
import com.thanh.deeplinkplus.storage.AppPreferences
import com.thanh.deeplinkplus.usecase.UrlUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomePresenter(view: View): BasePresenter<View>(view), Presenter {

    private var urlUseCase = UrlUseCase.getInstance()
    private lateinit var listUrl: MutableList<UrlModel>
    val regexUniversal: Regex = Regex("^((https?|ftp|smtp):\\/\\/)?(www.)?[a-z0-9]+\\.[a-z]+(\\/[a-zA-Z0-9#]+\\/?)*\$")
    val regexDeeplink: Regex = Regex("^(\\w*:\\/\\/).+\$")

    override fun requestHandleIntent(link: String) {
        if (!link.isSafe()){
            mView.onError(Resources.getString(R.string.err_msg_something_wrong))
        }else{
            try {
                if (link.matches(regexUniversal)){
                    mView.showWebView(link)
                    saveUrlToLocal(link, TypeUrl.UNIVERSAL_LINK)
                }
                else if (link.matches(regexDeeplink)){
                    mView.handleIntent(Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse(link)
                    })
                    saveUrlToLocal(link, TypeUrl.DEEP_LINK)
                }
                else
                    mView.onError(Resources.getString(R.string.err_msg_something_wrong))

            }catch (e: ActivityNotFoundException){
                e.printStackTrace()
                mView.onError(Resources.getString(R.string.err_msg_not_found))
            }catch (e: Exception){
                mView.onError(Resources.getString(R.string.err_msg_something_wrong))
            }
        }
    }

    override fun requestCleanUrls() {
        add(
            urlUseCase.clearAllUrl().subscribe()
        )
    }

    private fun saveUrlToLocal(link: String, type: Int){
        add(
            urlUseCase.saveUrl(urlUseCase.generateUrlModel(link, type))
                .subscribe()
        )
    }

    override fun requestRemoveUrl(url: UrlModel) {
        add(
            urlUseCase.removeUrlById(url).subscribe()
        )
    }

    override fun requestShowListUrl() {
        add(
            urlUseCase.getListUrlFromLocal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { res ->
                        res.apply {
                            if (size == 0)
                                mView.showEmpty()
                            else {
                                listUrl = reversed().toMutableList()
                                mView.showListUrl(listUrl)
                            }
                        }
                    },
                    { err -> err.printStackTrace() }

                )
        )
    }

    override fun onDestroy() {
        mComposite.clear()
    }

    override fun subscribeEventOnListUrlChanged() {
        add(
            urlUseCase.onUrlDataChanged().subscribe(
                { res -> handleOnUrlChanged(res) },
                { err -> err.printStackTrace() }
            )
        )
    }

    override fun requestVersionName(): String {
        try {
            val versionName = Resources.getContext().packageManager
                .getPackageInfo(Resources.getContext().packageName, 0).versionName
            return Resources.getString(R.string.version_name).replace("%s", versionName)
        }catch (e: Exception){
            e.printStackTrace()
        }

        return ""
    }

    override fun copyText(text: String?) {
        if (text.isNullOrEmpty())
            mView.copySuccess("Nothing...")
        else {
            Resources.copyToClipboard(text)
            mView.copySuccess(Resources.getString(R.string.copy_success))
        }
    }

    override fun getClipboard(context: Context): String {
        val clipboard = context
            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return clipboard.primaryClip?.getItemAt(0)?.text.toString()
    }

    override fun requestCheckingUpdate() {
//        val service: UpdateService = AppClient.createService(UpdateService::class.java)
//        add(service.getUpdateInfo()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                {res -> shouldShowDialogUpdate(res)},
//                {err -> err.printStackTrace()}
//            ))
    }

    override fun onStatusShowDialogUpdateChanged(boolean: Boolean) {
        AppPreferences.getInstance().setValueShouldShowDialog(!boolean)
    }

    override fun createDebounceEdt(edt: EditText) {
        edt.createDebounce(mComposite){
            mView.showLinkMode(getModeName(getModeIdByString(edt.text())))
        }
    }

    private fun getModeIdByString(s: String): Int{
        if(s.matches(regexUniversal))
            return TypeUrl.UNIVERSAL_LINK
        if (s.matches(regexDeeplink))
            return TypeUrl.DEEP_LINK
        return TypeUrl.OTHER
    }

    fun shouldShowDialogUpdate(update: UpdateModel){

        if (update.isForceUpdate || (AppPreferences.getInstance().getLastVersionShowPopup().isNotEmpty() && update.version != AppPreferences.getInstance().getLastVersionShowPopup()) || (Resources.getString(R.string.version_name).replace("%s", update.version) != requestVersionName() && AppPreferences.getInstance().shouldShowUpdateDialog()))
            mView.showDialogCheckingUpdate(update)
    }

    private fun handleOnUrlChanged(pair: Pair<ActionDataChanged, UrlModel>){
        when(pair.first){
            ActionDataChanged.DELETE -> {
                val id = getPositionAndRemove(pair.second)
                if (id != -1)
                    mView.removeUrlFromList(id)
            }
            ActionDataChanged.INSERT -> {
                mView.insertUrlIntoList(pair.second)
            }
            ActionDataChanged.DELETE_ALL -> {
                mView.showEmpty()
            }
        }
    }

    private fun getPositionAndRemove(url: UrlModel): Int{

        for ((index, urlIndexed) in listUrl.withIndex())
            if (url.id == urlIndexed.id){
                listUrl.removeAt(index)
                return index
            }
        return -1
    }

    private fun getModeName(id: Int): String{
        return when(id){
            TypeUrl.DEEP_LINK -> Resources.getString(R.string.mode_deep_link)
            TypeUrl.UNIVERSAL_LINK -> Resources.getString(R.string.mode_universal_link)
            else -> Resources.getString(R.string.mode_other)
        }
    }
}